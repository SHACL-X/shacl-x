package org.topbraid.shacl.js;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.topbraid.shacl.js.model.TermFactory;

/**
 * @author Ashley Caselli
 */
public class GraalJSScriptEngine extends JSScriptEngineImpl {

    private Context context;

    public GraalJSScriptEngine() {
        initEngine();
        context.getBindings("js").putMember("TermFactory", new TermFactory());
        context.eval("js", ARGS_FUNCTION);
    }

    @Override
    public void initEngine() {
        this.context = Context.newBuilder("js")
                //.allowHostClassLookup(s -> true)
                .allowHostAccess(HostAccess.ALL)
                .allowExperimentalOptions(true)
                //.allowNativeAccess(true)
                //.allowCreateThread(true)
                //.allowIO(true)
                //.allowHostClassLoading(true)
                .allowAllAccess(true)
                .option("js.ecmascript-version", "2021")
                .option("js.nashorn-compat", "true")
                .build();
        if (this.context == null) {
            throw new RuntimeException("GraalVM not found in the current context");
        }
    }

    @Override
    public Object eval(String expr) {
        return context.eval("js", expr);
    }


    @Override
    public Object get(String varName) {
        return context.getBindings("js").getMember(varName);
    }

    public final Context getContext() {
        return context;
    }

    @Override
    public Value invokeFunctionOrdered(String functionName, Object[] params) {
        return context.getBindings("js").getMember(functionName).execute(params);
    }

    @Override
    public void put(String varName, Object value) {
        context.getBindings("js").putMember(varName, value);
    }

}
