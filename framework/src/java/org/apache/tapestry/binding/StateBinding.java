package org.apache.tapestry.binding;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.state.ApplicationStateManager;

/**
 * Binding used to efficiently query whether an application state object (visit, global and friends)
 * exists without actually creating it (or creating a session).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class StateBinding extends AbstractBinding
{
    private ApplicationStateManager _applicationStateManager;

    private String _objectName;

    public StateBinding(String description, ValueConverter valueConverter, Location location,
            ApplicationStateManager applicationStateManager, String objectName)
    {
        super(description, valueConverter, location);

        Defense.notNull(applicationStateManager, "applicationStateManager");
        Defense.notNull(objectName, "objectName");

        _applicationStateManager = applicationStateManager;
        _objectName = objectName;
    }

    public Object getObject()
    {
        try
        {
            boolean exists = _applicationStateManager.exists(_objectName);

            return exists ? Boolean.TRUE : Boolean.FALSE;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }
    }
}