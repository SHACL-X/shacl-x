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

import org.topbraid.shacl.py.PyScriptEngineFactory;

/**
 * A singleton that uses a ThreadLocal to manage the life cycle of a JSScriptEngine
 * that can be shared for all JavaScript evaluated as part of a SHACL validation.
 * This prevents cases in which new objects and their JS libraries would need to be
 * created over and over again.
 * <p>
 * It is the responsibility of the caller to make sure that, once the validation has
 * completed, the engine is uninstalled.
 *
 * @author Holger Knublauch
 */
public class SHACLScriptEngineManager {

    private static ThreadLocal<Boolean> actives = new ThreadLocal<>();

    private static ThreadLocal<ScriptEngine> jsEngine = new ThreadLocal<>();

    private static ThreadLocal<ScriptEngine> pyEngine = new ThreadLocal<>();


    public static boolean begin() {
        if (actives.get() != null) {
            return actives.get();
        } else {
            actives.set(true);
            return false; // Signal that we are not nested inside of another begin/end block
        }
    }


    public static ScriptEngine getCurrentJSEngine() {
        ScriptEngine engine = jsEngine.get();
        if (engine == null) {
            // TODO decide how to manage the Engine creation
            engine = JSScriptEngineFactory.get().createScriptEngine("Graal");
            jsEngine.set(engine);
        }
        return engine;
    }

    public static ScriptEngine getCurrentPyEngine() {
        ScriptEngine engine = pyEngine.get();
        if (engine == null) {
            // TODO decide how to manage the Engine creation
            engine = PyScriptEngineFactory.get().createScriptEngine("Graal");
            pyEngine.set(engine);
        }
        return engine;
    }


    public static void end(boolean nested) {
        if (!nested) {
            jsEngine.remove();
            pyEngine.remove();
            actives.remove();
        }
    }
}
