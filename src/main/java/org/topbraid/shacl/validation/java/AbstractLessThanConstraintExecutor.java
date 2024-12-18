package org.topbraid.shacl.validation.java;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.expr.ExprNotComparableException;
import org.apache.jena.sparql.expr.NodeValue;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.validation.AbstractNativeConstraintExecutor;
import org.topbraid.shacl.validation.ValidationEngine;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

abstract class AbstractLessThanConstraintExecutor extends AbstractNativeConstraintExecutor {

    private String operator;

    private Predicate<Integer> test;


    AbstractLessThanConstraintExecutor(Predicate<Integer> test, String operator) {
        this.test = test;
        this.operator = operator;
    }

    @Override
    public void executeConstraint(Constraint constraint, ValidationEngine engine, Collection<RDFNode> focusNodes) {
        long startTime = System.currentTimeMillis();
        Property predicate = constraint.getParameterValue().as(Property.class);
        for (RDFNode focusNode : focusNodes) {
            if (focusNode instanceof Resource) {
                Collection<RDFNode> valueNodes = engine.getValueNodes(constraint, focusNode);
                Set<RDFNode> otherNodes = ((Resource) focusNode).listProperties(predicate).mapWith(Statement::getObject).toSet();
                for (RDFNode valueNode : valueNodes) {
                    NodeValue v = NodeValue.makeNode(valueNode.asNode());
                    for (RDFNode otherNode : otherNodes) {
                        NodeValue o = NodeValue.makeNode(otherNode.asNode());
                        try {
                            int cmp = NodeValue.compare(v, o);
                            if (test.test(cmp)) {
                                engine.createValidationResult(constraint, focusNode, valueNode, () -> "Value is not " + operator + " " + engine.getLabelFunction().apply(otherNode));
                            }
                        } catch (ExprNotComparableException ex) {
                            engine.createValidationResult(constraint, focusNode, valueNode, () -> "Cannot compare with " + engine.getLabelFunction().apply(otherNode));
                        }
                    }
                }
            }
            engine.checkCanceled();
        }
        addStatistics(constraint, startTime);
    }
}
