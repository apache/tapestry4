package net.sf.tapestry.record;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.Tapestry;

/**
 *  Identifies a failure to rollback a page to a prior state.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class RollbackException extends ApplicationRuntimeException
{
    private String componentId;
    private String propertyName;
    private Object newValue;

    public RollbackException(
        IComponent component,
        String propertyName,
        Object newValue,
        Throwable rootCause)
    {
        super(
            Tapestry.getString(
                "RollbackException.message",
                propertyName,
                component.getExtendedId(),
                newValue),
            rootCause);

        this.componentId = component.getExtendedId();
        this.propertyName = propertyName;
        this.newValue = newValue;
    }

    public String getComponentId()
    {
        return componentId;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public Object getNewValue()
    {
        return newValue;
    }

}