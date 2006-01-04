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

package org.apache.tapestry.bean;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.bean.LightweightBeanInitializer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestLightweightBeanInitializer extends HiveMindTestCase
{
    public void testSuccess()
    {
        String initializer = "required,minLength=10";
        TargetBean bean = new TargetBean();

        assertEquals(false, bean.isRequired());
        assertEquals(0, bean.getMinLength());

        IBeanInitializer bi = new LightweightBeanInitializer(initializer);

        assertEquals(initializer, bi.getPropertyName());

        bi.setBeanProperty(null, bean);

        assertEquals(true, bean.isRequired());
        assertEquals(10, bean.getMinLength());
    }

    public void testFailure()
    {
        Location l = newLocation();

        IBeanInitializer bi = new LightweightBeanInitializer("zip=zap");
        bi.setLocation(l);

        TargetBean bean = new TargetBean();

        try
        {
            bi.setBeanProperty(null, bean);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Class org.apache.tapestry.bean.TargetBean "
                    + "does not contain a property named 'zip'.", ex.getMessage());
            assertSame(l, ex.getLocation());
        }
    }
}
