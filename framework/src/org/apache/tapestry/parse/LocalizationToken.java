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

import java.util.Map;

import org.apache.tapestry.ILocation;

/**
 *  Represents localized text from the template.
 *
 *  @see TokenType#LOCALIZATION
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class LocalizationToken extends TemplateToken
{
    private String _tag;
    private String _key;
    private boolean _raw;
    private Map _attributes;
    
    /**
     *  Creates a new token.
     * 
     * 
     *  @param tag the tag of the element from the template
     *  @param key the localization key specified
     *  @param raw if true, then the localized value contains markup that should not be escaped
     *  @param attributes any additional attributes (beyond those used to define key and raw)
     *  that were specified.  This value is retained, not copied.
     *  @param location location of the tag which defines this token
     * 
     **/
    
    public LocalizationToken(String tag, String key, boolean raw, Map attributes, ILocation location)
    {
        super(TokenType.LOCALIZATION, location);
        
        _tag = tag;
        _key = key;
        _raw = raw;
        _attributes = attributes;
    }
    
    /**
     *  Returns any attributes for the token, which may be null.  Do not modify
     *  the return value.
     * 
     **/
    
    public Map getAttributes()
    {
        return _attributes;
    }

    public boolean isRaw()
    {
        return _raw;
    }

    public String getTag()
    {
        return _tag;
    }

    public String getKey()
    {
        return _key;
    }
}
