package net.sf.tapestry.bean;

import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IComponent;

/**
 *  A bean initializer that uses a localized string from the containing
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class StringBeanInitializer extends AbstractBeanInitializer
{
    protected String _key;

    public StringBeanInitializer(String propertyName, String key)
    {
        super(propertyName);

        _key = key;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IComponent component = provider.getComponent();
        String value = component.getString(_key);
        
        setBeanProperty(provider.getResourceResolver(), bean, value);
    }
}
