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

package org.apache.tapestry.test.assertions;

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.test.ResponseAssertion;
import org.apache.tapestry.test.ScriptMessages;
import org.apache.tapestry.test.ScriptedTestSession;

/**
 * Assertion used to check for the presence of a regular expression
 * within the response output. Alternately, it can check for a specific
 * list of matches of a regular expression within the response.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class AssertRegexp extends BaseLocatable implements ResponseAssertion
{

    private List _matches;
    private String _regexp;
    private int _subgroup;

    public void addMatch(RegexpMatch m)
    {
        if (_matches == null)
            _matches = new ArrayList();

        _matches.add(m);
    }

    public void execute(ScriptedTestSession session)
    {
        if (_matches != null)
        {
            executeMatches(session);
            return;
        }

        String responseContent = session.getResponse().getOutputString();

        if (session.getMatcher().contains(_regexp, responseContent))
            return;

        throw new ApplicationRuntimeException(
            ScriptMessages.expectedRegexpMissing(_regexp, getLocation()),
            getLocation(),
            null);
    }

    private void executeMatches(ScriptedTestSession session)
    {
        String responseContent = session.getResponse().getOutputString();

        String[] matches = session.getMatcher().getMatches(_regexp, responseContent, _subgroup);

        int expectedCount = _matches.size();
        int count = Math.min(expectedCount, matches.length);

        for (int i = 0; i < count; i++)
        {
            RegexpMatch m = (RegexpMatch) _matches.get(i);

            String expected = m.getExpectedString();
            String actual = matches[i];

            if (expected.equals(actual))
                continue;

            throw new ApplicationRuntimeException(
                ScriptMessages.incorrectRegexpMatch(expected, m.getLocation(), actual),
                m.getLocation(),
                null);

        }

        if (matches.length != expectedCount)
            throw new ApplicationRuntimeException(
                ScriptMessages.incorrectRegexpMatchCount(
                    _regexp,
                    getLocation(),
                    expectedCount,
                    matches.length),
                getLocation(),
                null);
    }

    public void setRegexp(String string)
    {
        _regexp = string;
    }

    public void setSubgroup(int subgroup)
    {
        _subgroup = subgroup;
    }

}
