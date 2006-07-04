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

import static org.easymock.EasyMock.*;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.hivemind.Registry;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.portlet.ApplicationPortlet}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestApplicationPortlet extends HiveMindTestCase
{
    public static class ApplicationPortletFixture extends ApplicationPortlet
    {
        private final Registry _constructRegistry;

        public ApplicationPortletFixture(Registry registry)
        {
            _constructRegistry = registry;
        }

        protected Registry constructRegistry(PortletConfig config)
        {
            return _constructRegistry;
        }
    }

    private PortletApplicationInitializer newInitializer()
    {
        return (PortletApplicationInitializer) newMock(PortletApplicationInitializer.class);
    }

    private ActionRequestServicer newActionRequestServicer()
    {
        return (ActionRequestServicer) newMock(ActionRequestServicer.class);
    }

    private RenderRequestServicer newRenderRequestServicer()
    {
        return (RenderRequestServicer) newMock(RenderRequestServicer.class);
    }

    private Registry newRegistry(PortletApplicationInitializer initializer,
            ActionRequestServicer actionRequestServicer, RenderRequestServicer renderRequestServicer)
    {
        Registry registry = (Registry) newMock(Registry.class);

        expect(registry.getService(
                "tapestry.portlet.PortletApplicationInitializer",
                PortletApplicationInitializer.class)).andReturn(initializer);

        expect(registry.getService("tapestry.portlet.ActionRequestServicer", ActionRequestServicer.class))
        .andReturn(actionRequestServicer);

        expect(registry.getService("tapestry.portlet.RenderRequestServicer", RenderRequestServicer.class))
        .andReturn(renderRequestServicer);

        return registry;
    }

    private PortletConfig newConfig()
    {
        return (PortletConfig) newMock(PortletConfig.class);
    }

    private ActionRequest newActionRequest()
    {
        return (ActionRequest) newMock(ActionRequest.class);
    }

    private ActionResponse newActionResponse()
    {
        return (ActionResponse) newMock(ActionResponse.class);
    }

    private RenderRequest newRenderRequest()
    {
        return (RenderRequest) newMock(RenderRequest.class);
    }

    private RenderResponse newRenderResponse()
    {
        return (RenderResponse) newMock(RenderResponse.class);
    }

    public void testParseOptionalDescriptors() throws Exception
    {
        PortletConfig config = (PortletConfig)newMock(PortletConfig.class);
        PortletContext context = (PortletContext)newMock(PortletContext.class);

        checkOrder(config, false);
        checkOrder(context, false);
        
        expect(config.getPortletName()).andReturn("myportlet").anyTimes();
        // Called once in ApplicationPortlet code,
        // then inside PortletWebContextInitializer

        expect(config.getPortletContext()).andReturn(context).anyTimes();
        
        expect(context.getResource("/WEB-INF/myportlet/hivemodule.xml"))
        .andReturn(getClass().getResource("hivemodule-portlet.xml")).anyTimes();
        
        expect(context.getResource("/WEB-INF/hivemodule.xml"))
        .andReturn(getClass().getResource("hivemodule.xml")).anyTimes();
        
        expect(context.getResource("/WEB-INF/myportlet/myportlet.application"))
        .andReturn(null);

        expect(context.getResource("/WEB-INF/myportlet.application")).andReturn(null);

        replayControls();

        ApplicationPortlet ap = new ApplicationPortlet();

        ap.init(config);

        assertNotNull(ap._registry);
        assertNotNull(ap._actionRequestServicer);
        assertNotNull(ap._renderRequestServicer);

        assertEquals("parsed", ap._registry.expandSymbols("${module-portlet}", null));
        assertEquals("parsed", ap._registry.expandSymbols("${module-plain}", null));
    }

    public void testInitAndDestroy() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();

        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);
        PortletConfig config = newConfig();

        initializer.initialize(config);

        replayControls();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verifyControls();

        registry.shutdown();

        replayControls();

        portlet.destroy();

        verifyControls();
    }

    public void testProcessAction() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();

        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);

        PortletConfig config = newConfig();

        initializer.initialize(config);

        replayControls();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verifyControls();

        ActionRequest request = newActionRequest();
        ActionResponse response = newActionResponse();

        registry.setupThread();
        
        actionRequestServicer.service(request, response);

        registry.cleanupThread();

        replayControls();

        portlet.processAction(request, response);

        verifyControls();
    }

    public void testProcessRender() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();

        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);
        
        PortletConfig config = newConfig();

        initializer.initialize(config);

        replayControls();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verifyControls();

        RenderRequest request = newRenderRequest();
        RenderResponse response = newRenderResponse();

        registry.setupThread();
        
        renderRequestServicer.service(request, response);

        registry.cleanupThread();

        replayControls();

        portlet.render(request, response);

        verifyControls();
    }
}