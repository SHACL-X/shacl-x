package org.topbraid.shacl.validation.java;

import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.validation.ValidationEngine;
import org.topbraid.shacl.vocabulary.SH;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class OrConstraintExecutor extends AbstractShapeListConstraintExecutor {

    // An optimization if the sh:or list consists only of sh:datatype constraints.
    private Set<String> datatypeURIs;


    OrConstraintExecutor(Constraint constraint) {
        super(constraint);
        if (hasOnlyDatatypeConstraints()) {
            datatypeURIs = new HashSet<>();
            for (Resource shape : shapes) {
                Resource datatype = shape.getPropertyResourceValue(SH.datatype);
                datatypeURIs.add(datatype.getURI());
            }
        }
    }


    @Override
    public void executeConstraint(Constraint constraint, ValidationEngine engine, Collection<RDFNode> focusNodes) {

        long startTime = System.currentTimeMillis();

        if (datatypeURIs != null) {
            for (RDFNode focusNode : focusNodes) {
                for (RDFNode valueNode : engine.getValueNodes(constraint, focusNode)) {
                    if (!valueNode.isLiteral() || !datatypeURIs.contains(valueNode.asNode().getLiteralDatatypeURI()) || !valueNode.asNode().getLiteralDatatype().isValid(valueNode.asNode().getLiteralLexicalForm())) {
                        engine.createValidationResult(constraint, focusNode, valueNode, () -> "Value matches none of the datatypes in the sh:or enumeration");
                    }
                }
                engine.checkCanceled();
            }
        } else {
            for (RDFNode focusNode : focusNodes) {
                for (RDFNode valueNode : engine.getValueNodes(constraint, focusNode)) {
                    boolean hasOne = false;
                    for (RDFNode shape : shapes) {
                        Model nestedResults = hasShape(engine, constraint, focusNode, valueNode, shape, true);
                        if (nestedResults == null) {
                            hasOne = true;
                            break;
                        }
                    }
                    if (!hasOne) {
                        engine.createValidationResult(constraint, focusNode, valueNode, () -> "Value has none of the shapes in the sh:or enumeration");
                    }
                }
            }
        }

        addStatistics(constraint, startTime);
    }


    private boolean hasOnlyDatatypeConstraints() {
        if (shapes.size() == 0) {
            return false;
        }
        for (Resource shape : shapes) {
            StmtIterator mit = shape.listProperties();
            if (mit.hasNext()) {
                Statement s = mit.next();
                if (!SH.datatype.equals(s.getPredicate()) || mit.hasNext() || !s.getObject().isURIResource()) {
                    mit.close();
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
