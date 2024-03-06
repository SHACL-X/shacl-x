/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */
package org.topbraid.shacl.validation.py;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.graalvm.polyglot.Value;
import org.topbraid.jenax.statistics.ExecStatistics;
import org.topbraid.jenax.statistics.ExecStatisticsManager;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.js.SHACLScriptEngineManager;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.js.ScriptEngineUtil;
import org.topbraid.shacl.model.SHPyExecutable;
import org.topbraid.shacl.py.PyGraph;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.py.model.PyTerm;
import org.topbraid.shacl.util.FailureLog;
import org.topbraid.shacl.validation.ConstraintExecutor;
import org.topbraid.shacl.validation.ValidationEngine;
import org.topbraid.shacl.validation.js.SHACLObject;
import org.topbraid.shacl.vocabulary.DASH;
import org.topbraid.shacl.vocabulary.SH;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractPyExecutor implements ConstraintExecutor {

    protected final static String SHACL = "SHACL";

    @Override
    public void executeConstraint(Constraint constraint, ValidationEngine validationEngine, Collection<RDFNode> focusNodes) {
        ScriptEngine pyEngine = SHACLScriptEngineManager.getCurrentPyEngine();

        Dataset dataset = validationEngine.getDataset();
        URI shapesGraphURI = validationEngine.getShapesGraphURI();
        String functionName = null;
        PyGraph shapesPyGraph = new PyGraph(validationEngine.getShapesModel().getGraph(), pyEngine);
        Model dataModel = dataset.getDefaultModel();
        Object oldSHACL = pyEngine.get(SHACL);
        pyEngine.put(SHACL, new SHACLObject(validationEngine.getShapesGraph(), shapesGraphURI, dataset));
        PyGraph dataPyGraph = new PyGraph(dataModel.getGraph(), pyEngine);
        try {

            pyEngine.put(SH.Py_SHAPES_VAR, shapesPyGraph);
            pyEngine.put(SH.Py_DATA_VAR, dataPyGraph);

            QuerySolutionMap bindings = new QuerySolutionMap();
            bindings.add(SH.currentShapeVar.getName(), constraint.getShapeResource());
            addBindings(constraint, bindings);

            SHPyExecutable executable = getExecutable(constraint);
            functionName = executable.getFunctionName();
            pyEngine.executeLibraries(executable);

            long startTime = System.currentTimeMillis();
            for (RDFNode theFocusNode : focusNodes) {
                validationEngine.checkCanceled();
                Object resultObj;
                bindings.add(SH.thisVar.getVarName(), theFocusNode);

                for (RDFNode valueNode : getValueNodes(validationEngine, constraint, bindings, theFocusNode)) {
                    bindings.add("value", valueNode);
                    resultObj = pyEngine.invokeFunction(functionName, bindings);
                    handlePyResultObject(resultObj, validationEngine, constraint, theFocusNode, valueNode, executable, bindings);
                }
            }
            if (ExecStatisticsManager.get().isRecording()) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                String label = getLabel(constraint);
                ExecStatistics stats = new ExecStatistics(label, null, duration, startTime, constraint.getComponent().asNode());
                ExecStatisticsManager.get().add(Collections.singletonList(stats));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Resource result = validationEngine.createResult(DASH.FailureResult, constraint, null);
            result.addProperty(SH.resultMessage, "Could not execute Python constraint");
            if (SH.PyConstraintComponent.equals(constraint.getComponent())) {
                result.addProperty(SH.sourceConstraint, constraint.getParameterValue());
            }
            FailureLog.get().logFailure("Could not execute Python function \"" + functionName + "\": " + ex);
        } finally {
            dataPyGraph.close();
            shapesPyGraph.close();
            pyEngine.put(SHACL, oldSHACL);
        }
    }

    protected abstract void addBindings(Constraint constraint, QuerySolutionMap bindings);

    protected abstract SHPyExecutable getExecutable(Constraint constraint);

    protected abstract Collection<RDFNode> getValueNodes(ValidationEngine engine, Constraint constraint, QuerySolutionMap bindings, RDFNode focusNode);

    @SuppressWarnings("rawtypes")
    private void addDefaultMessages(ValidationEngine engine, Constraint constraint, Resource messageHolder, Resource fallback, Resource result,
                                    QuerySolution bindings, Map resultObject) {
        if (constraint != null && constraint.getShapeResource().hasProperty(SH.message)) {
            for (Statement s : constraint.getShapeResource().listProperties(SH.message).toList()) {
                result.addProperty(SH.resultMessage, s.getObject());
            }
        } else {

            boolean found = false;
            for (Statement s : messageHolder.listProperties(SH.message).toList()) {
                if (s.getObject().isLiteral()) {
                    QuerySolutionMap map = new QuerySolutionMap();
                    map.addAll(bindings);
                    if (resultObject != null) {
                        for (Object keyObject : resultObject.keySet()) {
                            String key = (String) keyObject;
                            Object value = resultObject.get(key);
                            if (value != null) {
                                Node valueNode = PyFactory.getNode(value);
                                if (valueNode != null) {
                                    map.add(key, result.getModel().asRDFNode(valueNode));
                                }
                            }
                        }
                    }
                    engine.addResultMessage(result, s.getLiteral(), map);
                    found = true;
                }
            }
            if (!found && fallback != null) {
                addDefaultMessages(engine, null, fallback, null, result, bindings, resultObject);
            }
        }
    }

    private Resource createValidationResult(ValidationEngine engine, Constraint constraint, RDFNode focusNode) {
        Resource result = engine.createValidationResult(constraint, focusNode, null, null);
        if (SH.PyConstraintComponent.equals(constraint.getComponent())) {
            result.addProperty(SH.sourceConstraint, constraint.getParameterValue());
        }
        return result;
    }

    private void handlePyResultObject(Object resultObject, ValidationEngine engine, Constraint constraint,
                                      RDFNode focusNode, RDFNode valueNode, Resource messageHolder, QuerySolution bindings) throws Exception {
        if (ScriptEngineUtil.isArray(resultObject)) {
            for (Object ro : Objects.requireNonNull(ScriptEngineUtil.asArray(resultObject))) {
                createValidationResultFromPyObject(engine, constraint, focusNode, messageHolder, bindings, ro);
            }
        } else {
            Object resultObj = resultObject;
            if (resultObject instanceof Value) {
                resultObj = ((Value) resultObject).as(Object.class);
            }
            if (resultObj instanceof Map) {
                createValidationResultFromPyObject(engine, constraint, focusNode, messageHolder, bindings, resultObj);
            } else if (resultObj instanceof Boolean) {
                if (!(Boolean) resultObj) {
                    Resource result = createValidationResult(engine, constraint, focusNode);
                    if (valueNode != null) {
                        if (!SH.HasValueConstraintComponent.equals(constraint.getComponent())) { // See https://github.com/w3c/data-shapes/issues/111
                            result.addProperty(SH.value, valueNode);
                        }
                    }
                    addDefaultMessages(engine, constraint, messageHolder, constraint.getComponent(), result, bindings, null);
                }
            } else if (resultObj instanceof String) {
                Resource result = createValidationResult(engine, constraint, focusNode);
                result.addProperty(SH.resultMessage, (String) resultObj);
                if (valueNode != null) {
                    result.addProperty(SH.value, valueNode);
                }
                addDefaultMessages(engine, constraint, messageHolder, constraint.getComponent(), result, bindings, null);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void createValidationResultFromPyObject(ValidationEngine engine, Constraint constraint, RDFNode focusNode,
                                                    Resource messageHolder, QuerySolution bindings, Object ro) {
        Resource result = createValidationResult(engine, constraint, focusNode);
        if (ro instanceof Map) {
            Object value = ((Map) ro).get("value");
            if (value instanceof PyTerm) {
                Node resultValueNode = PyFactory.getNode(value);
                if (resultValueNode != null) {
                    result.addProperty(SH.value, result.getModel().asRDFNode(resultValueNode));
                }
            }
            Object message = ((Map) ro).get("message");
            if (message instanceof String) {
                result.addProperty(SH.resultMessage, (String) message);
            }
            Object path = ((Map) ro).get("path");
            if (path != null) {
                Node pathNode = PyFactory.getNode(path);
                if (pathNode != null && pathNode.isURI()) {
                    result.addProperty(SH.resultPath, result.getModel().asRDFNode(pathNode));
                }
            }
        } else if (ro instanceof String) {
            result.addProperty(SH.resultMessage, (String) ro);
        }
        if (!result.hasProperty(SH.resultMessage)) {
            addDefaultMessages(engine, constraint, messageHolder, constraint.getComponent(), result, bindings, ro instanceof Map ? (Map) ro : null);
        }
    }

    protected abstract String getLabel(Constraint constraint);
}
