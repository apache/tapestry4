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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.resolver.ComponentSpecificationResolverImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestComponentSpecificationResolver extends AbstractSpecificationResolverTestCase
{
    private void trainIsDeprecated(MockControl control, IComponentSpecification spec,
            boolean isDeprecated)
    {
        spec.isDeprecated();
        control.setReturnValue(isDeprecated);
    }

    protected ISpecificationSource newSource(Resource resource, IComponentSpecification spec)
    {
        MockControl control = newControl(ISpecificationSource.class);
        ISpecificationSource source = (ISpecificationSource) control.getMock();

        source.getComponentSpecification(resource);
        control.setReturnValue(spec);

        return source;
    }

    protected ISpecificationSource newSource(INamespace framework)
    {
        MockControl control = newControl(ISpecificationSource.class);
        ISpecificationSource source = (ISpecificationSource) control.getMock();

        source.getFrameworkNamespace();
        control.setReturnValue(framework);

        return source;
    }

    private ISpecificationResolverDelegate newDelegate(IRequestCycle cycle, INamespace namespace,
            IComponentSpecification spec)
    {
        MockControl control = newControl(ISpecificationResolverDelegate.class);
        ISpecificationResolverDelegate delegate = (ISpecificationResolverDelegate) control
                .getMock();

        delegate.findComponentSpecification(cycle, namespace, "DelegateComponent");
        control.setReturnValue(spec);

        return delegate;
    }

    public void testFoundInNamespace()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl control = newControl(INamespace.class);
        INamespace namespace = (INamespace) control.getMock();

        namespace.containsComponentType("MyComponent");
        control.setReturnValue(true);

        namespace.getComponentSpecification("MyComponent");
        control.setReturnValue(spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();

        resolver.resolve(cycle, namespace, "MyComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testDeprecated()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl control = newControl(INamespace.class);
        INamespace namespace = (INamespace) control.getMock();

        namespace.containsComponentType("MyComponent");
        control.setReturnValue(true);

        namespace.getComponentSpecification("MyComponent");
        control.setReturnValue(spec);

        trainIsDeprecated(specc, spec, true);

        Log log = (Log) newMock(Log.class);
        
        log.error("Component 'MyComponent' (at classpath:/org/apache/tapestry/resolver/TestComponentSpecificationResolver, line 1) is deprecated, and will likely be removed in a later release. Consult its documentation to find a replacement component.");

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);

        resolver.resolve(cycle, namespace, "MyComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInChildNamespace()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        MockControl libraryc = newControl(INamespace.class);
        INamespace library = (INamespace) libraryc.getMock();

        namespace.getChildNamespace("lib");
        namespacec.setReturnValue(library);

        library.containsComponentType("MyComponent");
        libraryc.setReturnValue(true);

        library.getComponentSpecification("MyComponent");
        libraryc.setReturnValue(spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();

        resolver.resolve(cycle, namespace, "lib:MyComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(library, resolver.getNamespace());

        verifyControls();
    }

    public void testSearchFoundRelative()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        Resource namespaceLocation = newResource("LibraryStandin.library");
        Resource specLocation = namespaceLocation.getRelativeResource("MyComponent.jwc");

        ISpecificationSource source = newSource(specLocation, spec);

        namespace.containsComponentType("MyComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("MyComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(specLocation));
        train(log, logc, ResolverMessages.installingComponent("MyComponent", namespace, spec));

        namespace.installComponentSpecification("MyComponent", spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);

        resolver.resolve(cycle, namespace, "MyComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInFrameworkNamespace()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        MockControl frameworkc = newControl(INamespace.class);
        INamespace framework = (INamespace) frameworkc.getMock();

        Resource namespaceLocation = newResource("LibraryStandin.library");

        namespace.containsComponentType("FrameworkComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("FrameworkComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("FrameworkComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(false);

        ISpecificationSource source = newSource(framework);

        framework.containsComponentType("FrameworkComponent");
        frameworkc.setReturnValue(true);

        framework.getComponentSpecification("FrameworkComponent");
        frameworkc.setReturnValue(spec);

        train(log, logc, ResolverMessages
                .installingComponent("FrameworkComponent", namespace, spec));
        namespace.installComponentSpecification("FrameworkComponent", spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);

        resolver.resolve(cycle, namespace, "FrameworkComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testProvidedByDelegate()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        MockControl frameworkc = newControl(INamespace.class);
        INamespace framework = (INamespace) frameworkc.getMock();

        ISpecificationResolverDelegate delegate = newDelegate(cycle, namespace, spec);

        Resource namespaceLocation = newResource("LibraryStandin.library");

        namespace.containsComponentType("DelegateComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("DelegateComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("DelegateComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(false);

        ISpecificationSource source = newSource(framework);

        framework.containsComponentType("DelegateComponent");
        frameworkc.setReturnValue(false);

        log.isDebugEnabled();
        logc.setReturnValue(false);
        
        namespace.installComponentSpecification("DelegateComponent", spec);
        
        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);
        resolver.setDelegate(delegate);

        resolver.resolve(cycle, namespace, "DelegateComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testNotFound()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        MockControl frameworkc = newControl(INamespace.class);
        INamespace framework = (INamespace) frameworkc.getMock();

        ISpecificationResolverDelegate delegate = newDelegate(cycle, namespace, null);

        Resource namespaceLocation = newResource("LibraryStandin.library");

        namespace.containsComponentType("DelegateComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("DelegateComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("DelegateComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(false);

        ISpecificationSource source = newSource(framework);

        framework.containsComponentType("DelegateComponent");
        frameworkc.setReturnValue(false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);
        resolver.setDelegate(delegate);

        try
        {
            resolver.resolve(cycle, namespace, "DelegateComponent", l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Component 'DelegateComponent' not found in EasyMock for interface org.apache.tapestry.INamespace.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    /**
     * Test for checking inside the WEB-INF/app folder (app is the application id, i.e., the servlet
     * name).
     */

    public void testFoundInAppFolder()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        Resource namespaceLocation = newResource("LibraryStandin.library");
        Resource specLocation = contextRoot.getRelativeResource("WEB-INF/myapp/MyAppComponent.jwc");

        ISpecificationSource source = newSource(specLocation, spec);

        namespace.containsComponentType("MyAppComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("MyAppComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("MyAppComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(specLocation));
        train(log, logc, ResolverMessages.installingComponent("MyAppComponent", namespace, spec));

        namespace.installComponentSpecification("MyAppComponent", spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);
        resolver.setContextRoot(contextRoot);
        resolver.setApplicationId("myapp");
        resolver.initializeService();

        resolver.resolve(cycle, namespace, "MyAppComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInWebInfFolder()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        Resource namespaceLocation = newResource("LibraryStandin.library");
        Resource specLocation = contextRoot.getRelativeResource("WEB-INF/MyWebInfComponent.jwc");

        ISpecificationSource source = newSource(specLocation, spec);

        namespace.containsComponentType("MyWebInfComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("MyWebInfComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("MyWebInfComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/MyWebInfComponent.jwc")));
        train(log, logc, ResolverMessages.checkingResource(specLocation));
        train(log, logc, ResolverMessages.installingComponent("MyWebInfComponent", namespace, spec));

        namespace.installComponentSpecification("MyWebInfComponent", spec);

        trainIsDeprecated(specc, spec, false);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);
        resolver.setContextRoot(contextRoot);
        resolver.setApplicationId("myapp");
        resolver.initializeService();

        resolver.resolve(cycle, namespace, "MyWebInfComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }

    public void testFoundInContextRoot()
    {
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl logc = newControl(Log.class);
        Log log = (Log) logc.getMock();

        Resource contextRoot = newResource("context/");

        MockControl namespacec = newControl(INamespace.class);
        INamespace namespace = (INamespace) namespacec.getMock();

        Resource namespaceLocation = newResource("LibraryStandin.library");
        Resource specLocation = contextRoot.getRelativeResource("ContextRootComponent.jwc");

        ISpecificationSource source = newSource(specLocation, spec);

        namespace.containsComponentType("ContextRootComponent");
        namespacec.setReturnValue(false);

        train(log, logc, ResolverMessages.resolvingComponent("ContextRootComponent", namespace));

        namespace.getSpecificationLocation();
        namespacec.setReturnValue(namespaceLocation);

        train(log, logc, ResolverMessages.checkingResource(namespaceLocation
                .getRelativeResource("ContextRootComponent.jwc")));

        namespace.isApplicationNamespace();
        namespacec.setReturnValue(true);

        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/myapp/ContextRootComponent.jwc")));
        train(log, logc, ResolverMessages.checkingResource(contextRoot
                .getRelativeResource("WEB-INF/ContextRootComponent.jwc")));
        train(log, logc, ResolverMessages.checkingResource(specLocation));
        train(log, logc, ResolverMessages.installingComponent(
                "ContextRootComponent",
                namespace,
                spec));

        trainIsDeprecated(specc, spec, false);

        namespace.installComponentSpecification("ContextRootComponent", spec);

        replayControls();

        ComponentSpecificationResolverImpl resolver = new ComponentSpecificationResolverImpl();
        resolver.setLog(log);
        resolver.setSpecificationSource(source);
        resolver.setContextRoot(contextRoot);
        resolver.setApplicationId("myapp");
        resolver.initializeService();

        resolver.resolve(cycle, namespace, "ContextRootComponent", l);

        assertSame(spec, resolver.getSpecification());
        assertSame(namespace, resolver.getNamespace());

        verifyControls();
    }
}