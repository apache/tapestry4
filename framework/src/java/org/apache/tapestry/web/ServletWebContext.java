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

package org.apache.tapestry.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.Describable;
import org.apache.tapestry.describe.DescriptionReceiver;

/**
 * Adapts {@link javax.servlet.ServletContext}&nbsp;as {@link org.apache.tapestry.web.WebContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServletWebContext implements WebContext
{
    private static final Log LOG = LogFactory.getLog(ServletWebContext.class);

    private final ServletContext _servletContext;

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_servletContext);
    }

    public ServletWebContext(ServletContext context)
    {
        Defense.notNull(context, "context");

        _servletContext = context;
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_servletContext.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _servletContext.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _servletContext.removeAttribute(name);
        else
            _servletContext.setAttribute(name, attribute);

    }

    public URL getResource(String path)
    {
        try
        {
            return _servletContext.getResource(path);
        }
        catch (MalformedURLException ex)
        {
            LOG.error(WebMessages.errorGettingResource(path, ex), ex);

            return null;
        }
    }

    public String getInitParameterValue(String name)
    {
        return _servletContext.getInitParameter(name);
    }

    public List getInitParameterNames()
    {
        return WebUtils.toSortedList(_servletContext.getInitParameterNames());
    }

    public String getMimeType(String resourcePath)
    {
        return _servletContext.getMimeType(resourcePath);
    }
}