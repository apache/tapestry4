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

package net.sf.tapestry.script;

import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  A looping operator, modeled after the Foreach component.  It takes
 *  as its source as property and iterates through the values, updating
 *  a symbol on each pass.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.1
 * 
 **/

class ForeachToken extends AbstractToken
{
    private String key;
    private String propertyPath;
    private String[] properties;

    ForeachToken(String key, String propertyPath)
    {
        this.key = key;
        this.propertyPath = propertyPath;
    }

    public void write(StringBuffer buffer, ScriptSession session)
        throws ScriptException
    {
        if (properties == null)
            properties = PropertyHelper.splitPropertyPath(propertyPath);

        Map symbols = session.getSymbols();
        PropertyHelper helper = PropertyHelper.forInstance(symbols);

        Object rawSource = helper.getPath(symbols, properties);

        Iterator i = Tapestry.coerceToIterator(rawSource);

        while (i.hasNext())
        {
            Object newValue = i.next();

            symbols.put(key, newValue);

            writeChildren(buffer, session);
        }

        // We leave the last value as a symbol; don't know if that's
        // good or bad.
    }
}