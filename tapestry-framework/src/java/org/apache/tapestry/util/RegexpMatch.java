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

/**
 * A "friendly" version of a regular expression match. 
 */
public class RegexpMatch
{
    private final int _groupCount;
    private final String[] _groups;

    RegexpMatch(int groupCount, String[] groups)
    {
        _groupCount = groupCount;
        _groups = groups;
    }

    /**
     * Returns a matching group within the input string. Group 0 is the entire input string, group 1
     * is the content with the first expression, etc.
     */

    public String getGroup(int group)
    {
        return _groups[group];
    }

    /**
     * Returns the entire matching input.
     */

    public String getInput()
    {
        return _groups[0];
    }
    
    public int getMatchLength()
    {
        return _groups[0].length();
    }
}
