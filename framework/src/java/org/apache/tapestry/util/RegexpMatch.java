// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.util;

import org.apache.oro.text.regex.MatchResult;

/**
 * A "friendly" version of a regular expression match.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class RegexpMatch
{
    private final MatchResult _match;

    RegexpMatch(MatchResult match)
    {
        _match = match;
    }

    /**
     * Returns a matching group within the input string. Group 0 is the entire input string, group 1
     * is the content with the first expression, etc.
     */

    public String getGroup(int group)
    {
        return _match.group(group);
    }

    /**
     * Returns the entire matching input.
     */

    public String getInput()
    {
        return _match.toString();
    }
    
    public int getMatchLength()
    {
        return _match.length();
    }
}
