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
 * An object that encodes a character according to rules of the HTML specification, 
 * so that it will be properly parsed by a browser irrespectively of the character
 * encoding used in the HTML output.
 * 
 * @author mb
 * @since 3.1
 */
public class MarkupCharacterTranslator implements ICharacterTranslator
{
    private static final String SAFE_CHARACTERS =
        "01234567890"
            + "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "\t\n\r !#$%'()*+,-./:;=?@[\\]^_`{|}~";

    private static final String[][] ENTITIES = {
    	{ "\"", "&quot;" }, 
		{ "<", "&lt;" },
		{ ">", "&gt;" },
		{ "&", "&amp;" }
    };
    
    private static final ICharacterMatcher SAFE_MATCHER = new AsciiCharacterMatcher(SAFE_CHARACTERS);
    private static final ICharacterTranslator ENTITY_TRANSLATOR = new AsciiCharacterTranslator(ENTITIES);
    
    private boolean _encodeNonAscii;
    private ICharacterMatcher _safeMatcher;
    private ICharacterTranslator _entityTranslator;
	
    public MarkupCharacterTranslator()
    {
    	this(true);
    }
    
    public MarkupCharacterTranslator(boolean encodeNonAscii)
    {
    	this(encodeNonAscii, SAFE_MATCHER, ENTITY_TRANSLATOR);
    }
    
    public MarkupCharacterTranslator(boolean encodeNonAscii, ICharacterMatcher safeMatcher, ICharacterTranslator entityTranslator)
    {
    	_encodeNonAscii = encodeNonAscii;
    	_safeMatcher = safeMatcher;
    	_entityTranslator = entityTranslator;
    }

    public MarkupCharacterTranslator(boolean encodeNonAscii, String safeCharacters, String[][] entities)
    {
    	_encodeNonAscii = encodeNonAscii;
    	_safeMatcher = new AsciiCharacterMatcher(safeCharacters);
    	_entityTranslator = new AsciiCharacterTranslator(entities);
    }
    
	/**
	 * @see org.apache.tapestry.util.text.IMarkupCharacterTranslator#translateAttribute(char)
	 */
	public String translate(char ch) {
		if (ch >= 128 && !_encodeNonAscii)
			return null;
		
		if (_safeMatcher.matches(ch))
			return null;

		String entity = _entityTranslator.translate(ch);
		if (entity != null)
			return entity;
		
		// needs to use a NumberFormat here to be fully compliant, 
		// but this is accepted fine by the browsers
		return "&#" + (int) ch + ";";
	}
}
