package net.sf.tapestry.bean;

/**
 * 
 *  Initializes a helper bean property from an OGNL expression (relative
 *  to the bean's {@link IComponent}).
 *
 *  <p>Replacement for {@link net.sf.tapestry.bean.PropertyBeanInitializer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/
public class ExpressionBeanInitializer extends PropertyBeanInitializer
{

    public ExpressionBeanInitializer(String propertyName, String expression)
    {
        super(propertyName, expression);
    }

}
