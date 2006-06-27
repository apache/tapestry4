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

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Base class for testing specification resolvers.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class AbstractSpecificationResolverTestCase extends BaseComponentTestCase
{

    protected IComponentSpecification newSpecification()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected URL newURL(String file)
    {
        return getClass().getResource(file);
    }

    protected Resource newResource(URL url)
    {
        Resource resource = (Resource)newMock(Resource.class);

        expect(resource.getResourceURL()).andReturn(url);

        return resource;
    }

    protected Resource newResource(String path)
    {
        return new URLResource(newURL(path));
    }

    protected void train(Log log, String message)
    {
        expect(log.isDebugEnabled()).andReturn(true);

        log.debug(message);
    }

    protected Log newLog()
    {
        return (Log) newMock(Log.class);
    }

    protected INamespace newNamespace()
    {
        return (INamespace) newMock(INamespace.class);
    }

    protected ISpecificationSource newSource()
    {
        return (ISpecificationSource) newMock(ISpecificationSource.class);
    }

    protected void trainContainsPage(INamespace namespace, String pageName, boolean containsPage)
    {
        expect(namespace.containsPage(pageName)).andReturn(containsPage);
    }

    protected void trainFindPageSpecification(ISpecificationResolverDelegate delegate, IRequestCycle cycle, INamespace application, String pageName, IComponentSpecification spec)
    {
        expect(delegate.findPageSpecification(cycle, application, pageName)).andReturn(spec);
    }

    protected void trainGetApplicationNamespace(ISpecificationSource source, INamespace application)
    {
        expect(source.getApplicationNamespace()).andReturn(application);
    }

    protected void trainGetChildNamespace(INamespace child, String name, INamespace application)
    {
        expect(application.getChildNamespace(name)).andReturn(child);
    }

    protected void trainGetFrameworkNamespace(ISpecificationSource source, INamespace framework)
    {
        expect(source.getFrameworkNamespace()).andReturn(framework);
    }

    protected void trainGetNamespaceId(INamespace namespace, String namespaceId)
    {
        expect(namespace.getNamespaceId()).andReturn(namespaceId);
    }

    protected void trainGetSpecificationLocation(INamespace namespace, Resource resource)
    {
        expect(namespace.getSpecificationLocation()).andReturn(resource);
    }

    protected void trainGetSpecificationLocation(INamespace namespace, Resource root, String path)
    {
        expect(namespace.getSpecificationLocation()).andReturn(root.getRelativeResource(path));
    }

    protected void trainIsApplicationNamespace(INamespace namespace, boolean isApplicationNamespace)
    {
        expect(namespace.isApplicationNamespace()).andReturn(isApplicationNamespace);
    }

    protected void trainIsDebugEnabled(Log log, boolean isDebugEnabled)
    {
        expect(log.isDebugEnabled()).andReturn(isDebugEnabled);
    }

}