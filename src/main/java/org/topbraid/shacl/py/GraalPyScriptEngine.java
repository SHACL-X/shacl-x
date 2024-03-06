package org.topbraid.shacl.py;

import org.graalvm.polyglot.Context;
import org.topbraid.shacl.py.model.PyTermFactory;

import javax.script.ScriptException;

/**
 * @author Ashley Caselli
 */
public class GraalPyScriptEngine extends PyScriptEngineImpl {

    private Context context;

    public GraalPyScriptEngine() {
        initEngine();
        context.getPolyglotBindings().putMember("PyTermFactory", new PyTermFactory());
        context.eval("python",
                """
                        import polyglot\s
                        py_tf = polyglot.import_value('PyTermFactory')
                        """);
        context.eval("python", ARGS_FUNCTION);
    }

    @Override
    public void initEngine() {
        this.context = Context.newBuilder()
                .allowAllAccess(true)
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
    public Object get(String varName) {
        return context.getBindings("python").getMember(varName);
    }

    public final Context getContext() {
        return context;
    }

    @Override
    public Object invokeFunctionOrdered(String functionName, Object[] args) {
        return context.getBindings("python").getMember(functionName).execute(args);
    }

    @Override
    public void put(String varName, Object value) {
        context.getBindings("python").putMember(varName, value);
    }

}
