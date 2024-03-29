/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */
package org.topbraid.shacl.validation.py;

import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.engine.Constraint;
import org.topbraid.shacl.model.SHConstraintComponent;
import org.topbraid.shacl.model.SHPyConstraint;
import org.topbraid.shacl.model.SHPyExecutable;
import org.topbraid.shacl.validation.ValidationEngine;
import org.topbraid.shacl.vocabulary.SH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Executes constraints based on a Python validator.
 *
 * @author Ashley Caselli
 */
public class PyComponentExecutor extends AbstractPyExecutor {

    @Override
    protected void addBindings(Constraint constraint, QuerySolutionMap bindings) {
        constraint.addBindings(bindings);
    }

    @Override
    protected SHPyExecutable getExecutable(Constraint constraint) {
        return constraint.getComponent().getValidator(SH.PyValidator, constraint.getContext()).as(SHPyConstraint.class);
    }

    @Override
    protected String getLabel(Constraint constraint) {
        return constraint.getComponent().getLocalName() + " (Python constraint component executor)";
    }

    @Override
    protected Collection<RDFNode> getValueNodes(ValidationEngine validationEngine, Constraint constraint, QuerySolutionMap bindings, RDFNode focusNode) {
        SHConstraintComponent component = constraint.getComponent();
        Resource context = constraint.getContext();
        Resource validatorResource = component.getValidator(SH.PyValidator, context);
        if (SH.PropertyShape.equals(context)) {
            if (component.hasProperty(SH.propertyValidator, validatorResource)) {
                bindings.add("path", constraint.getShapeResource().getRequiredProperty(SH.path).getObject());
                List<RDFNode> valueNodes = new ArrayList<>();
                valueNodes.add(null);
                return valueNodes;
            } else {
                return validationEngine.getValueNodes(constraint, focusNode);
            }
        } else {
            bindings.add("value", focusNode);
            return Collections.singletonList(focusNode);
        }
    }

}
