package org.topbraid.shacl.testcases;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.topbraid.jenax.functions.CurrentThreadFunctionRegistry;
import org.topbraid.jenax.util.ARQFactory;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.engine.ShapesGraph;
import org.topbraid.shacl.rules.RuleEngine;
import org.topbraid.shacl.testcases.context.JSPreferredTestCaseContext;
import org.topbraid.shacl.testcases.context.PyPreferredTestCaseContext;
import org.topbraid.shacl.testcases.context.SPARQLPreferredTestCaseContext;
import org.topbraid.shacl.testcases.context.TestCaseContextFactory;
import org.topbraid.shacl.util.SHACLUtil;
import org.topbraid.shacl.vocabulary.DASH;
import org.topbraid.shacl.vocabulary.SH;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;


public class InferencingTestCaseType extends TestCaseType {

    private static final List<TestCaseContextFactory> contextFactories = new LinkedList<>();

    static {
        registerContextFactory(SPARQLPreferredTestCaseContext.getTestCaseContextFactory());
        registerContextFactory(JSPreferredTestCaseContext.getTestCaseContextFactory());
        registerContextFactory(PyPreferredTestCaseContext.getTestCaseContextFactory());
    }

    public static void registerContextFactory(TestCaseContextFactory factory) {
        contextFactories.add(factory);
    }


    public InferencingTestCaseType() {
        super(DASH.InferencingTestCase);
    }


    @Override
    protected TestCase createTestCase(Resource graph, Resource resource) {
        return new InferencingTestCase(graph, resource);
    }


    private static class InferencingTestCase extends TestCase {

        InferencingTestCase(Resource graph, Resource resource) {
            super(graph, resource);
        }


        @Override
        public void run(Model results) throws InterruptedException {
            Resource testCase = getResource();

            Runnable tearDownCTFR = CurrentThreadFunctionRegistry.register(testCase.getModel());

            Model dataModel = SHACLUtil.withDefaultValueTypeInferences(testCase.getModel());

            Dataset dataset = ARQFactory.get().getDataset(dataModel);
            URI shapesGraphURI = SHACLUtil.withShapesGraph(dataset);
            ShapesGraph shapesGraph = new ShapesGraph(dataset.getNamedModel(shapesGraphURI.toString()));

            RuleEngine ruleEngine = new RuleEngine(dataset, shapesGraphURI, shapesGraph, dataModel);
            ruleEngine.executeAll();

            tearDownCTFR.run();

            Model actualResults = ruleEngine.getInferencesModel();
            actualResults.setNsPrefix(SH.PREFIX, SH.NS);
            actualResults.setNsPrefix("rdf", RDF.getURI());
            actualResults.setNsPrefix("rdfs", RDFS.getURI());

            Model expectedModel = JenaUtil.createDefaultModel();
            List<Statement> expectedResults = getResource().listProperties(DASH.expectedResult).toList();
            boolean valid = false;
            for (Statement s : expectedResults) {
                valid = false;
                expectedModel.add(s);
                Resource expectedInferredBN = s.getObject().asResource();
                Triple expectedInferredTriple = Triple.create(expectedInferredBN.getProperty(RDF.subject).getObject().asNode(),
                        expectedInferredBN.getProperty(RDF.predicate).getObject().asNode(),
                        expectedInferredBN.getProperty(RDF.object).getObject().asNode());
                if (actualResults.getGraph().contains(expectedInferredTriple)) {
                    valid = true;
                }
            }

            if (valid) {
                createResult(results, DASH.SuccessTestCaseResult);
            } else {
                createFailure(results,
                        "Mismatching inference results. Expected " + expectedModel.size() + " triples, found " + actualResults.size());
            }
        }

    }
}
