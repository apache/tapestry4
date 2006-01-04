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

package org.apache.tapestry.portlet;

import org.apache.tapestry.IRequestCycle;

/**
 * Used by {@link org.apache.tapestry.portlet.PortletHomeService} to determine the correct page name
 * to use to render a request that does not specify a page (that's what the home service does).
 * 
 * @author Howard M. Lewis Ship
 */
public interface PortletPageResolver
{
    /**
     * Looks at the current request to determine the correct page name.
     * 
     * @returns The page name to activate and render (never null).
     */

    public String getPageNameForRequest(IRequestCycle cycle);
}
