// Copyright 2004, 2005 The Apache Software Foundation
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

import java.net.URL;

/**
 * Calculates the checksum value, as a string, for a particular classpath resource. This is primarily
 * used by the {@link org.apache.tapestry.asset.AssetService} to authenticate requests (you are not
 * allowed access to a resource unless you can provide the correct checksum value).
 * 
 * This code is based on code from Howard Lewis Ship from the upcoming 3.1 release.
 * 
 * @author  Paul Ferraro
 * @since   3.0.3
 */
public interface ResourceChecksumSource
{
    /**
     * Returns the checksum value for the given resource.
     * @param resourceURL the url of a resource
     * @return the checksum value of the specified resource
     */
    public String getChecksum(URL resourceURL);
    
    /**
     * Clears the internal cache.
     */
    public void reset();
}
