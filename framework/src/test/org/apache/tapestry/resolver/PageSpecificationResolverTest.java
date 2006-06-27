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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
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

/**
 * Tests for {@link org.apache.tapestry.resolver.PageSpecificationResolverImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PageSpecificationResolverTest extends AbstractSpecificationResolverTestCase
{
    private static class MockApplicationNamespace implements INamespace
    {
        String _pageName;

        IComponentSpecification _specification;

        private Resource _specificationLocation;

        MockApplicationNamespace(Resource specificationLocation)
        {
            _specificationLocation = specificationLocation;
        }

        public String constructQualifiedName(String pageName)
        {
            return null;
        }

        public boolean containsComponentType(String type)
        {
            return false;
        }

        public boolean containsPage(String name)
        {
            return false;
        }

        public List getChildIds()
        {
            return null;
        }

        public INamespace getChildNamespace(String id)
        {
            return null;
        }

        public IComponentSpecification getComponentSpecification(String type)
        {
            return null;
        }

        public String getExtendedId()
        {
            return null;
        }

        public String getId()
        {
            return null;
        }

        public Location getLocation()
        {
            return null;
        }

        public String getNamespaceId()
        {
            return null;
        }

        public List getPageNames()
        {
            return null;
        }

        public IComponentSpecification getPageSpecification(String name)
        {
            return null;
        }

        public INamespace getParentNamespace()
        {
            return null;
        }

        public String getPropertyValue(String propertyName)
        {
            return null;
        }

        public ILibrarySpecification getSpecification()
        {
            return null;
        }

        public Resource getSpecificationLocation()
        {
            return _specificationLocation;
        }

        public void installComponentSpecification(String type, IComponentSpecification specification)
        {
        }

        public void installPageSpecification(String pageName, IComponentSpecification specification)
        {
            _pageName = pageName;
            _specification = specification;
        }

        public boolean isApplicationNamespace()
        {
            return true;
        }

    }

    private ISpecificationResolverDelegate newDelegate()
    {
        return (ISpecificationResolverDelegate) newMock(ISpecificationResolverDelegate.class);
    }

    private INamespace newNamespace(String pageName, IComponentSpecification spec)
    {
        INamespace namespace = newNamespace();

        trainContainsPage(namespace, pageName, spec != null);

        if (spec != null)
            trainGetPageSpecification(namespace, pageName, spec);

        return namespace;
    }

    private ComponentPropertySource newPropertySource(INamespace namespace)
    {
        ComponentPropertySource source = (ComponentPropertySource) newMock(ComponentPropertySource.class);

        expect(source.getNamespaceProperty(namespace, Tapestry.TEMPLATE_EXTENSION_PROPERTY))
        .andReturn("html");

        return source;
    }

    private ISpecificationSource newSource(INamespace application, INamespace framework)
    {
        ISpecificationSource source = newSource();

        trainGetApplicationNamespace(source, application);

        trainGetFrameworkNamespace(source, framework);

        return source;
    }

    private ISpecificationSource newSource(INamespace application, INamespace framework,
            Resource resource, IComponentSpecification pageSpec)
    {
        ISpecificationSource source = newSource();

        trainGetApplicationNamespace(source, application);

        trainGetFrameworkNamespace(source, framework);

        trainGetPageSpecification(source, resource, pageSpec);

        return source;
    }

    public void testExplicitlyInApplicationNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();
        INamespace application = newNamespace("ExistingPage", spec);
        INamespace framework = newNamespace();

        ISpecificationSource source = newSource();

        trainGetApplicationNamespace(source, application);
        trainGetFrameworkNamespace(source, framework);

        IRequestCycle cycle = newCycle();

        trainGetApplicationNamespace(source, application);

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "application:ExistingPage");

        assertEquals("ExistingPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verify();

    }

    public void testExplicitlyInFrameworkNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();
        INamespace application = newNamespace();
        INamespace framework = newNamespace("ExistingPage", spec);

        ISpecificationSource source = newSource();

        trainGetApplicationNamespace(source, application);
        trainGetFrameworkNamespace(source, framework);

        IRequestCycle cycle = newCycle();

        trainGetFrameworkNamespace(source, framework);

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "framework:ExistingPage");

        assertEquals("ExistingPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(framework, resolver.getNamespace());

        verify();
    }

    public void testFoundAsTemplate()
    {
        Log log = newLog();

        Resource contextRoot = newResource("context/");

        Resource resource = contextRoot.getRelativeResource("TemplatePage.html");

        MockApplicationNamespace application = new MockApplicationNamespace(contextRoot
                .getRelativeResource("WEB-INF/"));

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        ComponentPropertySource propertySource = newPropertySource(application);

        train(log, ResolverMessages.resolvingPage("TemplatePage", application));

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/TemplatePage.page")));

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/TemplatePage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/TemplatePage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("TemplatePage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("TemplatePage.html")));

        train(log, ResolverMessages.foundHTMLTemplate(resource));

        IComponentSpecification expectedSpec = new ComponentSpecification();
        expectedSpec.setPageSpecification(true);
        expectedSpec.setSpecificationLocation(resource);

        // The toString() on ComponentSpecification means we can't predict
        // what the string would be.

        trainIsDebugEnabled(log, false);

        replay();

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

        // The specification location is used to find relative assets and the like, and is baesd
        // on the page name and the namespace location.

        assertEquals(contextRoot.getRelativeResource("WEB-INF/TemplatePage.page"), spec
                .getSpecificationLocation());

        // The Location is used for any error reporting, and should be the actual file
        // located, the template.

        assertEquals(resource, spec.getLocation().getResource());
        assertEquals("TemplatePage", resolver.getSimplePageName());
        assertEquals("TemplatePage", application._pageName);

        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verify();
    }

    public void testFoundInApplicationNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();
        INamespace application = newNamespace("ExistingPage", spec);
        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "ExistingPage");

        assertEquals("ExistingPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verify();
    }

    public void testFoundInChildNamespace()
    {
        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        INamespace child = newNamespace("ChildPage", spec);
        INamespace application = newNamespace();

        trainGetChildNamespace(child, "foo.bar", application);

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();

        resolver.resolve(cycle, "foo.bar:ChildPage");

        assertEquals("ChildPage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(child, resolver.getNamespace());

        verify();
    }

    public void testFoundInContextRootFolder()
    {
        Log log = newLog();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("ContextRootPage.page");

        INamespace application = newNamespace();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "ContextRootPage", false);

        train(log, ResolverMessages.resolvingPage("ContextRootPage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/ContextRootPage.page")));

        trainIsApplicationNamespace(application, true);

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/ContextRootPage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/ContextRootPage.page")));
        train(log, ResolverMessages.checkingResource(resource));

        train(log, ResolverMessages.installingPage("ContextRootPage", application, spec));

        application.installPageSpecification("ContextRootPage", spec);

        replay();

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

        verify();
    }

    public void testFoundInFramework()
    {
        Log log = newLog();
        INamespace application = newNamespace();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        ComponentPropertySource propertySource = newPropertySource(application);

        INamespace framework = newNamespace("FrameworkPage", spec);
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "FrameworkPage", false);

        train(log, ResolverMessages.resolvingPage("FrameworkPage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/FrameworkPage.page")));

        trainIsApplicationNamespace(application, true);

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/FrameworkPage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/FrameworkPage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("FrameworkPage.page")));
        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("FrameworkPage.html")));

        train(log, ResolverMessages.foundFrameworkPage("FrameworkPage"));

        replay();

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

        verify();
    }

    public void testFoundInNamespaceFolder()
    {
        Log log = newLog();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/NamespacePage.page");

        INamespace application = newNamespace();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "NamespacePage", false);

        train(log, ResolverMessages.resolvingPage("NamespacePage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(resource));

        train(log, ResolverMessages.installingPage("NamespacePage", application, spec));

        application.installPageSpecification("NamespacePage", spec);

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.initializeService();
        resolver.setLog(log);

        resolver.resolve(cycle, "NamespacePage");

        assertEquals("NamespacePage", resolver.getSimplePageName());
        assertSame(spec, resolver.getSpecification());
        assertSame(application, resolver.getNamespace());

        verify();
    }

    public void testFoundInWebInfAppFolder()
    {
        Log log = newLog();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/myapp/MyAppPage.page");

        INamespace application = newNamespace();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "MyAppPage", false);

        train(log, ResolverMessages.resolvingPage("MyAppPage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/MyAppPage.page")));

        trainIsApplicationNamespace(application, true);

        train(log, ResolverMessages.checkingResource(resource));

        train(log, ResolverMessages.installingPage("MyAppPage", application, spec));

        application.installPageSpecification("MyAppPage", spec);

        replay();

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

        verify();
    }

    public void testFoundInWebInfFolder()
    {
        Log log = newLog();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        Resource resource = contextRoot.getRelativeResource("WEB-INF/MyWebInfPage.page");

        INamespace application = newNamespace();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework, resource, spec);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "MyWebInfPage", false);

        train(log, ResolverMessages.resolvingPage("MyWebInfPage", application));

        // Pretend the app spec is in the context root folder
        // Which isn't really something that happens in a real application
        // but is necessary to keep from finding the file too early.

        trainGetSpecificationLocation(application, contextRoot);

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("MyWebInfPage.page")));

        trainIsApplicationNamespace(application, true);

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/MyWebInfPage.page")));

        train(log, ResolverMessages.checkingResource(resource));

        train(log, ResolverMessages.installingPage("MyWebInfPage", application, spec));

        application.installPageSpecification("MyWebInfPage", spec);

        replay();

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

        verify();
    }

    public void testWebInf()
    {
        Log log = newLog();
        INamespace application = newNamespace();
        INamespace framework = newNamespace();

        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();
        Resource contextRoot = newResource("context/");

        trainContainsPage(application, "/WEB-Inf/BadName", false);

        trainIsDebugEnabled(log, false);

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();

        resolver.setSpecificationSource(source);
        resolver.setContextRoot(contextRoot);
        resolver.setLog(log);

        resolver.initializeService();

        try
        {
            resolver.resolve(cycle, "/WEB-Inf/BadName");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Page name '/WEB-Inf/BadName' is not valid, as it directly references a file stored in the WEB-INF folder.",
                    ex.getMessage());
        }

        verify();

        trainContainsPage(application, "web-inf/BadName", false);

        trainIsDebugEnabled(log, false);

        replay();

        try
        {
            resolver.resolve(cycle, "web-inf/BadName");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Page name 'web-inf/BadName' is not valid, as it directly references a file stored in the WEB-INF folder.",
                    ex.getMessage());
        }

        verify();

    }

    public void testNotFoundAnywhere()
    {
        Log log = newLog();
        INamespace application = newNamespace();

        Resource contextRoot = newResource("context/");

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "MissingPage", false);

        train(log, ResolverMessages.resolvingPage("MissingPage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/MissingPage.page")));

        trainIsApplicationNamespace(application, false);

        ISpecificationResolverDelegate delegate = newDelegate();

        trainFindPageSpecification(delegate, cycle, application, "MissingPage", null);

        trainGetNamespaceId(application, "<application namespace>");

        replay();

        PageSpecificationResolverImpl resolver = new PageSpecificationResolverImpl();
        resolver.setContextRoot(contextRoot);
        resolver.setSpecificationSource(source);
        resolver.setLog(log);
        resolver.setApplicationId("myapp");
        resolver.setDelegate(delegate);

        resolver.initializeService();

        try
        {
            resolver.resolve(cycle, "MissingPage");
            unreachable();
        }
        catch (PageNotFoundException ex)
        {
            assertEquals("Page 'MissingPage' not found in <application namespace>.", ex
                    .getMessage());
        }

        verify();
    }

    public void testProvidedByDelegate()
    {
        Log log = newLog();
        INamespace application = newNamespace();

        Resource contextRoot = newResource("context/");
        IComponentSpecification spec = newSpecification();

        INamespace framework = newNamespace();
        ISpecificationSource source = newSource(application, framework);
        IRequestCycle cycle = newCycle();

        trainContainsPage(application, "DelegatePage", false);

        train(log, ResolverMessages.resolvingPage("DelegatePage", application));

        // Pretend the app spec is in the WEB-INF folder

        trainGetSpecificationLocation(application, contextRoot, "WEB-INF/");

        train(log, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/DelegatePage.page")));

        trainIsApplicationNamespace(application, false);

        ISpecificationResolverDelegate delegate = newDelegate();

        trainFindPageSpecification(delegate, cycle, application, "DelegatePage", spec);

        trainIsDebugEnabled(log, false);

        application.installPageSpecification("DelegatePage", spec);

        replay();

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

        verify();
    }

    private void trainGetPageSpecification(INamespace namespace, String pageName,
            IComponentSpecification spec)
    {
        expect(namespace.getPageSpecification(pageName)).andReturn(spec);
    }

    private void trainGetPageSpecification(ISpecificationSource source, Resource resource,
            IComponentSpecification pageSpec)
    {
        expect(source.getPageSpecification(resource)).andReturn(pageSpec);
    }
}