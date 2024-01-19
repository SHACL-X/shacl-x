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
package org.topbraid.shacl.arq;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.graalvm.polyglot.Value;
import org.topbraid.jenax.util.ExceptionUtil;
import org.topbraid.jenax.util.JenaDatatypes;
import org.topbraid.shacl.js.SHACLScriptEngineManager;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.model.SHPyExecutable;
import org.topbraid.shacl.model.SHPyFunction;
import org.topbraid.shacl.py.PyGraph;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.vocabulary.SH;

import javax.script.ScriptException;

public class SHACLPyARQFunction extends SHACLARQFunction {

    private String functionName;


    public SHACLPyARQFunction(SHPyFunction shaclFunction) {
        super(shaclFunction);
        this.functionName = shaclFunction.getFunctionName();
        addParameters(shaclFunction);
    }


    @Override
    public NodeValue executeBody(Dataset dataset, Model dataModel, QuerySolution bindings) {

        if (functionName == null) {
            throw new IllegalArgumentException("Missing Python function name of " + getSHACLFunction().getURI());
        }

        boolean nested = SHACLScriptEngineManager.begin();
        ScriptEngine engine = SHACLScriptEngineManager.getCurrentPyEngine();

        SHPyExecutable as = getSHACLFunction().as(SHPyExecutable.class);
        PyGraph dataPyGraph = new PyGraph(dataModel.getGraph(), engine);
        try {
            engine.executeLibraries(as);
            engine.put(SH.Py_DATA_VAR, dataPyGraph);

            Object result = engine.invokeFunction(functionName, bindings);
            if (result != null) {
                if (result instanceof Value) {
                    result = ((Value) result).as(Object.class);
                }
                Node node = PyFactory.getNode(result);
                if (node != null) {
                    return NodeValue.makeNode(node);
                } else if (result instanceof String) {
                    return NodeValue.makeNode(NodeFactory.createLiteral((String) result));
                } else if (result instanceof Long) {
                    return NodeValue.makeNode(JenaDatatypes.createInteger(((Long) result).intValue()).asNode());
                } else if (result instanceof Integer) {
                    return NodeValue.makeNode(JenaDatatypes.createInteger((Integer) result).asNode());
                } else if (result instanceof Double) {
                    return NodeValue.makeDecimal((Double) result);
                } else if (result instanceof Boolean) {
                    return NodeValue.booleanReturn((Boolean) result);
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
        throw new ExprEvalException();
    }


    @Override
    protected String getQueryString() {
        return ((SHPyFunction) getSHACLFunction()).getFunctionName();
    }
}
