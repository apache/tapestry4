package org.apache.tapestry.portlet;

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
}