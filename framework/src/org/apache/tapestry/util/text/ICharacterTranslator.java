//Copyright 2004 The Apache Software Foundation
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
 * An interface for translating a character into a string.
 * 
 * @author mb
 * @version $Id$
 * @since 3.1
 */
public interface ICharacterTranslator 
{
	/**
	 * Translates the provided character into a string 
	 * 
	 * @param ch the character to be translated
	 * @return null if the character is not to be translated,
	 * an empty string if the character is to be ignored, 
	 * or another string to represent the character translation
	 */
	String translate(char ch);
}
