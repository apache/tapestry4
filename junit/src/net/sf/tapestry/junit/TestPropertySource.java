//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
package net.sf.tapestry.junit;

import javax.servlet.ServletConfig;

import junit.framework.TestCase;
import net.sf.tapestry.IPropertySource;
import net.sf.tapestry.junit.mock.MockContext;
import net.sf.tapestry.junit.mock.MockServletConfig;
import net.sf.tapestry.junit.mock.MockSession;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.util.DelegatingPropertySource;
import net.sf.tapestry.util.PropertyHolderPropertySource;
import net.sf.tapestry.util.ServletContextPropertySource;
import net.sf.tapestry.util.ServletPropertySource;
import net.sf.tapestry.util.SystemPropertiesPropertySource;

/**
 *  Test case for several classes
 *  implementing {@link net.sf.tapestry.IPropertySource}.
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
}
