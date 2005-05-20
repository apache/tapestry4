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

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerInvoker;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.components.InvokeListener}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInvokeListener extends BaseComponentTestCase
{
    private IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
    }

    private ListenerInvoker newInvoker()
    {
        return (ListenerInvoker) newMock(ListenerInvoker.class);
    }

    public void testSuccess()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        IActionListener listener = newListener();
        ListenerInvoker invoker = newInvoker();

        Object[] parameters = new Object[0];

        InvokeListener component = (InvokeListener) newInstance(InvokeListener.class, new Object[]
        { "listener", listener, "parameters", parameters, "listenerInvoker", invoker, });

        cycle.setListenerParameters(parameters);
        invoker.invokeListener(listener, component, cycle);
        cycle.setListenerParameters(null);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testEnsureParametersClearedAfterException()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        IActionListener listener = newListener();

        MockControl invokerc = newControl(ListenerInvoker.class);
        ListenerInvoker invoker = (ListenerInvoker) invokerc.getMock();

        Throwable t = new RuntimeException();

        Object[] parameters = new Object[0];

        InvokeListener component = (InvokeListener) newInstance(InvokeListener.class, new Object[]
        { "listener", listener, "parameters", parameters, "listenerInvoker", invoker, });

        cycle.setListenerParameters(parameters);

        invoker.invokeListener(listener, component, cycle);
        invokerc.setThrowable(t);

        cycle.setListenerParameters(null);

        replayControls();

        try
        {
            component.render(writer, cycle);
            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertSame(t, ex);
        }

        verifyControls();
    }
}
