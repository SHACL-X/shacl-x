package org.topbraid.shacl.validation.py;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.jenax.util.ExceptionUtil;
import org.topbraid.shacl.js.SHACLScriptEngineManager;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.js.ScriptEngineUtil;
import org.topbraid.shacl.model.SHParameterizableTarget;
import org.topbraid.shacl.model.SHPyExecutable;
import org.topbraid.shacl.py.PyGraph;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.targets.Target;
import org.topbraid.shacl.vocabulary.SH;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ashley Caselli
 */
public class PyTarget implements Target {

    private final SHPyExecutable as;

    private SHParameterizableTarget parameterizableTarget;

    public PyTarget(Resource executable, SHParameterizableTarget parameterizableTarget) {
        if (parameterizableTarget != null) {
            as = parameterizableTarget.getParameterizable().as(SHPyExecutable.class);
        } else {
            as = executable.as(SHPyExecutable.class);
        }
    }

    @Override
    public void addTargetNodes(Dataset dataset, Collection<RDFNode> results) {
        boolean nested = SHACLScriptEngineManager.begin();
        ScriptEngine engine = SHACLScriptEngineManager.getCurrentPyEngine();

        Model model = dataset.getDefaultModel();
        PyGraph dataPyGraph = new PyGraph(model.getGraph(), engine);
        try {
            engine.executeLibraries(as);
            engine.put(SH.Py_DATA_VAR, dataPyGraph);

            QuerySolutionMap bindings = new QuerySolutionMap();
            if (parameterizableTarget != null) {
                parameterizableTarget.addBindings(bindings);
            }

            Object result = engine.invokeFunction(as.getFunctionName(), bindings);
            if (ScriptEngineUtil.isArray(result)) {
                for (Object obj : ScriptEngineUtil.asArray(result)) {
                    Node node = PyFactory.getNode(obj);
                    results.add(model.asRDFNode(node));
                }
            }
        } catch (Exception ex) {
            ExceptionUtil.throwUnchecked(ex);
        } finally {
            dataPyGraph.close();
            SHACLScriptEngineManager.end(nested);
        }
    }

    @Override
    public boolean contains(Dataset dataset, RDFNode node) {
        Set<RDFNode> set = new HashSet<>();
        addTargetNodes(dataset, set);
        return set.contains(node);
    }

}
