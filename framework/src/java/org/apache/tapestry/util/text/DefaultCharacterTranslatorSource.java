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

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of a character translator source.
 * Returns a standard HTML translator that encodes everything that is non-safe
 * or an HTML translator that encodes only non-safe ASCII symbols 
 * if the encoding is a unicode one. 
 * 
 * @author mb
 * @since 3.1
 */
public class DefaultCharacterTranslatorSource implements ICharacterTranslatorSource
{
	private static final ICharacterTranslator DEFAULT_TRANSLATOR = new MarkupCharacterTranslator();
	private static final ICharacterTranslator UNICODE_TRANSLATOR = new MarkupCharacterTranslator(false);

	private final static Map _translators;
	
	static {
		_translators = new HashMap();
		_translators.put("UTF-8", UNICODE_TRANSLATOR);
		_translators.put("UTF-7", UNICODE_TRANSLATOR);
		_translators.put("UTF-16", UNICODE_TRANSLATOR);
		_translators.put("UTF-16BE", UNICODE_TRANSLATOR);
		_translators.put("UTF-16LE", UNICODE_TRANSLATOR);
	}
	
	/**
	 * Returns a translator that encodes all non-safe characters into their HTML equivalents.
	 * 
	 * @see org.apache.tapestry.util.text.ICharacterTranslatorSource#getDefaultTranslator()
	 */
	public ICharacterTranslator getDefaultTranslator() {
		return DEFAULT_TRANSLATOR;
	}

	/**
	 * If the encoding is a Unicode one, returns a translator that encodes only the 
	 * non-safe ASCII characters and leaves the others untouched.
	 * Otherwise, returns the default translator.
	 * 
	 * @see org.apache.tapestry.util.text.ICharacterTranslatorSource#getTranslator(java.lang.String)
	 */
	public ICharacterTranslator getTranslator(String encoding) {
		ICharacterTranslator translator = (ICharacterTranslator) _translators.get(encoding.toUpperCase());
		if (translator != null)
			return translator;
		return getDefaultTranslator();
	}
	
}
