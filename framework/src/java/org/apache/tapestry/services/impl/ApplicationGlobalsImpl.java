// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebContext;

/**
 * Implementation of {@link ApplicationGlobals}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ApplicationGlobalsImpl implements ApplicationGlobals
{
    private HttpServlet _servlet;

    private ServletContext _context;

    private IApplicationSpecification _specification;

    private WebContext _webContext;

    public void store(HttpServlet servlet, IApplicationSpecification applicationSpecification)
    {
        _servlet = servlet;
        _context = servlet.getServletContext();
        _specification = applicationSpecification;
    }

    public HttpServlet getServlet()
    {
        return _servlet;
    }

    public IApplicationSpecification getSpecification()
    {
        return _specification;
    }

    public ServletContext getContext()
    {
        return _context;
    }

    public String getServletName()
    {
        return _servlet.getServletName();
    }

    public ServletConfig getServletConfig()
    {
        return _servlet.getServletConfig();
    }

    public WebContext getWebContext()
    {
        return _webContext;
    }

    public void store(WebContext context)
    {
        _webContext = context;
    }
}