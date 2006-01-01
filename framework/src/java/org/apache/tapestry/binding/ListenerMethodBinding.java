// Copyright 2004, 2005, 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ListenerMethodBinding extends AbstractBinding implements IActionListener
{
    private final IComponent _component;

    private final String _methodName;

    // We have to defer obtaining the listener until after the page is loaded, because it is
    // (currently) reliant on the page's engine property to gain access to the
    // ListenerMapSource. I'd prefer it if this was a final field, resolved by the constructor,
    // but that will involve injecting the ListenerMapSource into AbstractComponent.

    private IActionListener _listener;

    public ListenerMethodBinding(String description, ValueConverter valueConverter, Location location,
            IComponent component, String methodName)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(methodName, "methodName");

        _component = component;
        _methodName = methodName;
    }

    public Object getComponent()
    {
        return _component;
    }

    /**
     * Returns this binding object; the binding object delegates to the actual listener. This allows
     * us to intercept errors and report the location of the binding.
     */
    public Object getObject()
    {
        return this;
    }

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    {
        try
        {
            if (_listener == null)
                _listener = _component.getListeners().getListener(_methodName);

            _listener.actionTriggered(component, cycle);
        }
        catch (PageRedirectException ex)
        {
            throw ex;
        }
        catch (RedirectException ex)
        {
            throw ex;
        }
        catch (RuntimeException ex)
        {
            throw new BindingException(BindingMessages.listenerMethodFailure(
                    _component,
                    _methodName,
                    ex), _component, getLocation(), this, ex);
        }
    }

    protected void extendDescription(StringBuffer buffer)
    {
        buffer.append(", component=");
        buffer.append(_component.getExtendedId());
        buffer.append(", methodName=");
        buffer.append(_methodName);
    }

}