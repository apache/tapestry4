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

package org.apache.tapestry.util.text;

/**
 * Matches a given character only if the provided object does NOT match it.
 * 
 * @author mb
 * @since 3.1
 */
public class InverseMatcher implements ICharacterMatcher
{
    private ICharacterMatcher _matcher;
    
    /**
     * Creates a new object that inverts the matching rule of the provided matcher
     * 
     * @param matcher the matcher whose behaviour will be inverted
     */
    public InverseMatcher(ICharacterMatcher matcher)
    {
        _matcher = matcher;
    }
    
    /**
     * Matches the character only if the provided object does NOT match it.
     * 
     * @see org.apache.tapestry.util.text.ICharacterMatcher#matches(char)
     */
    public boolean matches(char ch)
    {
        return !_matcher.matches(ch);
    }
}
