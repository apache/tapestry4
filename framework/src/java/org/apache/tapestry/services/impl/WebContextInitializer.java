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

package org.apache.tapestry.services.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.web.ServletWebContext;
import org.apache.tapestry.web.WebContext;

/**
 * Gets the context from the servlet, creates a
 * {@link org.apache.tapestry.web.ServletWebContext}, and stores that into the
 * {@link org.apache.tapestry.services.ApplicationGlobals}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class WebContextInitializer implements ApplicationInitializer
{
    private ApplicationGlobals _globals;

    public void initialize(HttpServlet servlet)
    {
        ServletContext servletContext = servlet.getServletContext();
        WebContext context = new ServletWebContext(servletContext);

        _globals.storeContext(context);
    }

    public void setGlobals(ApplicationGlobals globals)
    {
        _globals = globals;
    }
}