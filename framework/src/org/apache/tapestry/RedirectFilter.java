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

package org.apache.tapestry;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filter used to redirect a root context URL (i.e., "/context" or "/context/"
 * to the Tapestry application servlet (typically, "/context/app").  This
 * servlet is mapped to "/" and must have a &lt;init-parameter&;gt; 
 * <code>redirect-path</code> that is the application servlet's path (i.e.,
 * "/app").  If no value is specified, then "/app" is used.  The path
 * is always relative to the servlet context, and should always
 * begin with a leading slash.
 *  
 * <p>Filters are only available in Servlet API 2.3 and above.
 * 
 * <p>Servlet API 2.4 is expected to allow a servlets in the welcome list
 * (equivalent to index.html or index.jsp), at which point this filter
 * should no longer be necessary.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

public class RedirectFilter implements Filter
{
    private static final Log LOG = LogFactory.getLog(RedirectFilter.class);
    public static final String REDIRECT_PATH_PARAM = "redirect-path";

    private String _redirectPath;

    public void init(FilterConfig config) throws ServletException
    {
        _redirectPath = config.getInitParameter(REDIRECT_PATH_PARAM);

        if (Tapestry.isNull(_redirectPath))
            _redirectPath = "/app";

        if (LOG.isInfoEnabled())
            LOG.info(Tapestry.format("RedirectServlet.redirect-path", _redirectPath));
    }

    public void destroy()
    {

    }

    /**
     * This filter intercepts the so-called "default" servlet, whose job is
     * to provide access to standard resources packaged within the web application
     * context.  This code is interested in only the very root, redirecting
     * to the appropriate Tapestry application servlet.  Other values
     * are passed through unchanged.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;

        String servletPath = hrequest.getServletPath();
        String pathInfo = hrequest.getPathInfo();

        // Been experimenting with different servlet containers.  In Jetty 4.2.8 and Tomcat 4.1,
        // resources have a non-null servletPath.  If JBossWeb 3.0.6, the servletPath is
        // null and the pathInfo indicates the relative location of the resource.

        if ((Tapestry.isNull(servletPath) || servletPath.equals("/"))
            && (Tapestry.isNull(pathInfo) || pathInfo.equals("/")))
        {
            String path = hrequest.getContextPath() + _redirectPath;

            if (LOG.isInfoEnabled())
                LOG.info(Tapestry.format("RedirectServlet.redirecting", path));

            hresponse.sendRedirect(path);
            return;
        }

        chain.doFilter(request, response);
    }

}
