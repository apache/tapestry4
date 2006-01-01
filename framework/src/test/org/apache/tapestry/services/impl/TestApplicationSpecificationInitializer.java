// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.easymock.MockControl;

/**
 * Tests for the {@link org.apache.tapestry.services.impl.ApplicationSpecificationInitializer}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestApplicationSpecificationInitializer extends HiveMindTestCase
{
    public void testOnClasspath() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        Resource appSpecResource = new ClasspathResource(cr, "/foo/OnClasspath.application");

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        Log log = (Log) newMock(Log.class);

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        MockControl configControl = newControl(ServletConfig.class);
        ServletConfig config = (ServletConfig) configControl.getMock();

        trainForServletInit(configControl, config);

        config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM);
        configControl.setReturnValue(appSpecResource.getPath());

        IApplicationSpecification as = new ApplicationSpecification();

        MockControl parserControl = newControl(ISpecificationParser.class);
        ISpecificationParser parser = (ISpecificationParser) parserControl.getMock();

        i.setParser(parser);

        parser.parseApplicationSpecification(appSpecResource);
        parserControl.setReturnValue(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replayControls();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        assertNotNull(ag.getActivator());
        assertSame(as, ag.getSpecification());

        verifyControls();
    }

    private void trainForServletInit(MockControl configControl, ServletConfig config)
    {
        MockControl contextControl = newControl(ServletContext.class);
        ServletContext context = (ServletContext) contextControl.getMock();

        config.getServletContext();
        configControl.setReturnValue(context);

        config.getServletName();
        configControl.setReturnValue("test");

        context.log("test: init");
    }

    public void testInAppContextFolder() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        MockControl contextControl = newControl(ServletContext.class);
        ServletContext context = (ServletContext) contextControl.getMock();

        MockControl logControl = newControl(Log.class);
        Log log = (Log) logControl.getMock();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        MockControl configControl = newControl(ServletConfig.class);
        ServletConfig config = (ServletConfig) configControl.getMock();

        trainForServletInit(configControl, config);

        config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM);
        configControl.setReturnValue(null);

        config.getServletContext();
        configControl.setReturnValue(context);

        config.getServletName();
        configControl.setReturnValue("fred");

        log.isDebugEnabled();
        logControl.setReturnValue(true);

        Resource r = new ContextResource(context, "/WEB-INF/fred/fred.application");

        log.debug("Checking for existence of " + r);

        context.getResource(r.getPath());
        contextControl.setReturnValue(new URL("file:/context" + r.getPath()));

        log.debug("Found " + r);

        IApplicationSpecification as = new ApplicationSpecification();

        MockControl parserControl = newControl(ISpecificationParser.class);
        ISpecificationParser parser = (ISpecificationParser) parserControl.getMock();

        i.setParser(parser);

        parser.parseApplicationSpecification(r);
        parserControl.setReturnValue(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replayControls();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verifyControls();
    }

    public void testInWebInfFolder() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        MockControl contextControl = newControl(ServletContext.class);
        ServletContext context = (ServletContext) contextControl.getMock();

        MockControl logControl = newControl(Log.class);
        Log log = (Log) logControl.getMock();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        MockControl configControl = newControl(ServletConfig.class);
        ServletConfig config = (ServletConfig) configControl.getMock();

        trainForServletInit(configControl, config);

        config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM);
        configControl.setReturnValue(null);

        config.getServletContext();
        configControl.setReturnValue(context);

        config.getServletName();
        configControl.setReturnValue("barney");

        log.isDebugEnabled();
        logControl.setReturnValue(false);

        Resource r = new ContextResource(context, "/WEB-INF/barney.application");

        context.getResource("/WEB-INF/barney/barney.application");
        contextControl.setReturnValue(null);

        log.isDebugEnabled();
        logControl.setReturnValue(false);

        context.getResource(r.getPath());
        contextControl.setReturnValue(new URL("file:/context" + r.getPath()));

        log.debug("Found " + r);

        IApplicationSpecification as = new ApplicationSpecification();

        MockControl parserControl = newControl(ISpecificationParser.class);
        ISpecificationParser parser = (ISpecificationParser) parserControl.getMock();

        i.setParser(parser);

        parser.parseApplicationSpecification(r);
        parserControl.setReturnValue(as);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replayControls();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verifyControls();
    }

    public void testNoAppSpec() throws Exception
    {
        DefaultClassResolver cr = new DefaultClassResolver();

        ApplicationSpecificationInitializer i = new ApplicationSpecificationInitializer();

        MockControl contextControl = newControl(ServletContext.class);
        ServletContext context = (ServletContext) contextControl.getMock();

        MockControl logControl = newControl(Log.class);
        Log log = (Log) logControl.getMock();

        i.setLog(log);

        ClasspathResourceFactoryImpl cf = new ClasspathResourceFactoryImpl();
        cf.setClassResolver(cr);

        i.setClasspathResourceFactory(cf);

        HttpServlet servlet = new ServletFixture();

        MockControl configControl = newControl(ServletConfig.class);
        ServletConfig config = (ServletConfig) configControl.getMock();

        trainForServletInit(configControl, config);

        config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM);
        configControl.setReturnValue(null);

        config.getServletContext();
        configControl.setReturnValue(context);

        config.getServletName();
        configControl.setReturnValue("wilma");

        log.isDebugEnabled();
        logControl.setReturnValue(false);

        context.getResource("/WEB-INF/wilma/wilma.application");
        contextControl.setReturnValue(null);

        log.isDebugEnabled();
        logControl.setReturnValue(false);

        context.getResource("/WEB-INF/wilma.application");
        contextControl.setReturnValue(null);

        config.getServletName();
        configControl.setReturnValue("wilma");

        log.debug("Could not find an application specification for application servlet wilma.");

        config.getServletName();
        configControl.setReturnValue("wilma");

        config.getServletContext();
        configControl.setReturnValue(context);

        ApplicationGlobals ag = new ApplicationGlobalsImpl();

        i.setGlobals(ag);

        replayControls();

        servlet.init(config);

        // The real ApplicationServlet will build a Registry and, indirectly, invoke this.

        i.initialize(servlet);

        verifyControls();

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
        MockControl contextControl = newControl(ServletContext.class);
        ServletContext context = (ServletContext) contextControl.getMock();

        HttpServlet servlet = new ServletFixture();

        // Create a non-strict control
        MockControl configControl = MockControl.createControl(ServletConfig.class);
        addControl(configControl);
        ServletConfig config = (ServletConfig) configControl.getMock();

        config.getInitParameter(ApplicationSpecificationInitializer.APP_SPEC_PATH_PARAM);
        configControl.setReturnValue(null);

        config.getServletContext();
        configControl.setReturnValue(context, 2);

        config.getServletName();
        configControl.setReturnValue("dino", 2);

        context.log("dino: init");

        context.getResource("/WEB-INF/dino/dino.application");
        contextControl.setReturnValue(getClass().getResource("ParseApp.application"), 2);

        replayControls();

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

        verifyControls();

    }
}