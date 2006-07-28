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

package org.apache.tapestry.portlet;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.List;

import org.testng.annotations.Test;

import javax.portlet.PortletConfig;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebActivator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletWebActivator extends BasePortletWebTestCase
{
    public void testGetActivatorName()
    {
        PortletConfig config = newMock(PortletConfig.class);

        expect(config.getPortletName()).andReturn("portlet");
        
        replay();
        
        PortletWebActivator pwa = new PortletWebActivator(config);
        
        assertEquals("portlet", pwa.getActivatorName());

        verify();
    }

    public void testGetInitParameterNames()
    {
        PortletConfig config = newMock(PortletConfig.class);

        expect(config.getInitParameterNames()).andReturn(newEnumeration());
        
        replay();

        PortletWebActivator pwa = new PortletWebActivator(config);

        List l = pwa.getInitParameterNames();

        checkList(l);

        verify();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";
        
        PortletConfig config = newMock(PortletConfig.class);

        expect(config.getInitParameter("artist")).andReturn(value);
        
        replay();

        PortletWebActivator pwa = new PortletWebActivator(config);

        assertSame(value, pwa.getInitParameterValue("artist"));

        verify();
    }
}
