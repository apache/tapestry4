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

import java.util.Map;

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  A token that writes the value of a property using a property path
 *  routed in the symbols..
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class InsertToken implements IScriptToken
{
    private String _expression;

    InsertToken(String expression)
    {
        _expression = expression;
    }

    /**
     *  Gets the named symbol from the symbols {@link Map}, verifies that
     *  it is a String, and writes it to the {@link Writer}.
     *
     **/

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Map symbols = session.getSymbols();

        Object value = OgnlUtils.get(_expression, symbols);

        if (value != null)
            buffer.append(value);
    }

    public void addToken(IScriptToken token)
    {
        // Should never be invoked.
    }

}