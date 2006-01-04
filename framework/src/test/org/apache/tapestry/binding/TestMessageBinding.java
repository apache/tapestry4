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

package org.apache.tapestry.binding;

import org.apache.hivemind.Messages;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.MessageBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestMessageBinding extends BindingTestCase
{

    public void testCreate()
    {
        IComponent component = (IComponent) newMock(IComponent.class);
        ValueConverter vc = newValueConverter();

        replayControls();

        MessageBinding b = new MessageBinding("param", vc, fabricateLocation(12), component, "key");

        assertSame(component, b.getComponent());
        assertEquals("key", b.getKey());

        verifyControls();
    }

    public void testToString()
    {
        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();
        ValueConverter vc = newValueConverter();

        component.getExtendedId();
        control.setReturnValue("Foo/bar.baz");

        replayControls();

        MessageBinding b = new MessageBinding("param", vc, fabricateLocation(12), component, "key");

        assertEquals("StringBinding[Foo/bar.baz key]", b.toString());

        verifyControls();
    }

    public void testGetObject()
    {
        MockControl mc = newControl(Messages.class);
        Messages m = (Messages) mc.getMock();

        MockControl control = newControl(IComponent.class);
        IComponent component = (IComponent) control.getMock();

        ValueConverter vc = newValueConverter();

        component.getMessages();
        control.setReturnValue(m);

        m.getMessage("key");
        mc.setReturnValue("value");

        replayControls();
        MessageBinding b = new MessageBinding("param", vc, fabricateLocation(12), component, "key");

        assertEquals("value", b.getObject());

        verifyControls();
    }
}