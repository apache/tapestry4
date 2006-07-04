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

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.services.NamespaceResources;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Implementation of {@link org.apache.tapestry.services.NamespaceResources}.
 * 
 * @author Howard M. Lewis Ship
 */
public class NamespaceResourcesImpl implements NamespaceResources
{
    private final ISpecificationSource _specificationSource;

    private final AssetSource _assetSource;

    public NamespaceResourcesImpl(ISpecificationSource source, AssetSource assetSource)
    {
        Defense.notNull(source, "source");
        Defense.notNull(assetSource, "assetSource");

        _specificationSource = source;
        _assetSource = assetSource;
    }

    public ILibrarySpecification findChildLibrarySpecification(Resource parentResource,
            String path, Location location)
    {
        Resource childResource = findSpecificationResource(parentResource, path, location);

        return _specificationSource.getLibrarySpecification(childResource);

    }

    private Resource findSpecificationResource(Resource libraryResource, String path,
            Location location)
    {
        // TODO: This is where we'll play with assets and asset prefixes

        IAsset childAsset = _assetSource.findAsset(libraryResource, path, null, location);

        Resource childResource = childAsset.getResourceLocation();

        return childResource;
    }

    public IComponentSpecification getPageSpecification(Resource resource,
            String specificationPath, Location location)
    {
        Resource pageSpecificationResource = findSpecificationResource(
                resource,
                specificationPath,
                location);

        return _specificationSource.getPageSpecification(pageSpecificationResource);
    }

    public IComponentSpecification getComponentSpecification(Resource resource,
            String specificationPath, Location location)
    {
        Resource componentSpecificationResource = findSpecificationResource(
                resource,
                specificationPath,
                location);

        return _specificationSource.getComponentSpecification(componentSpecificationResource);
    }

}
