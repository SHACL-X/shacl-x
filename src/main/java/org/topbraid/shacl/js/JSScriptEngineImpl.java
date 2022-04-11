package org.topbraid.shacl.js;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.js.model.JSFactory;
import org.topbraid.shacl.vocabulary.SH;

import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public abstract class JSScriptEngineImpl implements JSScriptEngine {

    protected final static String ARGS_FUNCTION_NAME = "theGoodOldArgsFunction";

    // Copied from https://davidwalsh.name/javascript-arguments
    protected final static String ARGS_FUNCTION =
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

    protected Map<String, List<String>> functionParametersMap = new HashMap<>();

    // Remembers which sh:libraries executables were already handled so that they are
    // not installed twice
    protected Set<Resource> visitedLibraries = new HashSet<>();

    protected Set<String> loadedURLs = new HashSet<>();

    @Override
    public abstract void initEngine();

    @Override
    public abstract Object eval(String expr) throws ScriptException;

    @Override
    public void executeLibraries(Resource exec) throws Exception {
        for (Resource library : JenaUtil.getResourceProperties(exec, SH.jsLibrary)) {
            if (!visitedLibraries.contains(library)) {
                visitedLibraries.add(library);
                executeLibraries(library);
            }
        }
        for (Statement s : exec.listProperties(SH.jsLibraryURL).toList()) {
            if (s.getObject().isLiteral()) {
                String url = s.getString();
                executeScriptFromURL(url);
            }
        }
    }

    @Override
    public void executeScriptFromURL(String url) throws Exception {
        if (!loadedURLs.contains(url)) {
            loadedURLs.add(url);
            try (Reader reader = createScriptReader(url)) {
                String ts = IOUtils.toString(reader);
                this.eval(ts);
            }
        }
    }

    protected Reader createScriptReader(String url) throws Exception {
        if (DASH_JS.equals(url)) {
            return new InputStreamReader(GraalJSScriptEngine.class.getResourceAsStream("/js/dash.js"));
        } else if (RDFQUERY_JS.equals(url)) {
            return new InputStreamReader(GraalJSScriptEngine.class.getResourceAsStream("/js/rdfquery.js"));
        } else {
            return new InputStreamReader(new URL(url).openStream());
        }
    }

    @Override
    public abstract Object get(String varName);

    @Override
    public Object invokeFunction(String functionName, QuerySolution bindings) throws ScriptException, NoSuchMethodException {
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
    public abstract Object invokeFunctionOrdered(String functionName, Object[] args) throws ScriptException, NoSuchMethodException;

    @Override
    public abstract void put(String varName, Object value);

    private List<String> getFunctionParameters(String functionName) throws ScriptException {
        List<String> cached = functionParametersMap.get(functionName);
        if (cached != null) {
            return cached;
        }
        Object what = this.get(functionName);
        if (what == null) {
            throw new ScriptException("Cannot find JavaScript function \"" + functionName + "\"");
        }
        try {
            String funcString = what.toString();
            Object result = this.invokeFunctionOrdered(ARGS_FUNCTION_NAME, Collections.singletonList(funcString).toArray());
            List<String> results = ScriptEngineUtil.asArray(result).stream().map(Object::toString).collect(Collectors.toList());
            functionParametersMap.put(functionName, results);
            return results;
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

}
