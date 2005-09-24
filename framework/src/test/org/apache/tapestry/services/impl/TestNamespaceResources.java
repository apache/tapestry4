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

package org.apache.tapestry.services.impl;

import java.net.URL;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.services.NamespaceResources;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Tests for {@link org.apache.tapestry.services.impl.NamespaceResourcesImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestNamespaceResources extends HiveMindTestCase
{
    protected Resource newResource()
    {
        return (Resource) newMock(Resource.class);
    }

    protected void trainGetRelativeResource(Resource parent, String path, Resource child)
    {
        parent.getRelativeResource(path);
        setReturnValue(parent,child);
    }

    protected void trainGetResourceURL(Resource resource, URL url)
    {
        resource.getResourceURL();
        setReturnValue(resource,url);
    }

    protected ISpecificationSource newSource()
    {
        return (ISpecificationSource) newMock(ISpecificationSource.class);
    }

    protected ILibrarySpecification newSpec()
    {
        return (ILibrarySpecification) newMock(ILibrarySpecification.class);
    }

    protected AssetSource newAssetSource()
    {
        return (AssetSource) newMock(AssetSource.class);
    }

    public void testFindChildLibrarySpecification()
    {
        Resource parent = newResource();
        Resource child = newResource();
        ISpecificationSource source = newSource();
        ILibrarySpecification spec = newSpec();
        AssetSource assetSource = newAssetSource();
        Location l = newLocation();

        trainResolveChildResource(assetSource, parent, "foo/bar.library", l, child);

        trainGetLibrarySpecification(source, child, spec);

        replayControls();

        NamespaceResources nr = new NamespaceResourcesImpl(source, assetSource);

        assertSame(spec, nr.findChildLibrarySpecification(parent, "foo/bar.library", l));

        verifyControls();

    }

    protected void trainResolveChildResource(AssetSource assetSource, Resource parent,
            String childPath, Location location, Resource child)
    {
        IAsset asset = newAsset();

        assetSource.findAsset(parent, childPath, null, location);
        setReturnValue(assetSource,asset);

        asset.getResourceLocation();
        setReturnValue(asset,child);
    }

    protected IAsset newAsset()
    {
        return (IAsset) newMock(IAsset.class);
    }

    protected URL newURL()
    {
        return getClass().getResource("TestNamespaceResources.class");
    }

    protected void trainGetLibrarySpecification(ISpecificationSource source, Resource resource,
            ILibrarySpecification spec)
    {
        source.getLibrarySpecification(resource);
        setReturnValue(source,spec);
    }

    protected IComponentSpecification newComponentSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    public void testGetPageSpecification()
    {
        Resource libraryResource = newResource();
        Resource specResource = newResource();
        IComponentSpecification spec = newComponentSpec();
        ISpecificationSource source = newSource();
        AssetSource assetSource = newAssetSource();
        Location l = newLocation();

        trainResolveChildResource(assetSource, libraryResource, "Foo.page", l, specResource);

        source.getPageSpecification(specResource);
        setReturnValue(source,spec);

        replayControls();

        NamespaceResources nr = new NamespaceResourcesImpl(source, assetSource);

        assertSame(spec, nr.getPageSpecification(libraryResource, "Foo.page", l));

        verifyControls();
    }

    public void testGetComponentSpecification()
    {
        Resource libraryResource = newResource();
        Resource specResource = newResource();
        IComponentSpecification spec = newComponentSpec();
        ISpecificationSource source = newSource();
        AssetSource assetSource = newAssetSource();
        Location l = newLocation();

        trainResolveChildResource(assetSource, libraryResource, "Foo.jwc", l, specResource);

        source.getComponentSpecification(specResource);
        setReturnValue(source,spec);

        replayControls();

        NamespaceResources nr = new NamespaceResourcesImpl(source, assetSource);

        assertSame(spec, nr.getComponentSpecification(libraryResource, "Foo.jwc", l));

        verifyControls();
    }
}
