/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	/**
	 *  Not to be invoked; this service is different than the others.
	 * 
	 *  @throws ApplicationRuntimeException always
	 * 
	 **/
	
    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        throw new ApplicationRuntimeException(Tapestry.getMessage("TagSupportService.service-only"));
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
            throw new ServletException(
                Tapestry.format("TagSupportService.null-attribute", name));

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
