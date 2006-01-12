// Copyright 2005, 2006 The Apache Software Foundation
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

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Base class for testing specification resolvers.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class AbstractSpecificationResolverTestCase extends
        HiveMindTestCase {

    protected IComponentSpecification newSpecification()
    {
        return (IComponentSpecification)newMock(IComponentSpecification.class);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle)newMock(IRequestCycle.class);
    }

    protected URL newURL(String file)
    {
        return getClass().getResource(file);
    }

    protected Resource newResource(URL url)
    {
        MockControl control = newControl(Resource.class);
        Resource resource = (Resource)control.getMock();

        resource.getResourceURL();
        control.setReturnValue(url);

        return resource;
    }

    protected Resource newResource(String path)
    {
        return new URLResource(newURL(path));
    }

    protected void train(Log log, String message)
    {
        log.isDebugEnabled();
        setReturnValue(log, true);

        log.debug(message);
    }

    protected Log newLog()
    {
        return (Log)newMock(Log.class);
    }

    protected INamespace newNamespace()
    {
        return (INamespace)newMock(INamespace.class);
    }

    protected ISpecificationSource newSource()
    {
        return (ISpecificationSource)newMock(ISpecificationSource.class);
    }

    protected void trainContainsPage(INamespace namespace, String pageName,
            boolean containsPage)
    {
        namespace.containsPage(pageName);
        setReturnValue(namespace, containsPage);
    }

    protected void trainFindPageSpecification(
            ISpecificationResolverDelegate delegate, IRequestCycle cycle,
            INamespace application, String pageName,
            IComponentSpecification spec)
    {
        delegate.findPageSpecification(cycle, application, pageName);
        setReturnValue(delegate, spec);
    }

    protected void trainGetApplicationNamespace(ISpecificationSource source,
            INamespace application)
    {
        source.getApplicationNamespace();
        setReturnValue(source, application);
    }

    protected void trainGetChildNamespace(INamespace child, String name,
            INamespace application)
    {
        application.getChildNamespace(name);
        setReturnValue(application, child);
    }

    protected void trainGetFrameworkNamespace(ISpecificationSource source,
            INamespace framework)
    {
        source.getFrameworkNamespace();
        setReturnValue(source, framework);
    }

    protected void trainGetNamespaceId(INamespace namespace, String namespaceId)
    {
        namespace.getNamespaceId();
        setReturnValue(namespace, namespaceId);
    }

    protected void trainGetSpecificationLocation(INamespace namespace,
            Resource resource)
    {
        namespace.getSpecificationLocation();
        setReturnValue(namespace, resource);
    }

    protected void trainGetSpecificationLocation(INamespace namespace,
            Resource root, String path)
    {
        namespace.getSpecificationLocation();
        setReturnValue(namespace, root.getRelativeResource(path));
    }

    protected void trainIsApplicationNamespace(INamespace namespace,
            boolean isApplicationNamespace)
    {
        namespace.isApplicationNamespace();
        setReturnValue(namespace, isApplicationNamespace);
    }

    protected void trainIsDebugEnabled(Log log, boolean isDebugEnabled)
    {
        log.isDebugEnabled();
        setReturnValue(log, isDebugEnabled);
    }
}
