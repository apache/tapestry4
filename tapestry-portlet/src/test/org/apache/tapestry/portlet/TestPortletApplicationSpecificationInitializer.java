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

import java.net.URL;

import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.impl.ApplicationGlobalsImpl;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;
import org.testng.annotations.Test;

import javax.portlet.PortletConfig;

/**
 * Tests for {@link PortletApplicationSpecificationInitializer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletApplicationSpecificationInitializer extends BaseComponentTestCase
{
    private PortletConfig newConfig(String name)
    {
        PortletConfig config = newMock(PortletConfig.class);
        
        expect(config.getPortletName()).andReturn(name);
        
        return config;
    }

    private IApplicationSpecification newSpecification()
    {
        return newMock(IApplicationSpecification.class);
    }

    private ISpecificationParser newParser(Resource input, IApplicationSpecification specification)
    {
        ISpecificationParser parser = newMock(ISpecificationParser.class);

        expect(parser.parseApplicationSpecification(input)).andReturn(specification);
        
        return parser;
    }

    private ApplicationGlobals newGlobals()
    {
        return newMock(ApplicationGlobals.class);
    }

    public void testFoundInSubdir() throws Exception
    {
        PortletConfig config = newConfig("myportlet");
        
        WebContext context = newMock(WebContext.class);

        IApplicationSpecification specification = newSpecification();

        // Any arbitrary file will work here.

        URL fakeURL = getClass().getResource("hivemodule.xml");

        expect(context.getResource("/WEB-INF/myportlet/myportlet.application")).andReturn(fakeURL);
        
        Resource expectedResource = new WebContextResource(context,
                "/WEB-INF/myportlet/myportlet.application");

        ISpecificationParser parser = newParser(expectedResource, specification);

        ApplicationGlobals globals = newGlobals();

        globals.storeSpecification(specification);

        replay();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);
        init.setParser(parser);

        init.initialize(config);

        verify();
    }

    public void testFoundInRootDir() throws Exception
    {
        PortletConfig config = newConfig("myportlet");

        WebContext context = newMock(WebContext.class);

        IApplicationSpecification specification = newSpecification();

        // Any arbitrary file will work here.

        URL fakeURL = getClass().getResource("hivemodule.xml");

        expect(context.getResource("/WEB-INF/myportlet/myportlet.application")).andReturn(null);

        expect(context.getResource("/WEB-INF/myportlet.application")).andReturn(fakeURL);

        Resource expectedResource = new WebContextResource(context,
                "/WEB-INF/myportlet.application");

        ISpecificationParser parser = newParser(expectedResource, specification);

        ApplicationGlobals globals = newGlobals();

        globals.storeSpecification(specification);

        replay();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);
        init.setParser(parser);

        init.initialize(config);

        verify();
    }

    public void testNotFound() throws Exception
    {
        PortletConfig config = newConfig("myportlet");

        WebContext context = newMock(WebContext.class);

        expect(context.getResource("/WEB-INF/myportlet/myportlet.application")).andReturn(null);
        
        expect(context.getResource("/WEB-INF/myportlet.application")).andReturn(null);
        
        replay();

        ApplicationGlobals globals = new ApplicationGlobalsImpl();

        PortletApplicationSpecificationInitializer init = new PortletApplicationSpecificationInitializer();
        init.setContext(context);
        init.setGlobals(globals);

        init.initialize(config);

        verify();

        IApplicationSpecification spec = globals.getSpecification();

        assertEquals("myportlet", spec.getName());
        assertEquals(new WebContextResource(context, "/WEB-INF/myportlet.application"), spec
                .getSpecificationLocation());
    }
}
