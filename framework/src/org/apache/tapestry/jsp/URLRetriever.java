/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.jsp;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.Tapestry;

/**
 *  Encapsulates the process of calling into the Tapestry servlet to retrieve
 *  a URL.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class URLRetriever
{
    private static final Log LOG = LogFactory.getLog(URLRetriever.class);

    private PageContext _pageContext;
    private String _serviceName;
    private Object[] _serviceParameters;

    public URLRetriever(PageContext pageContext, String serviceName, Object[] serviceParameters)
    {
        _pageContext = pageContext;
        _serviceName = serviceName;
        _serviceParameters = serviceParameters;
    }

    /**
     *  Invokes the servlet to retrieve the URL.  The URL is inserted
     *  into the output (as with
     *  {@link RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}). 
     * 
     *
     **/

    public void insertURL(String servletPath) throws JspException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Obtaining Tapestry URL for service " + _serviceName + " at " + servletPath);

        ServletRequest request = _pageContext.getRequest();

        RequestDispatcher dispatcher = request.getRequestDispatcher(servletPath);

        if (dispatcher == null)
            throw new JspException(
                Tapestry.format("URLRetriever.unable-to-find-dispatcher", servletPath));

        request.setAttribute(Tapestry.TAG_SUPPORT_SERVICE_ATTRIBUTE, _serviceName);
        request.setAttribute(Tapestry.TAG_SUPPORT_PARAMETERS_ATTRIBUTE, _serviceParameters);
        request.setAttribute(Tapestry.TAG_SUPPORT_SERVLET_PATH_ATTRIBUTE, servletPath);

        try
        {
            _pageContext.getOut().flush();

            dispatcher.include(request, _pageContext.getResponse());
        }
        catch (IOException ex)
        {
            throw new JspException(
                Tapestry.format("URLRetriever.io-exception", servletPath, ex.getMessage()));
        }
        catch (ServletException ex)
        {
            throw new JspException(
                Tapestry.format("URLRetriever.servlet-exception", servletPath, ex.getMessage()));
        }
        finally
        {
            request.removeAttribute(Tapestry.TAG_SUPPORT_SERVICE_ATTRIBUTE);
            request.removeAttribute(Tapestry.TAG_SUPPORT_PARAMETERS_ATTRIBUTE);
            request.removeAttribute(Tapestry.TAG_SUPPORT_SERVLET_PATH_ATTRIBUTE);
        }

    }
}
