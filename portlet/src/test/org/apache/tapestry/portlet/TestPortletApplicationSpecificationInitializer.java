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

import java.net.URL;

import javax.portlet.PortletConfig;

import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.impl.ApplicationGlobalsImpl;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;
import org.easymock.MockControl;

/**
 * Tests for {@link PortletApplicationSpecificationInitializer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletApplicationSpecificationInitializer extends HiveMindTestCase
{
    private PortletConfig newConfig(String name)
    {
        MockControl control = newControl(PortletConfig.class);
        PortletConfig config = (PortletConfig) control.getMock();

        config.getPortletName();
        control.setReturnValue(name);

        return config;
    }

    private IApplicationSpecification newSpecification()
    {
        return (IApplicationSpecification) newMock(IApplicationSpecification.class);
    }

    private ISpecificationParser newParser(Resource input, IApplicationSpecification specification)
    {
        MockControl control = newControl(ISpecificationParser.class);
        ISpecificationParser parser = (ISpecificationParser) control.getMock();

        parser.parseApplicationSpecification(input);
        control.setReturnValue(specification);

        return parser;
    }

    private ApplicationGlobals newGlobals()
    {
        return (ApplicationGlobals) newMock(ApplicationGlobals.class);
    }

    public void testFoundInSubdir() throws Exception
    {
        PortletConfig config = newConfig("myportlet");

        MockControl contextc = newControl(WebContext.class);
        WebContext context = (WebContext) contextc.getMock();

        IApplicationSpecification specification = newSpecification();

        // Any arbitrary file will work here.

        URL fakeURL = getClass().getResource("hivemodule.xml");

        context.getResource("/WEB-INF/myportlet/myportlet.application");
        contextc.setReturnValue(fakeURL);

        Resource expectedResource = new WebContextResource(context,
                "/WEB-INF/myportlet/myportlet.application");

        ISpecificationParser parser = newParser(expectedResource, specification);

        ApplicationGlobals globals = newGlobals();

        globals.storeSpecification(specification);

        replayControls();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);
        init.setParser(parser);

        init.initialize(config);

        verifyControls();
    }

    public void testFoundInRootDir() throws Exception
    {
        PortletConfig config = newConfig("myportlet");

        MockControl contextc = newControl(WebContext.class);
        WebContext context = (WebContext) contextc.getMock();

        IApplicationSpecification specification = newSpecification();

        // Any arbitrary file will work here.

        URL fakeURL = getClass().getResource("hivemodule.xml");

        context.getResource("/WEB-INF/myportlet/myportlet.application");
        contextc.setReturnValue(null);

        context.getResource("/WEB-INF/myportlet.application");
        contextc.setReturnValue(fakeURL);

        Resource expectedResource = new WebContextResource(context,
                "/WEB-INF/myportlet.application");

        ISpecificationParser parser = newParser(expectedResource, specification);

        ApplicationGlobals globals = newGlobals();

        globals.storeSpecification(specification);

        replayControls();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);
        init.setParser(parser);

        init.initialize(config);

        verifyControls();
    }

    public void testNotFound() throws Exception
    {
        PortletConfig config = newConfig("myportlet");

        MockControl contextc = newControl(WebContext.class);
        WebContext context = (WebContext) contextc.getMock();

        context.getResource("/WEB-INF/myportlet/myportlet.application");
        contextc.setReturnValue(null);

        context.getResource("/WEB-INF/myportlet.application");
        contextc.setReturnValue(null);

        replayControls();

        ApplicationGlobals globals = new ApplicationGlobalsImpl();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);

        init.initialize(config);

        verifyControls();

        IApplicationSpecification spec = globals.getSpecification();

        assertEquals("myportlet", spec.getName());
        assertEquals(new WebContextResource(context, "/WEB-INF/myportlet.application"), spec
                .getSpecificationLocation());
    }
}