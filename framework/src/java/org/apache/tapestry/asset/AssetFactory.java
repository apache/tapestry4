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

import java.util.Locale;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;

/**
 * A service which creates an asset. In some cases, the asset is selected based on the Resource
 * (typically of the component or page specification).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface AssetFactory
{
    /**
     * Creates the new asset.
     * 
     * @param baseResource
     *            the base resource from which an asset path may be calculated. Each type of asset
     *            is linked to a particular implemenation of {@link Resource}, and generates a
     *            corresponding implementation of {@link org.apache.tapestry.IAsset}.
     * @param path
     *            the path relative to the resource (if no leading slash), or an absolute path
     *            within the domain of the asset type (i.e, within the classpath, or within the web
     *            application).
     * @param locale
     *            the desired locale of the asset; the closest match will be used.
     * @param location
     *            the location to be associated with the returned asset.
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if no matching asset may be found.
     */
    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location);
}