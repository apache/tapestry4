// Copyright 2004, 2005 The Apache Software Foundation
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
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;

import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.services.impl.ApplicationSpecificationInitializer}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestApplicationSpecificationInitializer extends BaseComponentTestCase
{
    public void testOnClasspath() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        Resource appSpecResource = new ClasspathResource(cr, "/foo/OnClasspath.application");

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        Log log = newMock(Log.class);

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();
        
        ServletConfig config = newMock(ServletConfig.class);

        trainForServletInit(config);

        expect(config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM))
        .andReturn(appSpecResource.getPath());

        IApplicationSpecification as = new ApplicationSpecification();
        
        ISpecificationParser parser = newMock(ISpecificationParser.class);

        i.setParser(parser);

        expect(parser.parseApplicationSpecification(appSpecResource)).andReturn(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replay();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        assertNotNull(ag.getActivator());
        assertSame(as, ag.getSpecification());

        verify();
    }

    private void trainForServletInit(ServletConfig config)
    {
        ServletContext context = newMock(ServletContext.class);

        expect(config.getServletContext()).andReturn(context);

        expect(config.getServletName()).andReturn("test");

        context.log("test: init");
    }

    public void testInAppContextFolder() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();
        
        ServletContext context = newMock(ServletContext.class);
        Log log = newLog();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();
        
        ServletConfig config = newMock(ServletConfig.class);

        trainForServletInit(config);

        expect(config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM))
        .andReturn(null);

        expect(config.getServletContext()).andReturn(context);

        expect(config.getServletName()).andReturn("fred");

        expect(log.isDebugEnabled()).andReturn(true);

        Resource r = new ContextResource(context, "/WEB-INF/fred/fred.application");

        log.debug("Checking for existence of " + r);

        expect(context.getResource(r.getPath()))
        .andReturn(new URL("file:/context" + r.getPath()));

        log.debug("Found " + r);

        IApplicationSpecification as = new ApplicationSpecification();
        
        ISpecificationParser parser = newMock(ISpecificationParser.class);

        i.setParser(parser);

        expect(parser.parseApplicationSpecification(r)).andReturn(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replay();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verify();
    }

    public void testInWebInfFolder() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        ServletContext context = newMock(ServletContext.class);
        Log log = newLog();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        ServletConfig config = newMock(ServletConfig.class);

        trainForServletInit(config);

        expect(config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM))
        .andReturn(null);

        expect(config.getServletContext()).andReturn(context);

        expect(config.getServletName()).andReturn("barney");

        expect(log.isDebugEnabled()).andReturn(false);

        Resource r = new ContextResource(context, "/WEB-INF/barney.application");

        expect(context.getResource("/WEB-INF/barney/barney.application")).andReturn(null);

        expect(log.isDebugEnabled()).andReturn(false);

        expect(context.getResource(r.getPath()))
        .andReturn(new URL("file:/context" + r.getPath()));

        log.debug("Found " + r);

        IApplicationSpecification as = new ApplicationSpecification();
        
        ISpecificationParser parser = newMock(ISpecificationParser.class);

        i.setParser(parser);

        expect(parser.parseApplicationSpecification(r)).andReturn(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replay();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verify();
    }

    public void testNoAppSpec() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        ServletContext context = newMock(ServletContext.class);
        Log log = newLog();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        ServletConfig config = newMock(ServletConfig.class);

        trainForServletInit(config);

        expect(config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM)).andReturn(null);

        expect(config.getServletContext()).andReturn(context);

        expect(config.getServletName()).andReturn("wilma");

        expect(log.isDebugEnabled()).andReturn(false);

        expect(context.getResource("/WEB-INF/wilma/wilma.application")).andReturn(null);

        expect(log.isDebugEnabled()).andReturn(false);

        expect(context.getResource("/WEB-INF/wilma.application")).andReturn(null);

        expect(config.getServletName()).andReturn("wilma");

        log.debug("Could not find an application specification for application servlet wilma.");

        expect(config.getServletName()).andReturn("wilma");

        expect(config.getServletContext()).andReturn(context);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replay();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verify();

        IApplicationSpecification as = ag.getSpecification();

        assertEquals("wilma", as.getName());
        assertEquals(new ContextResource(context, "/WEB-INF/wilma.application"), as
                .getSpecificationLocation());
    }

    /**
     * Test within the Registry, to ensure the module deployment descriptor is well configured.
     */
    public void testIntegration() throws Exception
    {
        ServletContext context = newMock(ServletContext.class);

        HttpServlet servlet = new ServletFixture();
        
        ServletConfig config = newMock(ServletConfig.class);

        expect(config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM))
        .andReturn(null);

        expect(config.getServletContext()).andReturn(context).times(2);

        expect(config.getServletName()).andReturn("dino").times(2);

        context.log("dino: init");

        expect(context.getResource("/WEB-INF/dino/dino.application"))
        .andReturn(getClass().getResource("ParseApp.application")).times(2);

        replay();

        servlet.init(config);

        Registry registry = RegistryBuilder.constructDefaultRegistry();

        ApplicationInitializer ai = (ApplicationInitializer) registry.getService(
                "tapestry.init.ApplicationSpecificationInitializer",
                ApplicationInitializer.class);

        ai.initialize(servlet);

        ApplicationGlobals ag = (ApplicationGlobals) registry.getService(
                "tapestry.globals.ApplicationGlobals",
                ApplicationGlobals.class);

        assertEquals("ParseApp", ag.getSpecification().getName());

        verify();

    }
}