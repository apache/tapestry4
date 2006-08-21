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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Registry;
import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Tests for {@link org.apache.tapestry.portlet.ApplicationPortlet}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestApplicationPortlet extends BaseComponentTestCase
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
        return newMock(PortletApplicationInitializer.class);
    }

    private ActionRequestServicer newActionRequestServicer()
    {
        return newMock(ActionRequestServicer.class);
    }

    private RenderRequestServicer newRenderRequestServicer()
    {
        return newMock(RenderRequestServicer.class);
    }

    private Registry newRegistry(PortletApplicationInitializer initializer,
            ActionRequestServicer actionRequestServicer, RenderRequestServicer renderRequestServicer)
    {
        Registry registry = newMock(Registry.class);
        checkOrder(registry, false);
        
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
        return newMock(PortletConfig.class);
    }

    private ActionRequest newActionRequest()
    {
        return newMock(ActionRequest.class);
    }

    private ActionResponse newActionResponse()
    {
        return newMock(ActionResponse.class);
    }

    private RenderRequest newRenderRequest()
    {
        return newMock(RenderRequest.class);
    }

    private RenderResponse newRenderResponse()
    {
        return newMock(RenderResponse.class);
    }

    public void testParseOptionalDescriptors() throws Exception
    {
        PortletConfig config = newMock(PortletConfig.class);
        PortletContext context = newMock(PortletContext.class);

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

        replay();

        ApplicationPortlet ap = new ApplicationPortlet();

        ap.init(config);
        
        assertNotNull(ap._registry);
        assertNotNull(ap._actionRequestServicer);
        assertNotNull(ap._renderRequestServicer);

        assertEquals("parsed", ap._registry.expandSymbols("${module-portlet}", null));
        assertEquals("parsed", ap._registry.expandSymbols("${module-plain}", null));
        
        verify();
    }

    public void testInitAndDestroy() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();

        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);
        PortletConfig config = newConfig();

        initializer.initialize(config);

        replay();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verify();

        registry.shutdown();

        replay();

        portlet.destroy();

        verify();
    }

    public void testProcessAction() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();
        
        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);
        
        PortletConfig config = newConfig();
        
        initializer.initialize(config);

        replay();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verify();

        ActionRequest request = newActionRequest();
        ActionResponse response = newActionResponse();

        registry.setupThread();
        
        actionRequestServicer.service(request, response);

        registry.cleanupThread();

        replay();

        portlet.processAction(request, response);

        verify();
    }

    public void testProcessRender() throws Exception
    {
        PortletApplicationInitializer initializer = newInitializer();
        ActionRequestServicer actionRequestServicer = newActionRequestServicer();
        RenderRequestServicer renderRequestServicer = newRenderRequestServicer();

        Registry registry = newRegistry(initializer, actionRequestServicer, renderRequestServicer);
        
        PortletConfig config = newConfig();

        initializer.initialize(config);

        replay();

        ApplicationPortletFixture portlet = new ApplicationPortletFixture(registry);

        portlet.init(config);

        verify();

        RenderRequest request = newRenderRequest();
        RenderResponse response = newRenderResponse();

        registry.setupThread();
        
        renderRequestServicer.service(request, response);

        registry.cleanupThread();

        replay();

        portlet.render(request, response);

        verify();
    }
}