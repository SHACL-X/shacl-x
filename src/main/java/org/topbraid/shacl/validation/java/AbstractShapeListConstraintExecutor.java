package org.topbraid.shacl.validation.java;

import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.validation.AbstractNativeConstraintExecutor;

import java.util.List;

abstract class AbstractShapeListConstraintExecutor extends AbstractNativeConstraintExecutor {

    protected List<Resource> shapes;


    AbstractShapeListConstraintExecutor(Constraint constraint) {
        RDFList list = constraint.getParameterValue().as(RDFList.class);
        ExtendedIterator<RDFNode> sit = list.iterator();
        shapes = sit.mapWith(RDFNode::asResource).toList();
    }
}
