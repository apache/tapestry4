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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;

/**
 * Implementation of the {@link org.apache.tapestry.asset.AssetSource}service interface.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AssetSourceImpl implements AssetSource
{
    private Map _assetFactoryByPrefix = new HashMap();

    private List _contributions;

    private AssetFactory _defaultAssetFactory;

    private AssetFactory _lookupAssetFactory;

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
        Defense.notNull(base, "base");
        Defense.notNull(path, "path");
        Defense.notNull(locale, "locale");
        Defense.notNull(location, "location");

        int colonx = path.indexOf(':');

        if (colonx < 0)
            return _lookupAssetFactory.createAsset(base, path, locale, location);

        String prefix = path.substring(0, colonx);
        String truePath = path.substring(colonx + 1);

        AssetFactory factory = (AssetFactory) _assetFactoryByPrefix.get(prefix);

        // Unknown prefix is expected to happen when an external asset (using an established
        // prefix such as http:) is referenced.

        if (factory == null)
        {
            factory = _defaultAssetFactory;

            // Path is the full path, including the prefix (which is really the scheme
            // of the URL).

            truePath = path;
        }

        return factory.createAsset(base, truePath, locale, location);
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

}