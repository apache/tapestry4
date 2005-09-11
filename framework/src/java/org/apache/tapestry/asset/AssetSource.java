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
 * Used to create an {@link org.apache.tapestry.IAsset} instance for a particular asset path. The
 * path may have a prefix that indicates its type, or it may be relative to some existing resource.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface AssetSource
{
    /**
     * Finds an asset relative to some existing resource (typically, a page, component or library
     * specification).
     * 
     * @param base
     *            the base resource used for resolving the asset
     * @param path
     *            the path relative to the base resource; alternately, the path may include a prefix
     *            that defines a domain (such as "classpath:" or "context:") in which case the base
     *            resource is ignored and the resource resolved within that domain
     * @param locale
     *            used to find a localized version of the asset, may be null to indicate no
     *            localization
     * @param location
     *            used to report errors (such as missing resources)
     * @return the asset, possibly localized
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the asset does not exist
     */
    public IAsset findAsset(Resource base, String path, Locale locale, Location location);
}