// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.callback;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 * Simple callback for re-invoking a {@link IDirect}&nbsp;trigger.
 * 
 * @author Howard Lewis Ship
 * @since 0.2.9
 */

public class DirectCallback implements ICallback
{
    /**
     * @since 2.0.4
     */

    private static final long serialVersionUID = -8888847655917503471L;

    private String _pageName;

    private String _componentIdPath;

    private Object[] _parameters;

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("DirectCallback[");

        buffer.append(_pageName);
        buffer.append('/');
        buffer.append(_componentIdPath);

        if (_parameters != null)
        {
            for (int i = 0; i < _parameters.length; i++)
            {
                buffer.append(i == 0 ? " " : ", ");
                buffer.append(_parameters[i]);
            }
        }

        buffer.append(']');

        return buffer.toString();

    }

    /**
     * Creates a new DirectCallback for the component. The parameters (which may be null) is
     * retained, not copied.
     */

    public DirectCallback(IDirect component, Object[] parameters)
    {
        Defense.notNull(component, "component");

        _pageName = component.getPage().getPageName();
        _componentIdPath = component.getIdPath();
        _parameters = parameters;
    }

    /**
     * Locates the {@link IDirect}component that was previously identified (and whose page and id
     * path were stored). Invokes {@link IRequestCycle#setListenerParameters(Object[])(Object[])}to
     * restore the service parameters, then invokes {@link IDirect#trigger(IRequestCycle)}on the
     * component.
     */

    public void performCallback(IRequestCycle cycle)
    {
        IPage page = cycle.getPage(_pageName);
        IComponent component = page.getNestedComponent(_componentIdPath);
        IDirect direct = null;

        try
        {
            direct = (IDirect) component;
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(CallbackMessages.componentNotDirect(component),
                    component, null, ex);
        }

        cycle.setListenerParameters(_parameters);
        direct.trigger(cycle);
    }
}