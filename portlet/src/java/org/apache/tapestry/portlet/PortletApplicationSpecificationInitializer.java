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

package org.apache.tapestry.portlet;

import javax.portlet.PortletConfig;

import org.apache.hivemind.Resource;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;

/**
 * Locates and reads the application specification for the portlet and stores it into
 * {@link org.apache.tapestry.services.ApplicationGlobals}.
 * <p>
 * TODO: Merge this code with
 * {@link org.apache.tapestry.services.impl.ApplicationSpecificationInitializer}, they're very
 * similar. This would probably be an additional service that would do the lookup based on the
 * {@link org.apache.tapestry.web.WebActivator}&nbsp;and the {@link org.apache.tapestry.web.WebContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.services.impl.ApplicationSpecificationInitializer
 */
public class PortletApplicationSpecificationInitializer implements PortletApplicationInitializer
{
    private WebContext _context;

    private ApplicationGlobals _globals;

    private ISpecificationParser _parser;

    public void initialize(PortletConfig portletConfig)
    {
        String name = portletConfig.getPortletName();

        Resource resource = findApplicationSpecification(name);

        IApplicationSpecification specification = resource == null ? constructStandinSpecification(name)
                : _parser.parseApplicationSpecification(resource);

        _globals.storeSpecification(specification);
    }

    private Resource findApplicationSpecification(String name)
    {
        String expectedName = name + ".application";

        Resource webInfLocation = new WebContextResource(_context, "/WEB-INF/");
        Resource webInfAppLocation = webInfLocation.getRelativeResource(name + "/");

        Resource result = check(webInfAppLocation, expectedName);
        if (result != null)
            return result;

        return check(webInfLocation, expectedName);
    }

    private Resource check(Resource resource, String name)
    {
        Resource result = resource.getRelativeResource(name);

        if (result.getResourceURL() != null)
            return result;

        return null;
    }

    private IApplicationSpecification constructStandinSpecification(String name)
    {
        ApplicationSpecification result = new ApplicationSpecification();

        // Pretend the file exists in the most common expected location.

        Resource virtualLocation = new WebContextResource(_context, "/WEB-INF/" + name
                + ".application");

        result.setSpecificationLocation(virtualLocation);

        result.setName(name);

        return result;
    }

    public void setContext(WebContext context)
    {
        _context = context;
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