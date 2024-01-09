package org.topbraid.shacl;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;
import org.topbraid.shacl.js.GraalJSScriptEngine;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.js.JSScriptEngineFactory;
import org.topbraid.shacl.js.model.JSLiteral;
import org.topbraid.shacl.js.model.TermFactory;

import java.util.Objects;

public class TestGraalJSScriptEngine {

    @Test
    public void testEngine() {
        ScriptEngine engine = JSScriptEngineFactory.get().createScriptEngine("Graal");
        Context context = ((GraalJSScriptEngine) engine).getContext();
        context.getBindings("js").putMember("TermFactory", new TermFactory());
        Value a = context.eval("js", "TermFactory.literal(\"testEngine\", \"en\")");
        JSLiteral tf = new TermFactory().literal("testEngine", "en");
        System.out.println(a.toString());
        assert Objects.equals(a.toString(), tf.toString());
        context.close();
    }

}
