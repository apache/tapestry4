package net.sf.tapestry.pageload;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  Given a component, a property and a value, this object will
 *  reset the property to the value whenever the page
 *  (containing the component) is detached.  This is related
 *  to support for {@link net.sf.tapestry.spec.PropertySpecification}s.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class PropertyInitializer implements PageDetachListener
{
    private IResourceResolver _resolver;
    private IComponent _component;
    private String _propertyName;
    private Object _value;

    public PropertyInitializer(
        IResourceResolver resolver,
        IComponent component,
        String propertyName,
        Object value)
    {
        _resolver = resolver;
        _component = component;
        _propertyName = propertyName;
        _value = value;
    }

    public void pageDetached(PageEvent event)
    {
        OgnlUtils.set(_propertyName, _resolver, _component, _value);
    }

}
