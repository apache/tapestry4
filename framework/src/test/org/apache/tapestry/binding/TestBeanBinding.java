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

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 */
public class TestBeanBinding extends HiveMindTestCase
{
    public void testCreate()
    {
        ValueConverter vc = (ValueConverter) newMock(ValueConverter.class);

        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        MockControl beanProviderc = newControl(IBeanProvider.class);
        IBeanProvider beanProvider = (IBeanProvider) beanProviderc.getMock();

        Location l = fabricateLocation(21);

        Object bean = new Object();

        component.getBeans();
        componentc.setReturnValue(beanProvider);

        beanProvider.getBean("fred");
        beanProviderc.setReturnValue(bean);

        replayControls();

        BeanBinding binding = new BeanBinding("param", vc, l, component, "fred");

        assertSame(bean, binding.getObject());

        verifyControls();

        assertSame(component, binding.getComponent());
        assertSame(l, binding.getLocation());
        assertEquals("param", binding.getDescription());
    }

}