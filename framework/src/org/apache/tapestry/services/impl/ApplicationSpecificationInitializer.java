
//Copyright 2004 The Apache Software Foundation
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package org.apache.tapestry.services.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.hivemind.Resource;
import org.apache.tapestry.resource.ContextResource;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.services.ClasspathResourceFactory;
import org.apache.tapestry.services.SpecificationParser;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Locates the application specification and informs the 
 * {@link org.apache.tapestry.services.ServletInfo} service about it.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ApplicationSpecificationInitializer implements ApplicationInitializer
{
    private Log _log;
    private ClasspathResourceFactory _classpathResourceFactory;
    private ApplicationGlobals _globals;
    private SpecificationParser _parser;

    public static final String APP_SPEC_PATH_PARAM =
        "org.apache.tapestry.application-specification";

    public void initialize(HttpServlet servlet)
    {
        IApplicationSpecification spec = null;

        Resource specResource = findApplicationSpecification(servlet);

        if (specResource == null)
        {
            _log.debug(ImplMessages.noApplicationSpecification(servlet));

            spec = constructStandinSpecification(servlet);
        }
        else
            spec = _parser.parseApplicationSpecification(specResource);

        _globals.store(servlet, spec);
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
        ApplicationSpecification result = new ApplicationSpecification();

        Resource virtualLocation = new ContextResource(servlet.getServletContext(), "/WEB-INF/");

        result.setSpecificationLocation(virtualLocation);

        result.setName(servlet.getServletName());

        // result.setResourceResolver(_resolver);

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

    public void setParser(SpecificationParser parser)
    {
        _parser = parser;
    }

}
