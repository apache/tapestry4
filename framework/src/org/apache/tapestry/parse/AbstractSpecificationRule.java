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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Rule;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.xml.sax.Attributes;

/**
 *  Placeholder for utility methods needed by the various
 *  specification-oriented {@link org.apache.commons.digester.Rule}s.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class AbstractSpecificationRule extends Rule
{

    protected String getValue(Attributes attributes, String name)
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
    
    protected void setProperty(String propertyName, Object value)
    throws Exception
    {
    	PropertyUtils.setProperty(digester.peek(), propertyName, value);
    }

    /**
     *  Gets the current location tag.  This requires that the
     *  rule's digester be {@link SpecificationDigester}.
     * 
     **/

    protected ILocation getLocation()
    {
        SpecificationDigester locatableDigester = (SpecificationDigester) digester;

        return locatableDigester.getLocationTag();
    }

    // Temporary, until DocumentParseException is fixed.

    protected IResourceLocation getResourceLocation()
    {
        SpecificationDigester locatableDigester = (SpecificationDigester) digester;

        return locatableDigester.getResourceLocation();
    }
}
