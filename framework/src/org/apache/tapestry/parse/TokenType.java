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
 * An {@link Enum} of the different possible token types.
 *
 * @see TemplateToken
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class TokenType extends Enum
{
    /**
     *  Raw HTML text.
     * 
     *  @see TextToken
     * 
     *
     **/

    public static final TokenType TEXT = new TokenType("TEXT");

    /**
     *  The opening tag of an element.
     * 
     *  @see OpenToken
     *
     **/

    public static final TokenType OPEN = new TokenType("OPEN");

    /**
     *  The closing tag of an element.
     * 
     *  @see CloseToken
     *
     **/

    public static final TokenType CLOSE = new TokenType("CLOSE");

    /**
     * 
     *  A reference to a localized string.
     * 
     *  @since 2.0.4
     * 
     **/
    
    public static final TokenType LOCALIZATION = new TokenType("LOCALIZATION");
    
    private TokenType(String name)
    {
        super(name);
    }

}