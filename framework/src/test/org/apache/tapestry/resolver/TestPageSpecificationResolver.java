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

package org.apache.tapestry.resolver;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.resolver.PageSpecificationResolverImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPageSpecificationResolver extends AbstractSpecificationResolverTestCase
{
    private static class MockApplicationNamespace implements INamespace
    {
        private Resource _specificationLocation;

        String _pageName;

        IComponentSpecification _specification;

        MockApplicationNamespace(Resource specificationLocation)
        {
            _specificationLocation = specificationLocation;
        }

        public String getId()
        {
            return null;
        }

        public String getExtendedId()
        {
            return null;
        }

        public String getNamespaceId()
        {
            return null;
        }

        public INamespace getParentNamespace()
        {
            return null;
        }

        public INamespace getChildNamespace(String id)
        {
            return null;
        }

        public List getChildIds()
        {
            return null;
        }

        public IComponentSpecification getPageSpecification(String name)
        {
            return null;
        }

        public boolean containsPage(String name)
        {
            return false;
        }

        public List getPageNames()
        {
            return null;
        }

        public IComponentSpecification getComponentSpecification(String type)
        {
            return null;
        }

        public boolean containsComponentType(String type)
        {
            return false;
        }

        public ILibrarySpecification getSpecification()
        {
            return null;
        }

        public String constructQualifiedName(String pageName)
        {
            return null;
        }

        public Resource getSpecificationLocation()
        {
            return _specificationLocation;
        }

        public boolean isApplicationNamespace()
        {
            return true;
        }

        public void installPageSpecification(String pageName, IComponentSpecification specification)
        {
            _pageName = pageName;
            _specification = specification;
        }

        public void installComponentSpecification(String type, IComponentSpecification specification)
        {
        }

        public Location getLocation()
        {
            return null;
        }

        public String getPropertyValue(String propertyName)
        {
            return null;
        }

    }

    private ISpecificationSource newSource(INamespace application, INamespace framework)
    {
        MockControl control = newControl(ISpecificationSource.class);
        ISpecificationSource source = (ISpecificationSource) control.getMock();

        source.getApplicationNamespace();
        control.setReturnValue(application);

        source.getFrameworkNamespace();
        control.setReturnValue(framework);

        return source;
    }

    private ISpecificationSource newSource(INamespace application, INamespace framework,
            Resource resource, IComponentSpecification pageSpec)
    {
        MockControl control = newControl(ISpecificationSource.class);
        ISpecificationSource source = (ISpecificationSource) control.getMock();

        source.getApplicationNamespace();
        control.setReturnValue(application);

        source.getFrameworkNamespace();
        control.setReturnValue(framework);

        source.getPageSpecification(resource);
        control.setReturnValue(pageSpec);

        return source;
    }

    private INamespace newNamespace()
    {
        return (INamespace) newMock(INamespace.class);
    }

    private INamespace newNamespace(String pageName, IComponentSpecification spec)
    {
        MockControl control = newControl(INamespace.class);
        INamespace namespace = (INamespace) control.getMock();

        namespace.containsPage(pageName);
        control.setReturnValue(spec != null);

        if (spec != null)
        {
            namespace.getPageSpecification(pageName);
            control.setReturnValue(spec);
        }

        return namespace;
    }

    private ComponentPropertySource newPropertySource(INamespace namespace)
    {
        MockControl control = newControl(ComponentPropertySource.class);
        ComponentPropertySource source = (ComponentPropertySource) control.getMock();

        source.getNamespaceProperty(namespace, Tapestry.TEMPLATE_EXTENSION_PROPERTY);
        control.setReturnValue("html");

        return source;
    }

    public void testFoundInApplicationNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();
        INamespace application = newNamespace("ExistingPage", spec);
        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "ExistingPage");

        assertEquals("ExistingPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testExplicitlyInFrameworkNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();
        INamespace application = newNamespace();
        INamespace framework = newNamespace("ExistingPage", spec);
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "framework:ExistingPage");

        assertEquals("ExistingPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(framework, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInChildNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        INamespace child = newNamespace("ChildPage", spec);

        MockControl control = newControl(INamespace.class);
        INamespace application = (INamespace) control.getMock();

        application.getChildNamespace("foo.bar");
        control.setReturnValue(child);

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "foo.bar:ChildPage");

        assertEquals("ChildPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(child, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInNamespaceFolder()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/NamespacePage.page");

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        application.containsPage("NamespacePage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("NamespacePage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(resource));

        train(log, logc, ResolverMessages.installingPage("NamespacePage", application, spec));

        application.installPageSpecification("NamespacePage", spec);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();
        resolver.setLog(log);

        resolver.resolve(cycle, "NamespacePage");

        assertEquals("NamespacePage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInWebInfAppFolder()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/myapp/MyAppPage.page");

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        application.containsPage("MyAppPage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("MyAppPage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/MyAppPage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(resource));

        train(log, logc, ResolverMessages.installingPage("MyAppPage", application, spec));

        application.installPageSpecification("MyAppPage", spec);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");

        resolver.initializeService();

        resolver.resolve(cycle, "MyAppPage");

        assertEquals("MyAppPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInWebInfFolder()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/MyWebInfPage.page");

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        application.containsPage("MyWebInfPage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("MyWebInfPage", application));

        // Pretend the app spec is in the context root folder
        // Which isn't really something that happens in a real application
        // but is necessary to keep from finding the file too early.

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("MyWebInfPage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/MyWebInfPage.page")));

        train(log, logc, ResolverMessages.checkingResource(resource));

        train(log, logc, ResolverMessages.installingPage("MyWebInfPage", application, spec));

        application.installPageSpecification("MyWebInfPage", spec);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");

        resolver.initializeService();

        resolver.resolve(cycle, "MyWebInfPage");

        assertEquals("MyWebInfPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInContextRootFolder()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("ContextRootPage.page");

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        application.containsPage("ContextRootPage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("ContextRootPage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/ContextRootPage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/ContextRootPage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/ContextRootPage.page")));
        train(log, logc, ResolverMessages.checkingResource(resource));

        train(log, logc, ResolverMessages.installingPage("ContextRootPage", application, spec));

        application.installPageSpecification("ContextRootPage", spec);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");

        resolver.initializeService();

        resolver.resolve(cycle, "ContextRootPage");

        assertEquals("ContextRootPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundAsTemplate()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");

        Resource resource = contextRoot.getRelativeResource("TemplatePage.html");

        MockApplicationNamespace application = new MockApplicationNamespace(contextRoot
                .getRelativeResource("WEB-INF/"));

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        ComponentPropertySource propertySource = newPropertySource(application);

        train(log, logc, ResolverMessages.resolvingPage("TemplatePage", application));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/TemplatePage.page")));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/TemplatePage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/TemplatePage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("TemplatePage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("TemplatePage.html")));

        train(log, logc, ResolverMessages.foundHTMLTemplate(resource));

        IComponentSpecification expectedSpec = new ComponentSpecification();
        expectedSpec.setPageSpecification(true);
        expectedSpec.setSpecificationLocation(resource);

        // The toString() on ComponentSpecification means we can't predict
        // what the string would be.

        log.isDebugEnabled();
        logc.setReturnValue(false);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");
        resolver.setComponentPropertySource(propertySource);

        resolver.initializeService();

        resolver.resolve(cycle, "TemplatePage");

        IComponentSpecification spec = application._specification;

        assertEquals(true, spec.isPageSpecification());
        assertEquals(resource, spec.getSpecificationLocation());

        assertEquals("TemplatePage", resolver.getSimplePageName());
        assertEquals("TemplatePage", application._pageName);

        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInFramework()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        ComponentPropertySource propertySource = newPropertySource(application);

        INamespace framework = newNamespace("FrameworkPage", spec);
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        application.containsPage("FrameworkPage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("FrameworkPage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/FrameworkPage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/FrameworkPage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/FrameworkPage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("FrameworkPage.page")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("FrameworkPage.html")));

        train(log, logc, ResolverMessages.foundFrameworkPage("FrameworkPage"));

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");
        resolver.setComponentPropertySource(propertySource);

        resolver.initializeService();

        resolver.resolve(cycle, "FrameworkPage");

        assertEquals("FrameworkPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(framework, resolver.getNamespace());

        verifyControls();
    }

    public void testProvidedByDelegate()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        application.containsPage("DelegatePage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("DelegatePage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/DelegatePage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(false);

        MockControl delegatec = newControl(ISpecificationResolverDelegate.class);
        ISpecificationResolverDelegate delegate = (ISpecificationResolverDelegate) delegatec
                .getMock();

        delegate.findPageSpecification(cycle, application, "DelegatePage");
        delegatec.setReturnValue(spec);

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");
        resolver.setDelegate(delegate);

        resolver.initializeService();

        resolver.resolve(cycle, "DelegatePage");

        assertEquals("DelegatePage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verifyControls();
    }

    public void testNotFoundAnywhere()
    {
        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");

        MockControl applicationc = newControl(INamespace.class);
        INamespace application = (INamespace) applicationc.getMock();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        application.containsPage("DelegatePage");
        applicationc.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingPage("DelegatePage", application));

        // Pretend the app spec is in the WEB-INF folder

        application.getSpecificationLocation();
        applicationc.setReturnValue(contextRoot.getRelativeResource("WEB-INF/"));

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/DelegatePage.page")));

        application.isApplicationNamespace();
        applicationc.setReturnValue(false);

        MockControl delegatec = newControl(ISpecificationResolverDelegate.class);
        ISpecificationResolverDelegate delegate = (ISpecificationResolverDelegate) delegatec
                .getMock();

        delegate.findPageSpecification(cycle, application, "DelegatePage");
        delegatec.setReturnValue(null);

        application.getNamespaceId();
        applicationc.setReturnValue("<application namespace>");

        replayControls();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");
        resolver.setDelegate(delegate);

        resolver.initializeService();

        try
        {
            resolver.resolve(cycle, "DelegatePage");
            unreachable();
        }
        catch (PageNotFoundException ex)
        {
            assertEquals("Page 'DelegatePage' not found in <application namespace>.", ex
                    .getMessage());
        }

        verifyControls();
    }
}