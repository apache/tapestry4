// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.test.assertions.AssertOutput;

/**
 * Test for the {@link org.apache.tapestry.test.assertions.AssertOutput}response assertion.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestAssertOutput extends TapestryTestCase
{
    public void testSuccess() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertOutput ao = new AssertOutput();

        ao.setExpectedSubstring("<title>Test</title>");

        ao.execute(ss);
    }

    public void testFailure() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertOutput ao = new AssertOutput();

        ao.setExpectedSubstring("<title>Home</title>");

        try
        {
            ao.execute(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                    "Expected text \\(\"<title>Home</title>\", at .*?\\) was not found in the response\\.",
                    ex.getMessage());
        }
    }

}