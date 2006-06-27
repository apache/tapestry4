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

package org.apache.tapestry.form.validator;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingTestCase;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Tests for {@link org.apache.tapestry.form.validator.ValidatorsBinding} and
 * {@link org.apache.tapestry.form.validator.ValidatorsBindingFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidatorsBinding extends BindingTestCase
{
    public void testSuccess()
    {
        IComponent component = newComponent();
        Location l = newLocation();
        List validators = newMock(List.class);
        ValueConverter vc = newValueConverter();
        
        ValidatorFactory vf = newMock(ValidatorFactory.class);

        expect(vf.constructValidatorList(component, "required")).andReturn(validators);

        replay();

        ValidatorsBindingFactory factory = new ValidatorsBindingFactory();
        factory.setValueConverter(vc);
        factory.setValidatorFactory(vf);

        IBinding binding = factory.createBinding(component, "my desc", "required", l);

        assertSame(validators, binding.getObject());
        assertSame(l, binding.getLocation());
        assertEquals("my desc", binding.getDescription());

        verify();
    }

    public void testFailure()
    {
        IComponent component = newComponent();
        Throwable t = new RuntimeException("Boom!");
        Location l = newLocation();

        ValueConverter vc = newValueConverter();

        ValidatorFactory vf = newMock(ValidatorFactory.class);

        expect(vf.constructValidatorList(component, "required")).andThrow(t);

        replay();

        ValidatorsBindingFactory factory = new ValidatorsBindingFactory();
        factory.setValueConverter(vc);
        factory.setValidatorFactory(vf);

        try
        {
            factory.createBinding(component, "my desc", "required", l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Boom!", ex.getMessage());
            assertSame(t, ex.getRootCause());
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}
