//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.test;

/**
 * Interface for classes that can test a response from the (simulated) web server
 * for conformance to some expectation; most implementions check the 
 * response content for particular literal values or regular expressions.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ResponseAssertion
{
	/**
	 * Invoked after a response has been recieved from the simulated servlet container;
	 * assertions run to check that the response is as expected.
	 */
	public void execute(ScriptedTestSession session);
}
