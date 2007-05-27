package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

import java.util.List;

/**
 * Converts string values to String arrays and hands them off to a {@link ClientIdListBinding} instance.
 */
public class ClientIdListBindingFactory extends AbstractBindingFactory {

    public IBinding createBinding(IComponent root, String bindingDescription, String expression, Location location)
    {
        // convert to String array first
        List ids = (List) getValueConverter().coerceValue(expression, List.class);
        String[] clientIds = (String[]) ids.toArray(new String[ids.size()]);

        return new ClientIdListBinding(bindingDescription, getValueConverter(), location, root, clientIds);
    }
}
