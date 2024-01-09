package org.topbraid.shacl.py;

import org.topbraid.shacl.js.ScriptEngine;

public class PyScriptEngineFactory {

    private static PyScriptEngineFactory singleton = new PyScriptEngineFactory();

    public static PyScriptEngineFactory get() {
        return singleton;
    }

    public static void set(PyScriptEngineFactory value) {
        PyScriptEngineFactory.singleton = value;
    }

    public ScriptEngine createScriptEngine(final String engineName) {
        if (engineName.equals("Graal")) {
            return new GraalPyScriptEngine();
        }
        return null;
    }

}
