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

package org.apache.tapestry.services;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.tapestry.container.ContainerContext;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * A "global" holder for the servlet, servlet context and application specification.
 * <p>
 * Note: the servlet API portions of this will likely move to a seperate service.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ApplicationGlobals
{
    /**
     * Invoked by the (indirectly) by the servlet at init(), after parsing the application
     * specification.
     */
    public void store(HttpServlet servlet, IApplicationSpecification applicationSpecification);

    /**
     * Invoked by the (indirectly) by the servlet at init().
     */

    public void store(ContainerContext context);

    /**
     * Returns the previously stored context.
     * 
     * @see #store(ContainerContext)}.
     */

    public ContainerContext getContainerContext();

    public HttpServlet getServlet();

    public IApplicationSpecification getSpecification();

    public ServletContext getContext();

    public String getServletName();

    public ServletConfig getServletConfig();
}