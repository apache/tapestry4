//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.parse;

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
     **/

    public static final TokenType TEXT = new TokenType("TEXT");

    /**
     *  The opening tag of an element.
     *
     **/

    public static final TokenType OPEN = new TokenType("OPEN");

    /**
     *  The closing tag of an element.
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