// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.util.text;

/**
 * Combine a set of character matchers. A given character will be matched if any
 * of the provided objects matches it.
 * 
 * @author mb
 * @since 3.1
 */
public class CompoundMatcher implements ICharacterMatcher
{
    private ICharacterMatcher[] _matchers;
    
    /**
     * Create a new object that will match a character if any of the provided objects matches it.
     * 
     * @param matchers the array of objects that will be queried if a character matches
     */
    public CompoundMatcher(ICharacterMatcher[] matchers) {
        _matchers = matchers;
    }
    
    /**
     * Match the character if any of the provided objects matches it.
     * 
     * @see org.apache.tapestry.util.text.ICharacterMatcher#matches(char)
     */
    public boolean matches(char ch)
    {
        for (int i = 0; i < _matchers.length; i++) {
            if (_matchers[i].matches(ch))
                return true;
        }
        return false;
    }
}