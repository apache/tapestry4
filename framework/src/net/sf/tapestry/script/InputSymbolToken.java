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

import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;

/**
 *  A token that validates that an input symbol exists or is of a
 *  declared type.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

class InputSymbolToken extends AbstractToken
{
    private String _key;
    private Class _class;
    private boolean _required;

    InputSymbolToken(String key, Class clazz, boolean required)
    {
        _key = key;
        _class = clazz;
        _required = required;
    }

    public void write(StringBuffer buffer, ScriptSession session) throws ScriptException
    {
        Object value = session.getSymbols().get(_key);

        if (_required && value == null)
            throw new ScriptException(Tapestry.getString("InputSymbolToken.required", _key), session);

        if (value != null && _class != null && !_class.isAssignableFrom(value.getClass()))
            throw new ScriptException(
                Tapestry.getString("InputSymbolToken.wrong-type", _key, value.getClass().getName(), _class.getName()),
                session);
    }

}
