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

import java.util.List;

import javax.portlet.PortletConfig;

import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebActivator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletWebActivator extends BasePortletWebTestCase
{
    public void testGetActivatorName()
    {
        MockControl control = newControl(PortletConfig.class);
        PortletConfig config = (PortletConfig) control.getMock();

        config.getPortletName();
        control.setReturnValue("portlet");

        replayControls();

        PortletWebActivator pwa = new PortletWebActivator(config);

        assertEquals("portlet", pwa.getActivatorName());

        verifyControls();
    }

    public void testGetInitParameterNames()
    {
        MockControl control = newControl(PortletConfig.class);
        PortletConfig config = (PortletConfig) control.getMock();

        config.getInitParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        PortletWebActivator pwa = new PortletWebActivator(config);

        List l = pwa.getInitParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";

        MockControl control = newControl(PortletConfig.class);
        PortletConfig config = (PortletConfig) control.getMock();

        config.getInitParameter("artist");
        control.setReturnValue(value);

        replayControls();

        PortletWebActivator pwa = new PortletWebActivator(config);

        assertSame(value, pwa.getInitParameterValue("artist"));

        verifyControls();
    }
}