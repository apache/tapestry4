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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.ComponentBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestComponentBinding extends BindingTestCase
{
    public void testGetObject()
    {
        IComponent nested = newComponent();

        MockControl cc = newControl(IComponent.class);
        IComponent component = (IComponent) cc.getMock();

        component.getComponent("foo");
        cc.setReturnValue(nested);

        ValueConverter vc = newValueConverter();

        replayControls();

        ComponentBinding b = new ComponentBinding(component, "foo", "param", vc,
                fabricateLocation(2));

        assertSame(component, b.getComponent());
        assertSame(nested, b.getObject());

        verifyControls();
    }
}