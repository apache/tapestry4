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

package org.apache.tapestry.asset;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.spec.IComponentSpecification;

import java.util.*;

/**
 * Implementation of the {@link org.apache.tapestry.asset.AssetSource} service interface.
 * 
 */
public class AssetSourceImpl implements AssetSource
{
    private Map _assetFactoryByPrefix = new HashMap();

    private List _contributions;

    private AssetFactory _defaultAssetFactory;

    private AssetFactory _lookupAssetFactory;

    private AssetFactory _classpathAssetFactory;

    private AssetFactory _contextAssetFactory;

    public void initializeService()
    {
        Iterator i = _contributions.iterator();
        while (i.hasNext())
        {
            AssetFactoryContribution c = (AssetFactoryContribution) i.next();

            _assetFactoryByPrefix.put(c.getPrefix(), c.getFactory());
        }
    }

    public IAsset findAsset(Resource base, String path, Locale locale, Location location)
    {
        return findAsset(base, null, path, locale, location);
    }

    public IAsset findAsset(Resource base, IComponentSpecification spec, String path, Locale locale, Location location)
    {
        Defense.notNull(path, "path");
        Defense.notNull(location, "location");

        int colonx = path.indexOf(':');
        
        String prefix = colonx > -1 ? path.substring(0, colonx) : null;
        String truePath = colonx > -1 ? path.substring(colonx + 1) : path;

        Resource assetBase = base;
        AssetFactory factory = null;

        if (prefix != null) {

            factory = (AssetFactory) _assetFactoryByPrefix.get(prefix);
        }

        // now we have to search
        
        if (factory == null && prefix == null) {
            
            factory = findAssetFactory(spec, assetBase, path, locale);
        }
        
        // Unknown prefix is expected to happen when an external asset (using an established
        // prefix such as http:) is referenced.

        if (factory == null)
        {
            factory = _defaultAssetFactory;

            // Path is the full path, including the prefix (which is really the scheme
            // of the URL).

            truePath = path;
        }
        
        if (truePath.startsWith("/"))
            return factory.createAbsoluteAsset(truePath, locale, location);

        // This can happen when a 3.0 DTD is read in

        return factory.createAsset(assetBase, spec, truePath, locale, location);
    }

    AssetFactory findAssetFactory(IComponentSpecification spec, Resource baseResource, String path, Locale locale)
    {
        // need to check these two core factories in order first

        if (_classpathAssetFactory.assetExists(spec, baseResource, path, locale))
            return _classpathAssetFactory;

        if (_contextAssetFactory.assetExists(spec, baseResource, path, locale))
            return _contextAssetFactory;
        
        for (int i=0; i < _contributions.size(); i++) {

            AssetFactoryContribution c = (AssetFactoryContribution)_contributions.get(i);

            if (c.getFactory().assetExists(spec, baseResource, path, locale))
                return c.getFactory();
        }

        return null;
    }

    /**
     * Factory used when the path has no prefix, and the type of asset must be inferred from the
     * type of resource.
     */

    public void setLookupAssetFactory(AssetFactory lookupAssetFactory)
    {
        _lookupAssetFactory = lookupAssetFactory;
    }

    /**
     * List of {@link org.apache.tapestry.asset.AssetFactoryContribution}.
     */

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    /**
     * Factory used when an unrecognized prefix (typically, an arbitrary URL's scheme) is provided.
     */

    public void setDefaultAssetFactory(AssetFactory defaultAssetFactory)
    {
        _defaultAssetFactory = defaultAssetFactory;
    }

    public void setClasspathAssetFactory(AssetFactory classpathAssetFactory)
    {
        _classpathAssetFactory = classpathAssetFactory;
    }

    public void setContextAssetFactory(AssetFactory contextAssetFactory)
    {
        _contextAssetFactory = contextAssetFactory;
    }
}
