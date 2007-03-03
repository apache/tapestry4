// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.listener;

import java.util.Map;

import ognl.ObjectPropertyAccessor;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import ognl.enhance.ExpressionCompiler;
import ognl.enhance.UnsupportedCompilationException;

/**
 * Exposes {@link org.apache.tapestry.IActionListener}&nbsp;listeners provided
 * by the {@link org.apache.tapestry.listener.ListenerMap}&nbsp;as read-only
 * properties of the map.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class ListenerMapPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor
{

    /**
     * Checks to see if the ListenerMapImpl provides the named listener,
     * returning the listener if it does. Otherwise, invokes the super
     * implementation.
     */

    public Object getProperty(Map context, Object target, Object name)
        throws OgnlException
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = (String) name;

        if (map.canProvideListener(listenerName))
            return map.getListener(listenerName);

        return super.getProperty(context, target, name);
    }

    /**
     * Returns true if the ListenerMap contains the named listener, otherwise
     * invokes super-implementation.
     */

    public boolean hasGetProperty(Map context, Object target, Object oname)
        throws OgnlException
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = (String) oname;

        if (map.canProvideListener(listenerName)) return true;

        return super.hasGetProperty(context, target, oname);
    }
    
    public Class getPropertyClass(OgnlContext context, Object target, Object name)
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = (String) name;
        
        if (map.canProvideListener(listenerName))
            return map.getListener(listenerName).getClass();
        
        return super.getPropertyClass(context, target, name);
    }
    
    public String getSourceAccessor(OgnlContext context, Object target, Object name)
    {
        ListenerMap map = (ListenerMap) target;
        String listenerName = ((String)name).replaceAll("\"", "");
        
        if (map.canProvideListener(listenerName)) {
            
            Class type = OgnlRuntime.getCompiler().getInterfaceClass(map.getListener(listenerName).getClass());
            
            ExpressionCompiler.addCastString(context, "((" + type.getName() + ")");
            
            context.setCurrentAccessor(ListenerMap.class);
            context.setCurrentType(type);
            
            return ".getListener(" + name + "))";
        }
        
        return super.getSourceAccessor(context, target, name);
    }
    
    public String getSourceSetter(OgnlContext context, Object target, Object name)
    {
        throw new UnsupportedCompilationException("Can't set listeners on ListenerMap.");
    }
}
