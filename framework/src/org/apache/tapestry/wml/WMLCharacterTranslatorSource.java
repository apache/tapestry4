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

package org.apache.tapestry.wml;

import org.apache.tapestry.util.text.AsciiCharacterMatcher;
import org.apache.tapestry.util.text.AsciiCharacterTranslator;
import org.apache.tapestry.util.text.ICharacterMatcher;
import org.apache.tapestry.util.text.ICharacterTranslator;
import org.apache.tapestry.util.text.ICharacterTranslatorSource;
import org.apache.tapestry.util.text.MarkupCharacterTranslator;

/**
 * The WML implementation of a character translator source.
 * Returns a WML translator that encodes everything that is non-safe.
 * 
 * Some code borrowed from WMLWriter (by David Solis)
 * 
 * @author mb
 * @since 3.1
 */
public class WMLCharacterTranslatorSource implements ICharacterTranslatorSource
{
    private static final String SAFE_CHARACTERS =
        "01234567890"
            + "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "\t\n\r !\"#%'()*+,-./:;=?@[\\]^_`{|}~";

    private static final String[][] ENTITIES = {
        	{ "\"", "&quot;" }, 
    		{ "<", "&lt;" },
    		{ ">", "&gt;" },
    		{ "&", "&amp;" },
    		{ "$", "$$" }
        };

    private static final ICharacterMatcher SAFE_MATCHER = new AsciiCharacterMatcher(SAFE_CHARACTERS);
    private static final ICharacterTranslator ENTITY_TRANSLATOR = new AsciiCharacterTranslator(ENTITIES);

    private static final ICharacterTranslator WML_TRANSLATOR = new MarkupCharacterTranslator(true, SAFE_MATCHER, ENTITY_TRANSLATOR);

	/**
	 * @see org.apache.tapestry.util.text.ICharacterTranslatorSource#getDefaultTranslator()
	 */
	public ICharacterTranslator getDefaultTranslator() {
		return WML_TRANSLATOR;
	}

	/**
	 * @see org.apache.tapestry.util.text.ICharacterTranslatorSource#getTranslator(java.lang.String)
	 */
	public ICharacterTranslator getTranslator(String encoding) {
		return getDefaultTranslator();
	}
}
