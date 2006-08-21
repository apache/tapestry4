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

package org.apache.tapestry.binding;

import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.InjectedValueProvider;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.binding.HiveMindBinding}and
 * {@link org.apache.tapestry.binding.HiveMindBindingFactory}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestHiveMindBinding extends BindingTestCase
{
    private IBinding newBinding(ValueConverter converter, InjectedValueProvider provider,
            String objectReference, Location location)
    {
        HiveMindBindingFactory factory = new HiveMindBindingFactory();

        factory.setValueConverter(converter);
        factory.setInjectedValueProvider(provider);

        return factory.createBinding(null, "binding description", objectReference, location);
    }

    public void testSuccess()
    {
        Object injectedValue = new Object();
        Location l = fabricateLocation(12);
        
        InjectedValueProvider provider = newMock(InjectedValueProvider.class);

        expect(provider.obtainValue("spring:bean", l)).andReturn(injectedValue);

        ValueConverter converter = newValueConverter();

        replay();

        IBinding binding = newBinding(converter, provider, "spring:bean", l);

        assertSame(injectedValue, binding.getObject());

        verify();
    }
}