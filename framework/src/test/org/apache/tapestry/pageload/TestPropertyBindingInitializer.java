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

package org.apache.tapestry.pageload;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.easymock.MockControl;

/**
 * Tests {@link org.apache.tapestry.pageload.PropertyBindingInitializer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPropertyBindingInitializer extends HiveMindTestCase
{
    public void testSuccess()
    {
        ComponentFixture c = new ComponentFixture();

        MockControl bc = newControl(IBinding.class);
        IBinding b = (IBinding) bc.getMock();

        replayControls();

        PropertyBindingInitializer i = new PropertyBindingInitializer(c, "stringProperty", b);

        verifyControls();

        b.getObject(String.class);
        bc.setReturnValue("fred");

        replayControls();

        i.pageDetached(null);

        verifyControls();

        assertEquals("fred", c.getStringProperty());
    }

    public void testFailure()
    {
        Location l = fabricateLocation(99);

        ComponentFixture c = new ComponentFixture();

        MockControl bc = newControl(IBinding.class);
        IBinding b = (IBinding) bc.getMock();

        Throwable npe = new NullPointerException();

        replayControls();

        PropertyBindingInitializer i = new PropertyBindingInitializer(c, "stringProperty", b);

        verifyControls();

        b.getObject(String.class);
        bc.setThrowable(npe);

        b.getLocation();
        bc.setReturnValue(l);

        replayControls();

        try
        {
            i.pageDetached(null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(l, ex.getLocation());
            assertSame(c, ex.getComponent());
            assertSame(npe, ex.getRootCause());
            assertExceptionSubstring(ex, "Unable to initialize property stringProperty");
        }

        verifyControls();

        assertNull(c.getStringProperty());
    }
}