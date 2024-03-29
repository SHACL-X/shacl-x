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
package org.topbraid.shacl.model.impl;

import org.apache.jena.enhanced.EnhGraph;
import org.apache.jena.graph.Node;
import org.apache.jena.vocabulary.RDFS;
import org.topbraid.jenax.util.JenaDatatypes;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.model.SHPyConstraint;
import org.topbraid.shacl.vocabulary.SH;

public class SHPyConstraintImpl extends SHResourceImpl implements SHPyConstraint {

    public SHPyConstraintImpl(Node node, EnhGraph graph) {
        super(node, graph);
    }


    @Override
    public String getFunctionName() {
        return JenaUtil.getStringProperty(this, SH.pyFunctionName);
    }


    @Override
    public boolean isDeactivated() {
        return hasProperty(SH.deactivated, JenaDatatypes.TRUE);
    }


    @Override
    public String toString() {

        String label = JenaUtil.getStringProperty(this, RDFS.label);
        if (label != null) {
            return label;
        }

        String comment = JenaUtil.getStringProperty(this, RDFS.comment);
        if (comment != null) {
            return comment;
        }

        String message = JenaUtil.getStringProperty(this, SH.message);
        if (message != null) {
            return message;
        }

        String script = getFunctionName();
        if (script != null) {
            return script;
        }

        return "(Incomplete Python Constraint)";
    }
}
