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

import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.testng.annotations.Test;

/**
 * @author Howard M. Lewis Ship
 */
@Test
public class TestBeanBinding extends BaseComponentTestCase
{
    public void testCreate()
    {
        ValueConverter vc = newMock(ValueConverter.class);
        
        IComponent component = newComponent();
        
        IBeanProvider beanProvider = newMock(IBeanProvider.class);

        Location l = fabricateLocation(21);

        Object bean = new Object();

        expect(component.getBeans()).andReturn(beanProvider);

        expect(beanProvider.getBean("fred")).andReturn(bean);

        replay();

        BeanBinding binding = new BeanBinding("param", vc, l, component, "fred");

        assertSame(bean, binding.getObject());

        verify();

        assertSame(component, binding.getComponent());
        assertSame(l, binding.getLocation());
        assertEquals("param", binding.getDescription());
    }

}