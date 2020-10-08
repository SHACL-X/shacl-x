package org.topbraid.shacl;

import org.graalvm.polyglot.Context;
import org.junit.Test;
import org.topbraid.shacl.js.GraalScriptEngine;
import org.topbraid.shacl.js.JSScriptEngine;
import org.topbraid.shacl.js.JSScriptEngineFactory;
import org.topbraid.shacl.js.model.TermFactory;

public class TestGraalScriptEngine {

    private final static String ARGS_FUNCTION_NAME = "theGoodOldArgsFunction";

    private static final String FUNC_STRING = "function maxLength($value, $maxLength) {return ($value.lex.length <= $maxLength.lex);}";
    // Copied from https://davidwalsh.name/javascript-arguments
    private final static String ARGS_FUNCTION =
            "function " + ARGS_FUNCTION_NAME + "(funcString) {\n" +
                    "    var args = funcString.toString().match(/function\\s.*?\\(([^)]*)\\)/)[1];\n" +
                    "    return args.split(',').map(function(arg) {\n" +
                    "        return arg.replace(/\\/\\*.*\\*\\//, '').trim();\n" +
                    "    }).filter(function(arg) {\n" +
                    "        return arg;\n" +
                    "    });\n" +
                    "}";

    private static final String NAME = "function getArgs(func) {\n" +
            "  // First match everything inside the function argument parens.\n" +
            "  var args = func.toString().match(/function\\s.*?\\(([^)]*)\\)/)[1];\n" +
            " \n" +
            "  // Split the arguments string into an array comma delimited.\n" +
            "  return args.split(',').map(function(arg) {\n" +
            "    // Ensure no inline comments are parsed and trim the whitespace.\n" +
            "    return arg.replace(/\\/\\*.*\\*\\//, '').trim();\n" +
            "  }).filter(function(arg) {\n" +
            "    // Ensure no undefined values are added.\n" +
            "    return arg;\n" +
            "  });\n" +
            "}";
    private static final String USAGE = "var a = getArgs(\"function maxLength($value, $maxLength) {return ($value.lex.length <= $maxLength.lex);}\"); console.log(a);";


    @Test
    public void testEngine() {
        JSScriptEngine engine = JSScriptEngineFactory.get().createScriptEngine("Graal");

        Context context = ((GraalScriptEngine) engine).getContext();
        context.getBindings("js").putMember("TermFactory", new TermFactory());
        context.eval("js", "var a = TermFactory.literal(true, \"en\"); print('aaaaa: ' + a);");

    }

}
