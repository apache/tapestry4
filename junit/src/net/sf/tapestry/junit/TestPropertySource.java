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
