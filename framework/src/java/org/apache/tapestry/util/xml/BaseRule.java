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

package org.apache.tapestry.util.xml;

import org.apache.tapestry.Tapestry;
import org.xml.sax.Attributes;

/**
 * Base implementation of {@link org.apache.tapestry.util.xml.IRule} that
 * does nothing.
 *
 * @author Howard Lewis Ship
 * @since 3.0
 **/
public class BaseRule implements IRule
{
	protected String getAttribute(Attributes attributes, String name)
	{
		int count = attributes.getLength();

		for (int i = 0; i < count; i++)
		{
			String attributeName = attributes.getLocalName(i);
        	
			if (Tapestry.isBlank(attributeName))
				attributeName = attributes.getQName(i);
        	
			if (attributeName.equals(name))
				return attributes.getValue(i);
		}

		return null;
	}

    public void startElement(RuleDirectedParser parser, Attributes attributes)
    {

    }

    public void endElement(RuleDirectedParser parser)
    {

    }

    public void content(RuleDirectedParser parser, String content)
    {

    }

}
