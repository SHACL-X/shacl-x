package org.topbraid.shacl.js;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.js.model.JSFactory;
import org.topbraid.shacl.js.model.TermFactory;
import org.topbraid.shacl.vocabulary.SH;

import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GraalScriptEngine implements JSScriptEngine {

    private final static String ARGS_FUNCTION_NAME = "theGoodOldArgsFunction";

    // Copied from https://davidwalsh.name/javascript-arguments
    private final static String ARGS_FUNCTION =
            "function " + ARGS_FUNCTION_NAME + "(funcString) {\n" +
                    "    var args = funcString.match(/function\\s.*?\\(([^)]*)\\)/)[1];\n" +
                    "    return args.split(',').map(function(arg) {\n" +
                    "        return arg.replace(/\\/\\*.*\\*\\//, '').trim();\n" +
                    "    }).filter(function(arg) {\n" +
                    "        return arg;\n" +
                    "    });\n" +
                    "}";

    public static final String DASH_JS = "http://datashapes.org/js/dash.js";

    public static final String RDFQUERY_JS = "http://datashapes.org/js/rdfquery.js";

    private Context context;

    private Map<String, List<String>> functionParametersMap = new HashMap<>();

    // Remembers which sh:libraries executables were already handled so that they are
    // not installed twice
    private Set<Resource> visitedLibraries = new HashSet<>();

    private Set<String> loadedURLs = new HashSet<>();

    public GraalScriptEngine() {
        context = findGraal();
        context.getBindings("js").putMember("TermFactory", new TermFactory());
        context.eval("js", ARGS_FUNCTION);
    }

    private Context findGraal() {
        Context graal = Context.newBuilder("js")
                //.allowHostClassLookup(s -> true)
                .allowHostAccess(HostAccess.ALL)
                .allowExperimentalOptions(true)

                //.allowNativeAccess(true)
                //.allowCreateThread(true)
                //.allowIO(true)
                //.allowHostClassLoading(true)
                .allowAllAccess(true)
                //.option("js.ecmascript-version", "2020").build();
                .option("js.nashorn-compat", "true").build();

        if (graal == null) {
            throw new RuntimeException("GraalVM not found in the current context");
        }
        return graal;
    }

    @Override
    public Object eval(String expr) {
        return context.eval("js", expr);
    }

    @Override
    public void executeLibraries(Resource e) throws Exception {
        for (Resource library : JenaUtil.getResourceProperties(e, SH.jsLibrary)) {
            if (!visitedLibraries.contains(library)) {
                visitedLibraries.add(library);
                executeLibraries(library);
            }
        }
        for (Statement s : e.listProperties(SH.jsLibraryURL).toList()) {
            if (s.getObject().isLiteral()) {
                String url = s.getString();
                executeScriptFromURL(url);
            }
        }
    }

    @Override
    public final void executeScriptFromURL(String url) throws Exception {
        if (!loadedURLs.contains(url)) {
            loadedURLs.add(url);
            try (Reader reader = createScriptReader(url)) {
                String ts = IOUtils.toString(reader);
                context.eval("js", ts);
            }
        }
    }

    protected Reader createScriptReader(String url) throws Exception {
        if (DASH_JS.equals(url)) {
            return new InputStreamReader(GraalScriptEngine.class.getResourceAsStream("/js/dash.js"));
        } else if (RDFQUERY_JS.equals(url)) {
            return new InputStreamReader(GraalScriptEngine.class.getResourceAsStream("/js/rdfquery.js"));
        } else {
            return new InputStreamReader(new URL(url).openStream());
        }
    }

    @Override
    public Object get(String varName) {
        return context.getBindings("js").getMember(varName);
    }


    public final Context getContext() {
        return context;
    }


    private List<String> getFunctionParameters(String functionName) throws ScriptException {
        List<String> cached = functionParametersMap.get(functionName);
        if (cached != null) {
            return cached;
        }
        Value what = context.getBindings("js").getMember(ARGS_FUNCTION_NAME);
        if (what == null) {
            throw new ScriptException("Cannot find JavaScript function \"" + functionName + "\"");
        }
        try {
            String funcString = context.getBindings("js").getMember(functionName).toString();
            Value result = what.execute(funcString);
            List<String> results = GraalUtil.asArray(result).stream().map(Object::toString).collect(Collectors.toList());
            functionParametersMap.put(functionName, results);
            return results;
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public Object invokeFunction(String functionName, QuerySolution bindings) throws javax.script.ScriptException {
        List<String> functionParams = getFunctionParameters(functionName);
        Object[] params = new Object[functionParams.size()];
        Iterator<String> varNames = bindings.varNames();
        while (varNames.hasNext()) {
            String varName = varNames.next();
            int index = functionParams.indexOf(varName);
            if (index < 0) {
                index = functionParams.indexOf("$" + varName);
            }
            if (index >= 0) {
                RDFNode value = bindings.get(varName);
                if (value != null) {
                    params[index] = JSFactory.asJSTerm(value.asNode());
                }
            }
        }
        return invokeFunctionOrdered(functionName, params);
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
