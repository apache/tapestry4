package org.apache.tapestry.binding;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Binding that is specifcially used to bind against components of type {@link org.apache.tapestry.IDynamicInvoker}
 * and only for the parameter name "updateComponents".
 *
 * <p>
 *  Will take a parameter specification of <code>updateComponents="componentA, componentB"</code> and turn it
 * into the equivalent of a {@link List} containing the result of invoking {@link org.apache.tapestry.IComponent#getClientId()} on
 * each component specified.
 * </p>
 */
public class ClientIdListBinding extends AbstractBinding {

    private final IComponent _target;

    private final String[] _componentIds;
    private IComponent[] _targets;

    public ClientIdListBinding(String description, ValueConverter valueConverter,
                               Location location, IComponent component, String[] componentIds)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(componentIds, "componentIds");

        _target = component;
        _componentIds = componentIds;
        _targets = new IComponent[_componentIds.length];
    }

    public Object getObject()
    {
        try
        {
            List clientIds = new ArrayList(_componentIds.length);

            for (int i=0; i < _componentIds.length; i++)
            {
                if (_targets[i] == null)
                {
                    if (_target.getComponents().containsKey(_componentIds[i]))
                    {
                        _targets[i] = _target.getComponent(_componentIds[i]);
                    } else if (_target.getPage() != null) {

                        _targets[i] = _target.getPage().getComponent(_componentIds[i]);
                    }
                    
                    // if not found we're in trouble

                    if (_targets[i] == null)
                        throw new ApplicationRuntimeException(BindingMessages.unknownComponent(_target, _componentIds[i]), getLocation(), null);
                }
                
                clientIds.add(_targets[i].getClientId());
            }

            return clientIds;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }
    }

    public Object getComponent()
    {
        return _target;
    }

    public boolean isInvariant()
    {
        return false;
    }
}
