package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.engine.state.ApplicationStateManager;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class StateBindingFactory extends AbstractBindingFactory
{
    private ApplicationStateManager _applicationStateManager;

    public void setApplicationStateManager(ApplicationStateManager applicationStateManager)
    {
        _applicationStateManager = applicationStateManager;
    }

    public IBinding createBinding(IComponent root, String bindingDescription, String path,
            Location location)
    {
        return new StateBinding(bindingDescription, getValueConverter(), location,
                _applicationStateManager, path);
    }

}