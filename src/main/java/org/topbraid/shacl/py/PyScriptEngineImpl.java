package org.topbraid.shacl.py;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.graalvm.polyglot.Value;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.js.ScriptEngineUtil;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.vocabulary.SH;

import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public abstract class PyScriptEngineImpl implements ScriptEngine {

    protected final static String ARGS_FUNCTION_NAME = "get_args";

    protected final static String ARGS_FUNCTION = "def " + ARGS_FUNCTION_NAME + "(func_str): \n" +
            "\t start_idx = func_str.find('(') + 1 \n" +
            "\t end_idx = func_str.find(')', start_idx) \n" +
            "\t if start_idx != -1 and end_idx != -1: \n" +
            "\t\t params_str = func_str[start_idx:end_idx] \n" +
            "\t\t args_list = [arg.strip() for arg in params_str.split(',')] \n" +
            "\t\t return args_list \n" +
            "\t else: \n" +
            "\t\t return []";

    public static final String DASH_PY = "http://datashapes.org/py/dash.py";

    public static final String RDFQUERY_PY = "http://datashapes.org/py/rdfquery.py";

    protected Map<String, List<String>> functionParametersMap = new HashMap<>();

    protected Set<Resource> visitedLibraries = new HashSet<>();

    protected Set<String> loadedURLs = new HashSet<>();

    @Override
    public abstract void initEngine();

    @Override
    public abstract Object eval(String expr) throws ScriptException;

    @Override
    public void executeLibraries(Resource exec) throws Exception {
        for (Resource library : JenaUtil.getResourceProperties(exec, SH.pyLibrary)) {
            if (!visitedLibraries.contains(library)) {
                visitedLibraries.add(library);
                executeLibraries(library);
            }
        }
        for (Statement s : exec.listProperties(SH.pyLibraryURL).toList()) {
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
                index = functionParams.indexOf("_" + varName);
            }
            if (index >= 0) {
                RDFNode value = bindings.get(varName);
                if (value != null) {
                    params[index] = PyFactory.asPyTerm(value.asNode());
                }
            }
        }
        return invokeFunctionOrdered(functionName, params);
    }

    @Override
    public abstract Object invokeFunctionOrdered(String functionName, Object[] args) throws ScriptException, NoSuchMethodException;

    @Override
    public abstract void put(String varName, Object value);

    protected Reader createScriptReader(String url) throws Exception {
        // TODO replace dash.js and rdfquery.js with python versions
        if (DASH_PY.equals(url)) {
            return new InputStreamReader(GraalPyScriptEngine.class.getResourceAsStream("/js/dash.js"));
        } else if (RDFQUERY_PY.equals(url)) {
            return new InputStreamReader(GraalPyScriptEngine.class.getResourceAsStream("/js/rdfquery.js"));
        } else {
            return new InputStreamReader(new URL(url).openStream());
        }
    }

    private List<String> getFunctionParameters(String functionName) throws ScriptException {
        List<String> cached = functionParametersMap.get(functionName);
        if (cached != null) {
            return cached;
        }
        Object what = this.get(functionName);
        if (what == null) {
            throw new ScriptException("Cannot find Python function \"" + functionName + "\"");
        }
        try {
            String funcString = ((Value) what).getSourceLocation().getCharacters().toString();
            Object result = this.invokeFunctionOrdered(ARGS_FUNCTION_NAME, Collections.singletonList(funcString).toArray());
            List<String> results = ScriptEngineUtil.asArray(result).stream().map(Object::toString).collect(Collectors.toList());
            functionParametersMap.put(functionName, results);
            return results;
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

}
