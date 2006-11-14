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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.services.ClasspathResourceFactory;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.HttpServletWebActivator;

/**
 * Locates the application specification and informs the servlet service about it.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ApplicationSpecificationInitializer implements ApplicationInitializer
{
    public static final String APP_SPEC_PATH_PARAM = "org.apache.tapestry.application-specification";

    private Log _log;

    private ClasspathResourceFactory _classpathResourceFactory;

    private ApplicationGlobals _globals;

    private ISpecificationParser _parser;

    public void initialize(HttpServlet servlet)
    {
        IApplicationSpecification spec = null;

        Resource specResource = findApplicationSpecification(servlet);

        if (specResource == null)
        {
            _log.warn(ImplMessages.noApplicationSpecification(servlet));

            spec = constructStandinSpecification(servlet);
        }
        else
            spec = _parser.parseApplicationSpecification(specResource);

        _globals.storeActivator(new HttpServletWebActivator(servlet));
        _globals.storeSpecification(spec);
    }

    private Resource findApplicationSpecification(HttpServlet servlet)
    {
        String path = servlet.getInitParameter(APP_SPEC_PATH_PARAM);

        if (path != null)
            return _classpathResourceFactory.newResource(path);

        ServletContext context = servlet.getServletContext();
        String servletName = servlet.getServletName();
        String expectedName = servletName + ".application";

        Resource webInfLocation = new ContextResource(context, "/WEB-INF/");
        Resource webInfAppLocation = webInfLocation.getRelativeResource(servletName + "/");

        Resource result = check(webInfAppLocation, expectedName);
        if (result != null)
            return result;

        return check(webInfLocation, expectedName);
    }

    private Resource check(Resource resource, String name)
    {
        Resource result = resource.getRelativeResource(name);

        if (_log.isDebugEnabled())
            _log.debug("Checking for existence of " + result);

        if (result.getResourceURL() != null)
        {
            _log.debug("Found " + result);
            return result;
        }

        return null;
    }

    private IApplicationSpecification constructStandinSpecification(HttpServlet servlet)
    {
        String servletName = servlet.getServletName();

        ApplicationSpecification result = new ApplicationSpecification();

        // Pretend the file exists in the most common expected location.

        Resource virtualLocation = new ContextResource(servlet.getServletContext(), "/WEB-INF/"
                + servletName + ".application");

        result.setSpecificationLocation(virtualLocation);

        result.setName(servletName);

        return result;
    }

    public void setClasspathResourceFactory(ClasspathResourceFactory factory)
    {
        _classpathResourceFactory = factory;
    }

    public void setLog(Log log)
    {
        _log = log;
    }

    public void setGlobals(ApplicationGlobals globals)
    {
        _globals = globals;
    }

    public void setParser(ISpecificationParser parser)
    {
        _parser = parser;
    }

}
