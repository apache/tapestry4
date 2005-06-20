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

import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.binding.BindingTestCase;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

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
        Location l = newLocation();
        List validators = (List) newMock(List.class);
        ValueConverter vc = newValueConverter();

        MockControl control = newControl(ValidatorFactory.class);
        ValidatorFactory vf = (ValidatorFactory) control.getMock();

        vf.constructValidatorList("required");
        control.setReturnValue(validators);

        replayControls();

        ValidatorsBindingFactory factory = new ValidatorsBindingFactory();
        factory.setValueConverter(vc);
        factory.setValidatorFactory(vf);

        IBinding binding = factory.createBinding(null, "my desc", "required", l);

        assertSame(validators, binding.getObject());
        assertSame(l, binding.getLocation());
        assertEquals("my desc", binding.getDescription());

        verifyControls();
    }

    public void testFailure()
    {
        Throwable t = new RuntimeException("Boom!");
        Location l = newLocation();

        ValueConverter vc = newValueConverter();

        MockControl control = newControl(ValidatorFactory.class);
        ValidatorFactory vf = (ValidatorFactory) control.getMock();

        vf.constructValidatorList("required");
        control.setThrowable(t);

        replayControls();

        ValidatorsBindingFactory factory = new ValidatorsBindingFactory();
        factory.setValueConverter(vc);
        factory.setValidatorFactory(vf);

        try
        {
            factory.createBinding(null, "my desc", "required", l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Boom!", ex.getMessage());
            assertSame(t, ex.getRootCause());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }
}
