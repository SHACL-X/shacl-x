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

package org.topbraid.jenax.functions;

import org.apache.jena.rdf.model.Resource;


/**
 * Can be used to define custom function factories such as spinx.
 *
 * @author Holger Knublauch
 */
public interface DeclarativeFunctionDriver {

    /**
     * If this factory is responsible for the provided function Resource
     * then it must create a FunctionFactory which can then be registered.
     *
     * @param function the function's declaration resource
     * @return the FunctionFactory or null if this is not responsible
     */
    DeclarativeFunctionFactory create(Resource function);
}
