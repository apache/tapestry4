package net.sf.tapestry.bean;

import net.sf.tapestry.IBeanProvider;

/**
 *  Initializes a bean with a static value.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public class StaticBeanInitializer extends AbstractBeanInitializer
{
    protected Object _value;

    public StaticBeanInitializer(String propertyName, Object value)
    {
        super(propertyName);

        _value = value;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        setBeanProperty(provider.getResourceResolver(), bean, _value);
    }
}