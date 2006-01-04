// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.link.DirectLink;
import org.apache.tapestry.listener.ListenerInvoker;

/**
 * Invokes a listener method, passing listener parameters. This is used when a page or component
 * needs some setup logic that can be best accomplished in Java code.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class InvokeListener extends AbstractComponent
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object[] parameters = DirectLink.constructServiceParameters(getParameters());

        try
        {
            cycle.setListenerParameters(parameters);

            getListenerInvoker().invokeListener(getListener(), this, cycle);
        }
        finally
        {
            cycle.setListenerParameters(null);
        }
    }

    /** Parameter */
    public abstract IActionListener getListener();

    /** Parameter */
    public abstract Object getParameters();

    /** Injected */
    public abstract ListenerInvoker getListenerInvoker();
}
