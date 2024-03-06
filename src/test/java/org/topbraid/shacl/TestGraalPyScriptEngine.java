package org.topbraid.shacl;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Test;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.py.GraalPyScriptEngine;
import org.topbraid.shacl.py.PyScriptEngineFactory;
import org.topbraid.shacl.py.model.PyLiteral;
import org.topbraid.shacl.py.model.PyTermFactory;

import java.util.Objects;

public class TestGraalPyScriptEngine {

    private static final String VALUE = "testEngine";

    private static final String LANG = "en";

    @Test
    public void testEngine() {
        ScriptEngine engine = PyScriptEngineFactory.get().createScriptEngine("Graal");
        Context context = ((GraalPyScriptEngine) engine).getContext();
        context.getPolyglotBindings().putMember("PyTermFactory", new PyTermFactory());
        Value value = context.eval("python",
                "import polyglot \n" +
                        "py_tf = polyglot.import_value('PyTermFactory')\n" +
                        "py_tf.literal(\"" + VALUE + "\", \"" + LANG + "\")");
        System.out.println(value.toString());
        PyLiteral tf = new PyTermFactory().literal(VALUE, LANG);
        assert Objects.equals(value.toString(), tf.toString());
        context.close();
    }

}
