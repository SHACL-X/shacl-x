package org.topbraid.shacl.py;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Resource;
import org.graalvm.polyglot.Context;

import javax.script.ScriptException;

/**
 * @author Ashley Caselli
 */
public class GraalPyScriptEngine extends PyScriptEngineImpl {

    private Context context;

    public GraalPyScriptEngine() {
        initEngine();
        // TODO implement
    }

    @Override
    public void initEngine() {
        this.context = Context.newBuilder()
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        if (this.context == null) {
            throw new RuntimeException("GraalVM not found in the current context");
        }
    }

    @Override
    public Object eval(String expr) throws ScriptException {
        return context.eval("python", expr);
    }

    @Override
    public void executeLibraries(Resource exec) throws Exception {
        // TODO implement
    }

    @Override
    public void executeScriptFromURL(String url) throws Exception {
        // TODO implement
    }

    @Override
    public Object get(String varName) {
        return context.getBindings("python").getMember(varName);
    }

    @Override
    public Object invokeFunction(String functionName, QuerySolution bindings) throws ScriptException, NoSuchMethodException {
        return null;
        // TODO implement
    }

    public final Context getContext() {
        return context;
    }

    @Override
    public Object invokeFunctionOrdered(String functionName, Object[] args) throws ScriptException, NoSuchMethodException {
        return context.getBindings("python").getMember(functionName).execute(args);
    }

    @Override
    public void put(String varName, Object value) {
        context.getBindings("python").putMember(varName, value);
    }

}
