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

        if (Tapestry.isBlank(_redirectPath))
            _redirectPath = "/app";

        if (LOG.isDebugEnabled())
            LOG.debug(Tapestry.format("RedirectServlet.redirect-path", _redirectPath));
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

        if ((Tapestry.isBlank(servletPath) || servletPath.equals("/"))
            && (Tapestry.isBlank(pathInfo) || pathInfo.equals("/")))
        {
            String path = hrequest.getContextPath() + _redirectPath;

            if (LOG.isDebugEnabled())
                LOG.debug(Tapestry.format("RedirectServlet.redirecting", path));

            hresponse.sendRedirect(path);
            return;
        }

        chain.doFilter(request, response);
    }

}
