// Copyright 2004 The Apache Software Foundation
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

/**
 * <p>
 * Note: This was a class prior to 3.1, at which point it was split into a service interface and a
 * {@link org.apache.tapestry.asset.AssetExternalizerImpl service implementation}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface AssetExternalizer
{
    /**
     * Gets the URL to a private resource. If the resource was previously copied out of the
     * classpath, the previously generated URL is returned.
     * <p>
     * If the asset directory and URL are not configured, then returns null.
     * <p>
     * Otherwise, the asset is copied out to the asset directory, the URL is constructed (and
     * recorded for later) and the URL is returned.
     * <p>
     * This method is not explicitly synchronized but should work multi-threaded. It synchronizes on
     * the internal <code>Map</code> used to map resource paths to URLs.
     * 
     * @param resourcePath
     *            The full path of the resource within the classpath. This is expected to include a
     *            leading slash. For example: <code>/com/skunkworx/Banner.gif</code>.
     */
    public String getURL(String resourcePath);
}