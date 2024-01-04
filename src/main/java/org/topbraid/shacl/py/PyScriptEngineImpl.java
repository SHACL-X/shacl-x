package org.topbraid.shacl.py;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Resource;

import javax.script.ScriptException;

public abstract class PyScriptEngineImpl implements PyScriptEngine {
    @Override
    public abstract void initEngine();

    @Override
    public abstract Object eval(String expr) throws ScriptException;

    @Override
    public void executeLibraries(Resource exec) throws Exception {
        // TODO implement
    }

    @Override
    public void executeScriptFromURL(String url) throws Exception {
        // TODO implement
    }

    @Override
    public abstract Object get(String varName);

    @Override
    public Object invokeFunction(String functionName, QuerySolution bindings) throws ScriptException, NoSuchMethodException {
        return null;
        // TODO implement
    }

    @Override
    public abstract Object invokeFunctionOrdered(String functionName, Object[] args) throws ScriptException, NoSuchMethodException;

    @Override
    public abstract void put(String varName, Object value);
}
