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
