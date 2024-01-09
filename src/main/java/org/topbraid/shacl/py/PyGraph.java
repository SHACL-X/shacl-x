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
package org.topbraid.shacl.py;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.topbraid.jenax.util.ExceptionUtil;
import org.topbraid.shacl.js.ScriptEngine;
import org.topbraid.shacl.py.model.PyFactory;
import org.topbraid.shacl.py.model.PyTriple;
import org.topbraid.shacl.util.FailureLog;

import java.util.HashSet;
import java.util.Set;

public class PyGraph {

    protected ScriptEngine engine;
    private Graph graph;
    private Set<PyTripleIterator> openIterators = new HashSet<>();

    public PyGraph(Graph graph, ScriptEngine engine) {
        this.engine = engine;
        this.graph = graph;
    }

    public void close() {
        if (!openIterators.isEmpty()) {
            FailureLog.get().logWarning("Python graph session ended but " + openIterators.size() + " iterators have not been closed. Make sure to close them programmatically to avoid resource leaks and locking problems.");
        }
        closeIterators();
    }

    public void closeIterators() {
        for (PyTripleIterator stream : openIterators) {
            stream.closeIterator();
        }
        openIterators.clear();
    }

    public Graph getGraph() {
        return graph;
    }

    public PyTripleIterator find(Object subjectSOM, Object predicateSOM, Object objectSOM) {
        Node subject = PyFactory.getNode(subjectSOM);
        Node predicate = PyFactory.getNode(predicateSOM);
        Node object = PyFactory.getNode(objectSOM);
        ExtendedIterator<Triple> it = getGraph().find(subject, predicate, object);
        PyTripleIterator pyit = new PyTripleIterator(it);
        openIterators.add(pyit);
        return pyit;
    }

    public Object query() {
        try {
            return engine.invokeFunctionOrdered("RDFQuery", new Object[]{this});
        } catch (Exception ex) {
            throw ExceptionUtil.throwUnchecked(ex);
        }
    }

    public class PyTripleIterator {

        private ExtendedIterator<Triple> it;

        PyTripleIterator(ExtendedIterator<Triple> it) {
            this.it = it;
        }

        public void close() {
            closeIterator();
            openIterators.remove(this);
        }

        void closeIterator() {
            it.close();
        }

        public PyTriple next() {
            if (it.hasNext()) {
                Triple triple = it.next();
                return PyFactory.asPyTriple(triple);
            } else {
                close();
                return null;
            }
        }
    }
}
