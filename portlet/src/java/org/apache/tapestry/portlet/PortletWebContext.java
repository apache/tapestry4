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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts {@link javax.portlet.PortletContext}as {@link org.apache.tapestry.web.WebContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletWebContext implements WebContext
{
    private static final Log LOG = LogFactory.getLog(PortletWebContext.class);

    private final PortletContext _portletContext;

    public PortletWebContext(PortletContext portletContext)
    {
        Defense.notNull(portletContext, "portletContext");

        _portletContext = portletContext;
    }

    public URL getResource(String path)
    {
        try
        {
            return _portletContext.getResource(path);
        }
        catch (MalformedURLException ex)
        {
            LOG.error(PortletMessages.errorGettingResource(path, ex), ex);

            return null;
        }
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_portletContext.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _portletContext.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _portletContext.removeAttribute(name);
        else
            _portletContext.setAttribute(name, attribute);
    }

    public List getInitParameterNames()
    {
        return WebUtils.toSortedList(_portletContext.getInitParameterNames());
    }

    public String getInitParameterValue(String name)
    {
        return _portletContext.getInitParameter(name);
    }

}