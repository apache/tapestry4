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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.Locale;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.IPropertySource;

/**
 * Tests for {@link org.apache.tapestry.services.impl.LocalizedPropertySource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestLocalizedPropertySource extends BaseComponentTestCase
{
    public void testFound()
    {
        IPropertySource ps = newMock(IPropertySource.class);

        expect(ps.getPropertyValue("property-name_en")).andReturn(null);

        expect(ps.getPropertyValue("property-name")).andReturn("fred");

        replay();

        LocalizedPropertySource lps = new LocalizedPropertySource(ps);

        String result = lps.getPropertyValue("property-name", Locale.ENGLISH);

        assertEquals("fred", result);

        verify();
    }

    public void testNotFound()
    {
        IPropertySource ps = newMock(IPropertySource.class);

        expect(ps.getPropertyValue("property-name_fr")).andReturn(null);

        expect(ps.getPropertyValue("property-name")).andReturn(null);

        replay();

        LocalizedPropertySource lps = new LocalizedPropertySource(ps);

        assertNull(lps.getPropertyValue("property-name", Locale.FRENCH));

        verify();
    }
}