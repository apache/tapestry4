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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  An attribute, associated with a {@link org.apache.tapestry.parse.OpenToken}, taken
 *  from a template.  Each attribute has a type and a value.  The interpretation of the
 *  value is based on the type.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TemplateAttribute
{
	private AttributeType _type;
	private String _value;
	
	public TemplateAttribute(AttributeType type, String value)
	{
		_type = type;
		_value = value;
	}
	
	public AttributeType getType()
	{
		return _type;
	}
	
	public String getValue()
	{
		return _value;
	}
	
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("type", _type);
		builder.append("value", _value);
		
		return builder.toString();
	}
}
