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
package org.topbraid.shacl.py.model;

import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.riot.system.PrefixMap;
import org.apache.jena.riot.system.PrefixMapStd;
import org.apache.jena.sparql.util.NodeFactoryExtra;

/**
 * A partial implementation of DataFactory from
 * <a href="https://github.com/rdfjs/representation-task-force/blob/master/interface-spec.md">Data model specs</a>
 * to be used by Python.
 *
 * @author Ashley Caselli
 */
public class PyTermFactory {

    private final PrefixMap pm = new PrefixMapStd();

    public PyBlankNode blankNode() {
        return blankNode(null);
    }

    public PyBlankNode blankNode(String value) {
        Node node = value == null ?
                NodeFactory.createBlankNode() :
                NodeFactory.createBlankNode(value);
        return new PyBlankNode(node);
    }

    public PyLiteral literal(Object value, Object langOrDatatype) {
        String stringVal = value.toString();
        if (value instanceof Number) {
            stringVal = String.valueOf(value);
        }
        if (langOrDatatype instanceof PyNamedNode) {
            return new PyLiteral(NodeFactory.createLiteralDT(stringVal, TypeMapper.getInstance().getTypeByName(((PyNamedNode) langOrDatatype).getValue())));
        } else if (langOrDatatype instanceof String) {
            return new PyLiteral(NodeFactory.createLiteralLang(stringVal, (String) langOrDatatype));
        } else {
            throw new IllegalArgumentException("Invalid type of langOrDatatype argument");
        }
    }

    public PyNamedNode namedNode(String value) {
        Node node = NodeFactory.createURI(value);
        return new PyNamedNode(node);
    }

    public void registerNamespace(String prefix, String namespace) {
        pm.add(prefix, namespace);
    }

    public PyTerm term(Object obj) {
        Node n;
        try {
            n = NodeFactoryExtra.parseNode(String.valueOf(obj), pm);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse node \"" + obj + "\"", ex);
        }
        if (n.isURI()) {
            return new PyNamedNode(n);
        } else if (n.isLiteral()) {
            return new PyLiteral(n);
        } else if (n.isBlank()) {
            return new PyBlankNode(n);
        } else {
            throw new IllegalArgumentException("Unexpected node type for " + n);
        }
    }

}
