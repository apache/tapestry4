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

package org.apache.tapestry;

/**
 * Extends {@link org.apache.tapestry.IScriptProcessor}&nbsp;with a handful of additional methods
 * needed when rendering a page response.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 * @see org.apache.tapestry.html.Body
 * @see org.apache.tapestry.TapestryUtils#getPageRenderSupport(IRequestCycle, Object)
 */
public interface PageRenderSupport extends IScriptProcessor
{
    /**
     * Sets up the given URL to preload, and returns a reference to the loaded image, in the form of
     * a snippet of JavaScript expression that can be inserted into some larger block of JavaScript
     * as a function parameter, or as a property assignment. A typical return value might be
     * <code>tapestry_preload[7].src</code>.
     */

    public String getPreloadedImageReference(String url);
}