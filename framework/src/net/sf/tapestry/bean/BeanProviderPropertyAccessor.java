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
package net.sf.tapestry.bean;

import java.util.Map;

import net.sf.tapestry.IBeanProvider;
import ognl.ObjectPropertyAccessor;
import ognl.OgnlException;

/**
 *  Adapts a {@link net.sf.tapestry.IBeanProvider} to
 *  <a href="http://www.ognl.org">OGNL</a> by exposing the named
 *  beans provided by the provider as read-only properties of
 *  the provider.
 * 
 *  <p>This is registered by {@link net.sf.tapestry.AbstractComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class BeanProviderPropertyAccessor extends ObjectPropertyAccessor
{
    /**
     *  Checks to see if the name matches the name of a bean inside
     *  the provider and returns that bean if so.
     *  Otherwise, invokes the super implementation.
     * 
     **/
    
    public Object getProperty(Map context, Object target, Object name) throws OgnlException
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = (String)name;
        
        if (provider.canProvideBean(beanName))
            return provider.getBean(beanName);
        
        return super.getProperty(context, target, name);
    }

    /**
     *  Returns true if the name matches a bean provided by the provider.
     *  Otherwise invokes the super implementation.
     * 
     **/
    
    public boolean hasGetProperty(Map context, Object target, Object oname) throws OgnlException
    {
        IBeanProvider provider = (IBeanProvider)target;
        String beanName = (String)oname;

        if (provider.canProvideBean(beanName))
            return true;
            
        return super.hasGetProperty(context, target, oname);
    }

}
