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
 * The final entry on the tapestry.portlet.resolver.PageResolvers chain; this one returns a fixed,
 * default page name (typically, "View").
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultPortletPageResolver implements PortletPageResolver
{
    private String _pageName;

    public void setPageName(String pageName)
    {
        _pageName = pageName;
    }

    public String getPageNameForRequest(IRequestCycle cycle)
    {
        return _pageName;
    }

}
