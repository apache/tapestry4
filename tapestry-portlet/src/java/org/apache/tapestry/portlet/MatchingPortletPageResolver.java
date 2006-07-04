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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;

/**
 * Uses the tapestry.portlet.resolver.PageResolverRules configuration point to find a match.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MatchingPortletPageResolver implements PortletPageResolver
{
    private List _contributions;

    private List _sortedContributions;

    private PortletRequest _request;

    public void initializeService()
    {
        _sortedContributions = new ArrayList(_contributions);
        Collections.sort(_sortedContributions);
    }

    public String getPageNameForRequest(IRequestCycle cycle)
    {
        Iterator i = _sortedContributions.iterator();
        while (i.hasNext())
        {
            PageResolverContribution c = (PageResolverContribution) i.next();

            if (c.match(_request))
            {
                String pageName = c.getPageName();

                try
                {
                    cycle.getPage(pageName);

                    return pageName;
                }
                catch (PageNotFoundException ex)
                {
                    // Ignore.
                }
            }
        }

        // No match, or no matches where the page actually exists.

        return null;
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    public void setRequest(PortletRequest request)
    {
        _request = request;
    }

}
