package org.apache.tapestry.portlet;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Test for {@link org.apache.tapestry.portlet.RenderService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestRenderService extends HiveMindTestCase
{
    public void testGetLinkUnsupported()
    {
        RenderService rs = new RenderService();

        try
        {
            rs.getLink(null, null);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Hm. Not actually the best message!
            assertEquals("Method getLink() is not supported for portlet requests.", ex.getMessage());
        }
    }

    public void testNormal() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        PortletRenderer renderer = (PortletRenderer) newMock(PortletRenderer.class);

        trainGetParameter(cyclec, cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cyclec, cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(requestc, request, PortletMode.VIEW);
        trainGetWindowState(requestc, request, WindowState.NORMAL);

        trainGetParameter(cyclec, cycle, ServiceConstants.PAGE, "Fred");

        renderer.renderPage(cycle, "Fred");

        replayControls();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);

        rs.service(cycle);

        verifyControls();
    }

    public void testPortletModeChanged() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        PortletRenderer renderer = (PortletRenderer) newMock(PortletRenderer.class);

        PortletPageResolver resolver = newResolver(cycle, "Barney");

        trainGetParameter(cyclec, cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cyclec, cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(requestc, request, PortletMode.EDIT);

        renderer.renderPage(cycle, "Barney");

        replayControls();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);
        rs.setPageResolver(resolver);

        rs.service(cycle);

        verifyControls();
    }

    public void testWindowStateChanged() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        PortletRenderer renderer = (PortletRenderer) newMock(PortletRenderer.class);

        PortletPageResolver resolver = newResolver(cycle, "Wilma");

        trainGetParameter(cyclec, cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cyclec, cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(requestc, request, PortletMode.VIEW);
        trainGetWindowState(requestc, request, WindowState.MAXIMIZED);

        renderer.renderPage(cycle, "Wilma");

        replayControls();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);
        rs.setPageResolver(resolver);

        rs.service(cycle);

        verifyControls();
    }

    private PortletPageResolver newResolver(IRequestCycle cycle, String pageName)
    {
        MockControl control = newControl(PortletPageResolver.class);
        PortletPageResolver resolver = (PortletPageResolver) control.getMock();

        resolver.getPageNameForRequest(cycle);
        control.setReturnValue(pageName);

        return resolver;
    }

    private void trainGetWindowState(MockControl control, PortletRequest request, WindowState state)
    {
        request.getWindowState();
        control.setReturnValue(state);
    }

    private void trainGetPortletMode(MockControl control, PortletRequest request, PortletMode mode)
    {
        request.getPortletMode();
        control.setReturnValue(mode);
    }

    private void trainGetParameter(MockControl control, IRequestCycle cycle, String name,
            String value)
    {
        cycle.getParameter(name);
        control.setReturnValue(value);
    }
}
