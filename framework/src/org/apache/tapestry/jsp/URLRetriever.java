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
