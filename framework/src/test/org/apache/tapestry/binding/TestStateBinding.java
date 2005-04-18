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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.StateBinding}and
 * {@link org.apache.tapestry.binding.StateBindingFactory}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestStateBinding extends HiveMindTestCase
{

    private ValueConverter newValueConverter()
    {
        return (ValueConverter) newMock(ValueConverter.class);
    }

    private IBinding newBinding(String objectName, ValueConverter vc, ApplicationStateManager asm,
            Location location)
    {
        StateBindingFactory f = new StateBindingFactory();
        f.setValueConverter(vc);
        f.setApplicationStateManager(asm);

        return f.createBinding(null, "binding description", objectName, location);
    }

    public void testSuccess()
    {
        MockControl asmc = newControl(ApplicationStateManager.class);
        ApplicationStateManager asm = (ApplicationStateManager) asmc.getMock();

        asm.exists("fred");
        asmc.setReturnValue(true);

        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding b = newBinding("fred", vc, asm, null);

        assertEquals(Boolean.TRUE, b.getObject());

        verifyControls();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(22);

        Throwable t = new RuntimeException("Nested exception.");

        MockControl asmc = newControl(ApplicationStateManager.class);
        ApplicationStateManager asm = (ApplicationStateManager) asmc.getMock();

        asm.exists("fred");
        asmc.setThrowable(t);

        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding b = newBinding("fred", vc, asm, l);

        
        try
        {
            b.getObject();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Nested exception.", ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(t, ex.getRootCause());
        }

        verifyControls();
    }

}