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

package org.apache.tapestry.services.impl;

import java.util.Locale;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.IPropertySource;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.LocalizedPropertySource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestLocalizedPropertySource extends HiveMindTestCase
{
    public void testFound()
    {
        MockControl control = newControl(IPropertySource.class);
        IPropertySource ps = (IPropertySource) control.getMock();

        ps.getPropertyValue("property-name_en");
        control.setReturnValue(null);

        ps.getPropertyValue("property-name");
        control.setReturnValue("fred");

        replayControls();

        LocalizedPropertySource lps = new LocalizedPropertySource(ps);

        String result = lps.getPropertyValue("property-name", Locale.ENGLISH);

        assertEquals("fred", result);

        verifyControls();
    }

    public void testNotFound()
    {
        MockControl control = newControl(IPropertySource.class);
        IPropertySource ps = (IPropertySource) control.getMock();

        ps.getPropertyValue("property-name_fr");
        control.setReturnValue(null);

        ps.getPropertyValue("property-name");
        control.setReturnValue(null);

        replayControls();

        LocalizedPropertySource lps = new LocalizedPropertySource(ps);

        assertNull(lps.getPropertyValue("property-name", Locale.FRENCH));

        verifyControls();
    }
}