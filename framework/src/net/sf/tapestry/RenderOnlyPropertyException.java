package net.sf.tapestry;

/**
 *  Exception thrown when a property of an {@link IComponent} is accessed that
 *  is only valid while the component is actually rendering (such properties
 *  are related to parameters, and satisfied by {@link IBinding bindings}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class RenderOnlyPropertyException extends ApplicationRuntimeException
{
    private IComponent component;
    private String propertyName;

    public RenderOnlyPropertyException(IComponent component, String propertyName)
    {
        super(Tapestry.getString("RenderOnlyPropertyException.message", propertyName, component));

        this.component = component;
        this.propertyName = propertyName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public IComponent getComponent()
    {
        return component;
    }
}