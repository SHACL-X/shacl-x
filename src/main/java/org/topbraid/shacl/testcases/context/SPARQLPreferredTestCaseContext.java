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
package org.topbraid.shacl.testcases.context;

import org.topbraid.shacl.util.SHACLPreferences;

/**
 * A TestCaseContext in which SPARQL execution is preferred over JavaScript.
 * 
 * @author Holger Knublauch
 */
public class SPARQLPreferredTestCaseContext implements TestCaseContext {
	
	public static TestCaseContextFactory getTestCaseContextFactory() {
		return new TestCaseContextFactory() {
			@Override
			public TestCaseContext createContext() {
				return new SPARQLPreferredTestCaseContext();
			}
		};
	}

	
	private boolean oldValueJS;
	private boolean oldValuePy;

	@Override
	public void setUpTestContext() {
		oldValueJS = SHACLPreferences.isJSPreferred();
		oldValuePy = SHACLPreferences.isPyPreferred();
		SHACLPreferences.setJSPreferred(false);
		SHACLPreferences.setPyPreferred(false);
	}

	
	@Override
	public void tearDownTestContext() {
		SHACLPreferences.setJSPreferred(oldValueJS);
		SHACLPreferences.setPyPreferred(oldValuePy);
	}


	@Override
	public String toString() {
		return "SPARQL preferred";
	}
}
