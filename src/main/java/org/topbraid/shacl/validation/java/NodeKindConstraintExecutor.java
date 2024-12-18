package org.topbraid.shacl.validation.java;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.validation.AbstractNativeConstraintExecutor;
import org.topbraid.shacl.validation.ValidationEngine;
import org.topbraid.shacl.vocabulary.SH;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Native implementation of sh:NodeKindConstraintComponent.
 *
 * @author Holger Knublauch
 */
class NodeKindConstraintExecutor extends AbstractNativeConstraintExecutor {

    private static final Map<RDFNode, Predicate<RDFNode>> checkers = new HashMap<>();

    static {
        checkers.put(SH.BlankNode, RDFNode::isAnon);
        checkers.put(SH.BlankNodeOrIRI, (node) -> node.isAnon() || node.isURIResource());
        checkers.put(SH.BlankNodeOrLiteral, (node) -> node.isAnon() || node.isLiteral());
        checkers.put(SH.IRI, RDFNode::isURIResource);
        checkers.put(SH.IRIOrLiteral, (node) -> node.isURIResource() || node.isLiteral());
        checkers.put(SH.Literal, RDFNode::isLiteral);
    }


    @Override
    public void executeConstraint(Constraint constraint, ValidationEngine engine, Collection<RDFNode> focusNodes) {
        long startTime = System.currentTimeMillis();
        RDFNode nodeKind = constraint.getParameterValue();
        Predicate<RDFNode> checker = checkers.get(nodeKind);
        if (checker == null) {
            throw new IllegalArgumentException("Unsupported sh:nodeKind " + nodeKind);
        }
        String message = "Value does not have node kind " + ((Resource) nodeKind).getLocalName();
        for (RDFNode focusNode : focusNodes) {
            for (RDFNode valueNode : engine.getValueNodes(constraint, focusNode)) {
                if (!checker.test(valueNode)) {
                    engine.createValidationResult(constraint, focusNode, valueNode, () -> message);
                }
            }
            engine.checkCanceled();
        }
        addStatistics(constraint, startTime);
    }
}
