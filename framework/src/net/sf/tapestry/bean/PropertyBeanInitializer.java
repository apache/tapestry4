package net.sf.tapestry.bean;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.prop.OgnlUtils;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Initializes a helper bean property from an OGNL expression (relative
 *  to the bean's {@link IComponent}).
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *  @deprecated To be removed in 2.3.  Use {@link ExpressionBeanInitializer}.
 * 
 **/

public class PropertyBeanInitializer extends AbstractBeanInitializer
{
    protected String _expression;

    public PropertyBeanInitializer(String propertyName, String expression)
    {
        super(propertyName);

        _expression = expression;
    }

    public void setBeanProperty(IBeanProvider provider, Object bean)
    {
        IResourceResolver resolver = provider.getResourceResolver();
        IComponent component = provider.getComponent();
        
        Object value = OgnlUtils.get(_expression, resolver, component);

        setBeanProperty(resolver, bean, value);
    }

}