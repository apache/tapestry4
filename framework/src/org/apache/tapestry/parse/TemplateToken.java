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
import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocation;

/**
 *  Base class for a number of different types of tokens that can be extracted
 *  from a page/component template.  This class defines the
 *  type of the token,
 *  subclasses provide interpretations on the token.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class TemplateToken implements ILocatable
{
    private TokenType _type;
    private ILocation _location;

    protected TemplateToken(TokenType type, ILocation location)
    {
        _type = type;
        _location = location;
    }

    public TokenType getType()
    {
        return _type;
    }
    
    public ILocation getLocation()
    {
    	return _location;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("type", _type.getName());
        builder.append("location", _location);

        extendDescription(builder);

        return builder.toString();
    }

    /**
     *  Overridden in subclasses to append additional fields (defined in the subclass)
     *  to the description.  Subclasses may override this method without invoking
     *  this implementation, which is empty.
     * 
     *  @since 3.0
     * 
     **/

    protected void extendDescription(ToStringBuilder builder)
    {
    }
}