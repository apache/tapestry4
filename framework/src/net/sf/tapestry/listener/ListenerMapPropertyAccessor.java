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
