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

package org.apache.tapestry.test.assertions;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.test.ResponseAssertion;
import org.apache.tapestry.test.ScriptMessages;
import org.apache.tapestry.test.ScriptedTestSession;
import org.apache.tapestry.test.mock.MockResponse;

/**
 * Assertion that checks for the presence of a particular
 * substring within the response text.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class AssertOutput extends BaseLocatable implements ResponseAssertion
{
    private String _expectedSubstring;

    public void setExpectedSubstring(String expectedSubstring)
    {
        _expectedSubstring = expectedSubstring;
    }

    public void execute(ScriptedTestSession session)
    {
        MockResponse response = session.getResponse();

        String output = response.getOutputString();

        if (output.indexOf(_expectedSubstring) >= 0)
            return;

        throw new ApplicationRuntimeException(
            ScriptMessages.expectedSubstringMissing(_expectedSubstring, getLocation()),
            getLocation(),
            null);
    }

}
