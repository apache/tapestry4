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

package org.apache.tapestry.parse;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.util.xml.InvalidStringException;
import org.xml.sax.Attributes;

/**
 *  Validates that an attribute matches a specified pattern.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ValidateRule extends AbstractSpecificationRule
{
    private RegexpMatcher _matcher;
    private String _attributeName;
    private String _pattern;
    private String _errorKey;

	public ValidateRule(RegexpMatcher matcher, String attributeName, String pattern, String errorKey)
	{
		_matcher = matcher;
		_attributeName = attributeName;
		_pattern = pattern;
		_errorKey = errorKey;
	}

	/**
	 *  Validates that the attribute, if provided, matches the pattern.
	 * 
	 *  @throws InvalidStringException if the value does not match the pattern.
	 * 
	 **/
	
    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String value = getValue(attributes, _attributeName);
        if (value == null)
            return;

        if (_matcher.matches(_pattern, value))
            return;

        throw new InvalidStringException(
            Tapestry.format(_errorKey, value),
            value,
            getResourceLocation());
    }

}
