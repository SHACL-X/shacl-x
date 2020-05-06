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
package org.topbraid.shacl.js;

import org.topbraid.jenax.util.ExceptionUtil;
import org.topbraid.shacl.js.model.TermFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Default implementation of JSScriptEngine, based on Nashorn.
 *
 * @author Holger Knublauch
 */
public class NashornScriptEngine extends JSScriptEngineImpl {

    private ScriptEngine engine;

    public NashornScriptEngine() {
        initEngine();
        engine.put("TermFactory", new TermFactory());
        try {
            engine.eval(ARGS_FUNCTION);
        } catch (ScriptException ex) {
            ExceptionUtil.throwUnchecked(ex);
        }
    }

    @Override
    public void initEngine() {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        if (this.engine == null) {
            this.engine = new ScriptEngineManager(null).getEngineByName("nashorn");
        }
        if (this.engine == null) {
            throw new RuntimeException("Oracle Nashorn not found in the current context");
        }
    }

    @Override
    public Object eval(String expr) throws ScriptException {
        return engine.eval(expr);
    }

    @Override
    public Object get(String varName) {
        return engine.get(varName);
    }

    public final ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public Object invokeFunctionOrdered(String functionName, Object[] params)
            throws ScriptException, NoSuchMethodException {
        return ((Invocable) engine).invokeFunction(functionName, params);
    }

    @Override
    public void put(String varName, Object value) {
        engine.put(varName, value);
    }

}
