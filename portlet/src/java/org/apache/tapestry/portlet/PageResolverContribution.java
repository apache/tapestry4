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

import javax.portlet.PortletRequest;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.ToStringBuilder;

/**
 * Contribution used for resolving requests to named pages.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PageResolverContribution extends BaseLocatable implements Comparable
{
    private String _portletMode;

    private String _windowState;

    private String _mimeType;

    private String _pageName;

    public void setMimeType(String mimeType)
    {
        _mimeType = mimeType;
    }

    public void setPageName(String pageName)
    {
        _pageName = pageName;
    }

    public String getPageName()
    {
        return _pageName;
    }

    public void setPortletMode(String portletMode)
    {
        _portletMode = portletMode;
    }

    public void setWindowState(String windowState)
    {
        _windowState = windowState;
    }

    int sortScore()
    {
        int result = 0;

        if (_mimeType != null)
            result += 4;

        if (_portletMode != null)
            result += 2;

        if (_windowState != null)
            result += 1;

        return result;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("mimeType", _mimeType);
        builder.append("portletMode", _portletMode);
        builder.append("windowState", _windowState);
        builder.append("pageName", _pageName);

        return builder.toString();
    }

    public int compareTo(Object o)
    {
        int thisScore = sortScore();
        int otherScore = ((PageResolverContribution) o).sortScore();

        // End result: sorted descending by specificity

        return otherScore - thisScore;
    }

    public boolean match(PortletRequest request)
    {
        Defense.notNull(request, "request");

        if (_mimeType != null && !_mimeType.equals(request.getResponseContentType()))
            return false;

        if (_portletMode != null && !_portletMode.equals(request.getPortletMode().toString()))
            return false;

        if (_windowState != null && !_windowState.equals(request.getWindowState().toString()))
            return false;

        return true;
    }
}
