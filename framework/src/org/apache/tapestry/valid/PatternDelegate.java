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

package org.apache.tapestry.valid;

/**
 * Implementations of this interface will provide pattern utility services.
 * 
 * @author  Harish Krishnaswamy
 * @version $Id$
 * @since   3.0
 */
public interface PatternDelegate
{
    /**
     * Answers the question whether the input string fulfills the pattern string provided.
     * 
     * @param patternString The pattern that the input string is compared against.
     * @param input The string under test.
     * @return Returns true if the pattern exists in the input string; returns false otherwise.
     */
    public boolean contains(String patternString, String input);
    
    /**
     * Returns the escaped sequence of characters representing the pattern string provided.
     * 
     * @param patternString The raw sequence of characters that represent the pattern.
     * @return The escaped sequence of characters that represent the pattern.
     */
    public String getEscapedPatternString(String patternString);
}
