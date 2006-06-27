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

import static org.easymock.EasyMock.expectLastCall;
import static org.testng.AssertJUnit.assertSame;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerInvoker;

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
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);

        IActionListener listener = newListener();
        ListenerInvoker invoker = newInvoker();

        Object[] parameters = new Object[0];

        InvokeListener component = (InvokeListener) newInstance(InvokeListener.class, new Object[]
        { "listener", listener, "parameters", parameters, "listenerInvoker", invoker, });

        cycle.setListenerParameters(parameters);
        invoker.invokeListener(listener, component, cycle);
        cycle.setListenerParameters(null);

        replay();

        component.render(writer, cycle);

        verify();
    }

    public void testEnsureParametersClearedAfterException()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = (IRequestCycle)newMock(IRequestCycle.class);

        IActionListener listener = newListener();
        
        ListenerInvoker invoker = newMock(ListenerInvoker.class);

        Throwable t = new RuntimeException();

        Object[] parameters = new Object[0];

        InvokeListener component = newInstance(InvokeListener.class, new Object[]
        { "listener", listener, "parameters", parameters, "listenerInvoker", invoker, });

        cycle.setListenerParameters(parameters);

        invoker.invokeListener(listener, component, cycle);
        expectLastCall().andThrow(t);

        cycle.setListenerParameters(null);

        replay();

        try
        {
            component.render(writer, cycle);
            unreachable();
        }
        catch (RuntimeException ex)
        {
            assertSame(t, ex);
        }

        verify();
    }
}
