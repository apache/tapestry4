package net.sf.tapestry.bean;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.OgnlUtils;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Base class for initializing a property of a JavaBean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

abstract public class AbstractBeanInitializer implements IBeanInitializer
{
    protected String _propertyName;

    public AbstractBeanInitializer(String propertyName)
    {
        _propertyName = propertyName;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    protected void setBeanProperty(IResourceResolver resolver, Object bean, Object value)
    {
        OgnlUtils.set(_propertyName, resolver, bean, value);
    }
}