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

import org.apache.commons.lang.enum.Enum;

/**
 *  The type of an {@link org.apache.tapestry.parse.TemplateAttribute}.
 *  New types can be created by modifying
 *  {@link org.apache.tapestry.parse.TemplateParser} to recognize
 *  the attribute prefix in compnent tags, and
 *  by modifying
 *  {@link org.apache.tapestry.BaseComponentTemplateLoader}
 *  to actually do something with the TemplateAttribute, based on type.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class AttributeType extends Enum
{
	/**
	 *  Indicates the attribute is simple, literal text.  This is
	 *  the default for any attributes without a recognized
	 *  prefix.
	 * 
	 *  @see org.apache.tapestry.binding.StaticBinding
	 * 
	 **/
	
	public static final AttributeType LITERAL = new AttributeType("LITERAL");

	/**
	 *  Indicates the attribute is a OGNL expression.
	 * 
	 *  @see org.apache.tapestry.binding.ExpressionBinding
	 * 
	 **/
	
	public static final AttributeType OGNL_EXPRESSION = new AttributeType("OGNL_EXPRESSION");
	
	/**
	 *  Indicates the attribute is a localization key.
	 * 
	 *  @see org.apache.tapestry.binding.StringBinding
	 * 
	 **/
	
	public static final AttributeType LOCALIZATION_KEY = new AttributeType("LOCALIZATION_KEY");

    private AttributeType(String name)
    {
        super(name);
    }

}
