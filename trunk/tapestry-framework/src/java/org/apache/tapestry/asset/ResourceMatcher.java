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

/**
 * Responsible for determining if the path string of a {@link Reasource} -
 * typically involving {@link IAsset} resources being examined by the 
 * {@link AssetService} - match the configured resource match strings
 * of this service.
 *
 * <p/>Currently used by {@link AssetService} to enable having unprotected 
 * resources based off of regexp patterns. 
 *
 * @author jkuhnert
 */
public interface ResourceMatcher {

    /**
     * Checks for the existance of a resource pattern that matches the incoming
     * path string.
     * @param path The path to a resource
     * @return True if the path is matched against one of the patterns configured
     *         for this matcher, false otherwise.
     */
    
    boolean containsResource(String path);
}
