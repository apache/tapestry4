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

package net.sf.tapestry.listener;

import java.util.Map;

import ognl.ObjectPropertyAccessor;
import ognl.OgnlException;

/**
 *  Exposes {@link net.sf.tapestry.IActionListener} listeners
 *  provided by the {@link ListenerMap} as read-only properties
 *  of the ListenerMap.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class ListenerMapPropertyAccessor extends ObjectPropertyAccessor
{
    /**
     *  Checks to see if the ListenerMap provides the named
     *  listener, returning the listener if it does.  Otherwise,
     *  invokes the super implementation.
     * 
     **/
    
    public Object getProperty(Map context, Object target, Object name) throws OgnlException
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = (String) name;

        if (map.canProvideListener(listenerName))
            return map.getListener(listenerName);

        return super.getProperty(context, target, name);
    }
    
    /**
     *  Returns true if the ListenerMap contains the named listener,
     *  otherwise invokes super-implementation.
     * 
     **/

    public boolean hasGetProperty(Map context, Object target, Object oname) throws OgnlException
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = (String) oname;

        if (map.canProvideListener(listenerName))
            return true;

        return super.hasGetProperty(context, target, oname);
    }

}
