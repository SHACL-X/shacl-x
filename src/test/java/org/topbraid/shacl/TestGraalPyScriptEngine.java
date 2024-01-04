package org.topbraid.shacl;

import org.graalvm.polyglot.Value;
import org.junit.Test;
import org.topbraid.shacl.py.PyScriptEngine;
import org.topbraid.shacl.py.PyScriptEngineFactory;

import javax.script.ScriptException;
import java.util.Objects;

public class TestGraalPyScriptEngine {

    @Test
    public void testEngine() throws ScriptException {
        PyScriptEngine engine = PyScriptEngineFactory.get().createScriptEngine("Graal");
        Value value = (Value) engine.eval("[1,2,42,4]");
        int element = value.getArrayElement(2).asInt();
        assert Objects.equals(element, 42);
        // TODO add test with TermFactory
    }

}
