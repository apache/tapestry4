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

package org.apache.tapestry.engine;

import java.util.ResourceBundle;

import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.PropertyHolderPropertySource;
import org.apache.tapestry.util.ResourceBundlePropertySource;
import org.apache.tapestry.util.ServletContextPropertySource;
import org.apache.tapestry.util.ServletPropertySource;
import org.apache.tapestry.util.SystemPropertiesPropertySource;

/**
 *  The default property source for application configuration properties.
 *  The search order this property source follows is the following:<br>
 *  <ul>
 *    <li>Application specification
 *    <li>Servlet properties
 *    <li>Servlet context
 *    <li>Property source extension defined in the application specification
 *    <li>System properties
 *    <li>Factory defaults
 *  </ul>
 *
 *  <p>If the application specification contains an extension
 *  named "org.apache.tapestry.property-source" it is inserted
 *  in the search path just before
 *  the property source for JVM System Properties.  This is a simple
 *  hook that allows application-specific methods of obtaining
 *  configuration values (typically, from a database or from JMX,
 *  in some way).  
 *  
 *  @author mb
 *  @since 3.1
 */
public class DefaultPropertySource implements IPropertySource
{
    /**
     *  Name of an application extension that can provide configuration properties.
     *
     *  @since 2.3
     *
     **/
    private static final String EXTENSION_PROPERTY_SOURCE_NAME =
        "org.apache.tapestry.property-source";

    private IPropertySource _degatingPropertySource;
    
	/**
	 * Creates a new default property source for looking up application-specific
	 * properties. The search order of the property source is shown in the
	 * documentation of this class.
	 * 
	 * @param context the request context for which this property source will be created
	 */
	public DefaultPropertySource(RequestContext context) {
        DelegatingPropertySource result = new DelegatingPropertySource();

        ApplicationServlet servlet = context.getServlet();
        IApplicationSpecification spec = servlet.getApplicationSpecification();

        result.addSource(new PropertyHolderPropertySource(spec));
        result.addSource(new ServletPropertySource(servlet.getServletConfig()));
        result.addSource(new ServletContextPropertySource(servlet.getServletContext()));

        if (spec.checkExtension(EXTENSION_PROPERTY_SOURCE_NAME))
        {
            IPropertySource source =
                (IPropertySource) spec.getExtension(
                    EXTENSION_PROPERTY_SOURCE_NAME,
                    IPropertySource.class);

            result.addSource(source);
        }

        result.addSource(SystemPropertiesPropertySource.getInstance());

        // Lastly, add a final source to handle "factory defaults".

        ResourceBundle bundle =
            ResourceBundle.getBundle("org.apache.tapestry.ConfigurationDefaults");

        result.addSource(new ResourceBundlePropertySource(bundle));

        _degatingPropertySource = result;
	}
	
	/**
	 * @see org.apache.tapestry.engine.IPropertySource#getPropertyValue(java.lang.String)
	 */
	public String getPropertyValue(String propertyName)
	{
		return _degatingPropertySource.getPropertyValue(propertyName);
	}
}
