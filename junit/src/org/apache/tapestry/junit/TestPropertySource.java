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

package org.apache.tapestry.junit;

import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.tapestry.junit.mock.MockContext;
import org.apache.tapestry.junit.mock.MockServletConfig;

import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.PropertyHolderPropertySource;
import org.apache.tapestry.util.ResourceBundlePropertySource;
import org.apache.tapestry.util.ServletContextPropertySource;
import org.apache.tapestry.util.ServletPropertySource;
import org.apache.tapestry.util.SystemPropertiesPropertySource;

/**
 *  Test case for several classes
 *  implementing {@link org.apache.tapestry.IPropertySource}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class TestPropertySource extends TestCase
{
    public TestPropertySource(String name)
    {
        super(name);
    }

    private void expect(IPropertySource source, String key, String expected)
    {
        String actual = source.getPropertyValue(key);

        assertEquals(key, expected, actual);
    }

    public void testServlet()
    {
        IPropertySource source = buildServletSource();

        expect(source, "alpha", "bravo");
        expect(source, "gamma", null);
    }

    private IPropertySource buildServletSource()
    {
        MockServletConfig config = new MockServletConfig(null, null);

        config.setInitParameter("alpha", "bravo");
        IPropertySource source = new ServletPropertySource(config);
        return source;
    }

    public void testServletContext()
    {
        MockContext context = new MockContext();

        context.setInitParameter("howard", "suzanne");

        IPropertySource source = new ServletContextPropertySource(context);

        expect(source, "howard", "suzanne");
        expect(source, "godzilla", null);
    }

    public void testApplicationSpecification()
    {
        ApplicationSpecification spec = new ApplicationSpecification();

        spec.setProperty("fred", "barney");

        IPropertySource source = new PropertyHolderPropertySource(spec);

        expect(source, "fred", "barney");
        expect(source, "wilma", null);
    }

    public void testSystemProperties()
    {
        IPropertySource source = SystemPropertiesPropertySource.getInstance();

        expect(source, "java.os", System.getProperty("java.os"));
        expect(source, "foo", null);
    }

    public void testDelegator()
    {
        DelegatingPropertySource source = new DelegatingPropertySource(buildServletSource());

        source.addSource(SystemPropertiesPropertySource.getInstance());

        expect(source, "java.os", System.getProperty("java.os"));
        expect(source, "alpha", "bravo");
        expect(source, "gamma", null);
    }

    public void testResourceBundle()
    {
        ResourceBundle bundle = ResourceBundle.getBundle("org.apache.tapestry.junit.Properties");

        ResourceBundlePropertySource source = new ResourceBundlePropertySource(bundle);

        expect(source, "fred", "flintstone");
        expect(source, "barney", "rubble");
        expect(source, "wilma", null);
    }
}
