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

/**
 * Calculates the digest value, as a string, for a particular classpath resource. This is primarily
 * used by the {@link org.apache.tapestry.asset.AssetService}to authenticate requests (you are not
 * allowed access to a resource unless you can provide the correct digest value).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ResourceDigestSource
{
    /**
     * Calculates the DIGEST checksum for a classpath resource. Because this is expensive, the value
     * may be cached.
     * 
     * @param resourcePath
     *            the classpath resource, which should start with a leading slash.
     * @return A string representation of the digest for the provided resource path.
     * @throws ApplicationRuntimeException
     *             if the resource does not exist, or there is an error calculating the checksum.
     */

    public String getDigestForResource(String resourcePath);
}