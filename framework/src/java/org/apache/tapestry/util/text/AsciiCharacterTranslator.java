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
 * An object that translates selected ASCII characters into equivalent strings.
 * 
 * @author mb
 * @since 3.1
 */
public class AsciiCharacterTranslator implements ICharacterTranslator
{
	private String[] _charMap;
	
	/**
	 * Creates and initializes a new translator that translates the provided 
	 * ASCII characters into strings. All other characters will be translated to null.
	 * 
	 * @param characterMap an array of pairs of strings. 
	 *        Each pair consists of a key that must be a single ASCII character, 
	 *        and a value that is its equivalent string. 
	 */
	public AsciiCharacterTranslator(String[][] characterMap)
	{
		_charMap = new String[128];
		
		int pairCount = characterMap.length;
		for (int i = 0; i < pairCount; i++) {
			String[] pair = characterMap[i];
			if (pair.length != 2)
				continue;
			String key = pair[0];
			String value = pair[1];
			if (key.length() != 1)
				continue;
			char ch = key.charAt(0);
			if (ch >= 128)
				continue;
			
			_charMap[ch] = value;
		}
	}
	
	/**
	 * @see org.apache.tapestry.util.text.ICharacterTranslator#translate(char)
	 */
	public String translate(char ch) {
		if (ch >= 128)
			return null;
		return _charMap[ch];
	}
}
