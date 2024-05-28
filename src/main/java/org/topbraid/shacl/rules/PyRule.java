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
package org.topbraid.shacl.rules;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.vocabulary.RDFS;
import org.topbraid.jenax.progress.ProgressMonitor;
import org.topbraid.jenax.util.ExceptionUtil;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.engine.Shape;
import org.topbraid.shacl.js.SHACLScriptEngineManager;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.js.ScriptEngineUtil;
import org.topbraid.shacl.model.SHPyExecutable;
import org.topbraid.shacl.py.PyGraph;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.validation.SHACLException;
import org.topbraid.shacl.vocabulary.SH;

import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

class PyRule extends AbstractRule {

    PyRule(Resource rule) {
        super(rule);
    }

    @Override
    public void execute(RuleEngine ruleEngine, List<RDFNode> focusNodes, Shape shape) {

        Resource rule = getResource();
        String functionName = JenaUtil.getStringProperty(rule, SH.pyFunctionName);
        if (functionName == null) {
            throw new IllegalArgumentException("Missing Python function name at rule " + rule);
        }

        ProgressMonitor monitor = ruleEngine.getProgressMonitor();
        for (RDFNode focusNode : focusNodes) {

            if (monitor != null && monitor.isCanceled()) {
                return;
            }

            boolean nested = SHACLScriptEngineManager.begin();
            ScriptEngine engine = SHACLScriptEngineManager.getCurrentPyEngine();

            SHPyExecutable as = rule.as(SHPyExecutable.class);
            PyGraph dataPyGraph = new PyGraph(ruleEngine.getDataset().getDefaultModel().getGraph(), engine);
            PyGraph shapesPyGraph = new PyGraph(ruleEngine.getDataset().getDefaultModel().getGraph(), engine);
            try {
                engine.executeLibraries(as);
                engine.put(SH.Py_DATA_VAR, dataPyGraph);
                engine.put(SH.Py_SHAPES_VAR, shapesPyGraph);

                QuerySolutionMap bindings = new QuerySolutionMap();
                bindings.add(SH.thisVar.getVarName(), focusNode);
                Object result = engine.invokeFunction(functionName, bindings);
                if (ScriptEngineUtil.isArray(result)) {
                    for (Object tripleO : ScriptEngineUtil.asArray(result)) {
                        Node subject;
                        Node predicate;
                        Node object;
                        if (ScriptEngineUtil.isArray(tripleO)) {
                            List<Object> nodes = ScriptEngineUtil.asArray(tripleO);
                            subject = PyFactory.getNode(nodes.get(0));
                            predicate = PyFactory.getNode(nodes.get(1));
                            object = PyFactory.getNode(nodes.get(2));
                        } else if (tripleO instanceof @SuppressWarnings("rawtypes")Map triple) {
                            subject = PyFactory.getNode(triple.get("subject"));
                            predicate = PyFactory.getNode(triple.get("predicate"));
                            object = PyFactory.getNode(triple.get("object"));
                        } else if (tripleO instanceof List triple) {
                            subject = PyFactory.getNode(triple.get(0));
                            predicate = PyFactory.getNode(triple.get(1));
                            object = PyFactory.getNode(triple.get(2));
                        } else {
                            throw new SHACLException("Array members produced by rule must be either Arrays/Lists with three nodes, or Python objects with subject, predicate and object");
                        }
                        ruleEngine.infer(Triple.create(subject, predicate, object), this, shape);
                    }
                }
            } catch (ScriptException ex) {
                ExceptionUtil.throwUnchecked(ex);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ExprEvalException(ex);
            } finally {
                dataPyGraph.close();
                SHACLScriptEngineManager.end(nested);
            }
        }
    }

    @Override
    public String toString() {
        String label = JenaUtil.getStringProperty(getResource(), RDFS.label);
        if (label == null) {
            Statement s = getResource().getProperty(SH.jsFunctionName);
            if (s != null && s.getObject().isLiteral()) {
                label = s.getString();
            } else {
                label = "(Missing Python function name)";
            }
        }
        return getLabelStart("Python") + label;
    }

}
