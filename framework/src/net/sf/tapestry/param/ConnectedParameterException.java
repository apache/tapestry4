package net.sf.tapestry.param;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponent;

/**
 *  Identifies exceptions in connected parameters (parameters that
 *  are automatically assigned to component properties by the framework).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class ConnectedParameterException extends ApplicationRuntimeException
{
    private IComponent component;
    private String parameterName;
    private String propertyName;

    public ConnectedParameterException(
        String message,
        IComponent component,
        String parameterName,
        String propertyName)
    {
        this(message, component, parameterName, propertyName, null);
    }

    public ConnectedParameterException(
        String message,
        IComponent component,
        String parameterName,
        String propertyName,
        Throwable rootCause)
    {
        super(message, rootCause);

        this.component = component;
        this.parameterName = parameterName;
        this.propertyName = propertyName;
    }

    public IComponent getComponent()
    {
        return component;
    }

    public String getParameterName()
    {
        return parameterName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }
}