// Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.listener.ListenerMap;
import org.easymock.MockControl;

/**
 * Test for {@link org.apache.tapestry.binding.ListenerMethodBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestListenerMethodBinding extends BindingTestCase
{
    public void testGetObject()
    {
        MockControl cc = newControl(IComponent.class);
        IComponent component = (IComponent) cc.getMock();

        MockControl lmc = newControl(ListenerMap.class);
        ListenerMap lm = (ListenerMap) lmc.getMock();

        Object listener = "LISTENER";

        component.getListeners();
        cc.setReturnValue(lm);

        lm.getListener("foo");
        lmc.setReturnValue(listener);

        ValueConverter vc = newValueConverter();

        replayControls();

        ListenerMethodBinding b = new ListenerMethodBinding(component, "foo", "param", vc,
                fabricateLocation(33));

        assertSame(listener, b.getObject());
        assertSame(component, b.getComponent());

        verifyControls();
    }
}