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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.ILocation;

/**
 *  Token representing the open tag for a component.  Components may be either
 *  specified or implicit.  Specified components (the traditional type, dating
 *  back to the origin of Tapestry) are matched by an entry in the
 *  containing component's specification.  Implicit components specify their
 *  type in the component template and must not have an entry in
 *  the containing component's specification.
 *
 *  @see TokenType#OPEN
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class OpenToken extends TemplateToken
{
    private String _tag;
    private String _id;
    private String _componentType;
    private Map _attributes;

    /**
     *  Creates a new token with the given tag, id and type 
     * 
     *  @param tag the template tag which represents the component, typically "span"
     *  @param id the id for the component, which may be assigned by the template
     *  parser for implicit components
     *  @param  componentType the type of component, if an implicit component, or null for
     *  a specified component
     *  @param location location of tag represented by this token
     * 
     **/

    public OpenToken(String tag, String id, String componentType, ILocation location)
    {
        super(TokenType.OPEN, location);

        _tag = tag;
        _id = id;
        _componentType = componentType;
    }

    /**
     *  Returns the id for the component.
     * 
     **/
    
    public String getId()
    {
        return _id;
    }

    /**
     *  Returns the tag used to represent the component within the template.
     * 
     **/
    
    public String getTag()
    {
        return _tag;
    }
    
    /**
     *  Returns the specified component type, or null for a component where the type
     *  is not defined in the template.  The type may include a library id prefix.
     * 
     **/
    
    public String getComponentType()
    {
        return _componentType;
    }

	public void addAttribute(String name, AttributeType type, String value)
	{
		TemplateAttribute attribute = new TemplateAttribute(type, value);
		
		if (_attributes == null)
		_attributes = new HashMap();
		
		_attributes.put(name, attribute);
	}
	
	/**
	 *  Returns a Map of attributes.  Key is the attribute name, value
	 *  is an instance of {@link org.apache.tapestry.parse.TemplateAttribute}.
	 *  The caller should not modify the Map.  Returns null if
	 *  this OpenToken contains no attributes.
	 * 
	 **/
	
	public Map getAttributesMap()
	{
		return _attributes;
	}

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("id", _id);
        builder.append("componentType", _componentType);
        builder.append("tag", _tag);
        builder.append("attributes", _attributes);
    }

}
