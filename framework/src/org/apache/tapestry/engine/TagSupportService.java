//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.html.HTMLWriter;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  A very specialized service used by JSPs to access Tapestry URLs. 
 *  This is used by the Tapestry JSP tags, such as
 *  {@link org.apache.tapestry.jsp.PageTag}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *  @see org.apache.tapestry.jsp.URLRetriever
 * 
 **/

public class TagSupportService implements IEngineService
{
    private static final Log LOG = LogFactory.getLog(TagSupportService.class);

    /**
     *  Not to be invoked; this service is different than the others.
     * 
     *  @throws ApplicationRuntimeException always
     * 
     **/

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        throw new ApplicationRuntimeException(
            Tapestry.getMessage("TagSupportService.service-only"));
    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        RequestContext context = cycle.getRequestContext();
        HttpServletRequest request = context.getRequest();

        String serviceName = getAttribute(request, Tapestry.TAG_SUPPORT_SERVICE_ATTRIBUTE);

        Object raw = request.getAttribute(Tapestry.TAG_SUPPORT_PARAMETERS_ATTRIBUTE);
        Object[] parameters = null;

        try
        {
            parameters = (Object[]) raw;
        }
        catch (ClassCastException ex)
        {
            throw new ServletException(
                Tapestry.format(
                    "TagSupportService.attribute-not-array",
                    Tapestry.TAG_SUPPORT_PARAMETERS_ATTRIBUTE,
                    Tapestry.getClassName(raw.getClass())));
        }

        IEngineService service = cycle.getEngine().getService(serviceName);

        ILink link = service.getLink(cycle, null, parameters);

        String URI = link.getURL();

        if (LOG.isDebugEnabled())
        {
        	LOG.debug("Request servlet path = " + request.getServletPath());
        	
            Enumeration e = request.getParameterNames();
            while (e.hasMoreElements())
            {
                String name = (String) e.nextElement();
                LOG.debug("Request parameter " + name + " = " + request.getParameter(name));
            }
            e = request.getAttributeNames();
            while (e.hasMoreElements())
            {
                String name = (String) e.nextElement();
                LOG.debug("Request attribute " + name + " = " + request.getAttribute(name));
            }

            LOG.debug("Result URI: " + URI);
        }

        HttpServletResponse response = context.getResponse();
        PrintWriter servletWriter = response.getWriter();

        IMarkupWriter writer = new HTMLWriter(servletWriter);

        writer.print(URI);

        writer.flush();
    }

    private String getAttribute(HttpServletRequest request, String name) throws ServletException
    {
        Object result = request.getAttribute(name);

        if (result == null)
            throw new ServletException(Tapestry.format("TagSupportService.null-attribute", name));

        try
        {
            return (String) result;
        }
        catch (ClassCastException ex)
        {
            throw new ServletException(
                Tapestry.format(
                    "TagSupportService.attribute-not-string",
                    name,
                    Tapestry.getClassName(result.getClass())));

        }
    }

    /**
     *  @return {@link org.apache.tapestry.Tapestry#TAGSUPPORT_SERVICE}.
     * 
     **/

    public String getName()
    {
        return Tapestry.TAGSUPPORT_SERVICE;
    }

}
