package org.topbraid.shacl;

import java.io.InputStream;
import java.net.URI;

import junit.framework.TestCase;

import org.junit.Assert;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.vocabulary.MF;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.shacl.vocabulary.SHT;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaDatatypes;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileUtils;

abstract class AbstractSHACLTestClass extends TestCase {
	
	private Model shaclModel;

	protected Resource testResource;
	
	
	public AbstractSHACLTestClass(Resource testResource) {
		super("testRun");
		this.testResource = testResource;
		SHACLFunctions.registerFunctions(getSHACLModel());
	}


	protected void compareResults(Model results) {
		Statement resultS = testResource.getProperty(MF.result);
		if(resultS == null || JenaDatatypes.TRUE.equals(resultS.getObject())) {
			Assert.assertTrue("Expected no validation results, but found: " + results.size() + " triples", results.isEmpty());
		}
		else {
			results.removeAll(null, SH.message, (RDFNode)null);
			results.removeAll(null, SH.source, (RDFNode)null);
			Model expected = JenaUtil.createDefaultModel();
			for(Statement s : testResource.listProperties(MF.result).toList()) {
				expected.add(s.getResource().listProperties());
			}
			if(!expected.getGraph().isIsomorphicWith(results.getGraph())) {
				fail("Mismatching validation results. Expected " + expected.size() + " triples, found " + results.size());
			}
		}
	}

	
	protected Dataset createDataset() throws Exception {
		Dataset result = ARQFactory.get().getDataset(getDataModel());
		result.addNamedModel(getShapesGraphURI().getURI(), getShapesModel());
		return result;
	}
	
	
	protected Resource getAction() {
		return testResource.getPropertyResourceValue(MF.action);
	}
	
	
	protected Model getDataModel() throws Exception {
		return getModelFromAction(SHT.data, SHT.data_format);
	}
	
	
	protected Model getSHACLModel() {
		if(shaclModel == null) {
			shaclModel = JenaUtil.createDefaultModel();
			InputStream is = getClass().getResourceAsStream("/etc/shacl.shacl.ttl");
			shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
		}
		return shaclModel;
	}
	
	
	protected Resource getShapesGraphURI() {
		return getAction().getPropertyResourceValue(SHT.schema);
	}
	
	
	protected Model getShapesModel() throws Exception {
		Model model = getModelFromAction(SHT.schema, SHT.schema_format);
		MultiUnion multiUnion = new MultiUnion(new Graph[] {
				model.getGraph(),
				getSHACLModel().getGraph()
		});
		multiUnion.setBaseGraph(model.getGraph());
		return ModelFactory.createModelForGraph(multiUnion);
	}
	
	
	private Model getModelFromAction(Property property, Property formatProperty) throws Exception {
		Resource schema = getAction().getPropertyResourceValue(property);
		Resource schemaFormat = getAction().getPropertyResourceValue(formatProperty);
		if(schemaFormat == null) {
			if(schema.getURI().toLowerCase().endsWith(".ttl")) {
				schemaFormat = SHT.TURTLE;
			}
			else if(schema.getURI().toLowerCase().endsWith(".shc")) {
				schemaFormat = SHT.SHACLC;
			}
			else {
				throw new IllegalArgumentException("Cannot determine file format");
			}
		}
		if(SHT.SHACLC.equals(schemaFormat)) {
			throw new UnsupportedTestException();
		}
		
		Model model = JenaUtil.createDefaultModel();
		model.read(new URI(schema.toString()).toURL().openStream(), schema.toString(), FileUtils.langTurtle);
		return model;
	}
	
	
	public boolean isSupported() {
		try {
			getDataModel();
			getShapesModel();
		}
		catch(UnsupportedTestException ex) {
			return false;
		}
		catch(Exception ex) {
			// Ignore for now -> let it bubble up
		}
		return true;
	}
}
