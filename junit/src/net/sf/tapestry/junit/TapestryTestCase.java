package net.sf.tapestry.junit;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IComponentStringsSource;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.DefaultStringsSource;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.resource.ClasspathResourceLocation;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.util.IPropertyHolder;

/**
 *  Base class for Tapestry test cases.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TapestryTestCase extends TestCase
{
    protected static final boolean IS_JDK13 = System.getProperty("java.specification.version").equals("1.3");

    private IResourceResolver _resolver = new DefaultResourceResolver();

    public TapestryTestCase(String name)
    {
        super(name);
    }

    protected IPage createPage(String specificationPath, Locale locale)
    {
        IResourceLocation location = new ClasspathResourceLocation(_resolver, specificationPath);

        IComponentStringsSource source = new DefaultStringsSource();
        MockEngine engine = new MockEngine();
        engine.setComponentStringsSource(source);

        MockPage result = new MockPage();
        result.setEngine(engine);
        result.setLocale(locale);

        ComponentSpecification spec = new ComponentSpecification();
        spec.setSpecificationLocation(location);
        result.setSpecification(spec);

        return result;
    }

    protected ComponentSpecification parseComponent(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseComponentSpecification(location);
    }

    protected ComponentSpecification parsePage(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parsePageSpecification(location);
    }

    protected IApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseApplicationSpecification(location, _resolver);
    }

    protected IResourceLocation getSpecificationResourceLocation(String simpleName)
    {
        String adjustedClassName = "/" + getClass().getName().replace('.', '/') + ".class";

        IResourceLocation classResourceLocation = new ClasspathResourceLocation(_resolver, adjustedClassName);

        IResourceLocation appSpecLocation = classResourceLocation.getRelativeLocation(simpleName);
        return appSpecLocation;
    }

    protected ILibrarySpecification parseLib(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseLibrarySpecification(location, _resolver);
    }

    protected void checkList(String propertyName, String[] expected, List actual)
    {
        int count = Tapestry.size(actual);

        assertEquals(propertyName + " element count", expected.length, count);

        for (int i = 0; i < count; i++)
        {
            assertEquals("propertyName[" + i + "]", expected[i], actual.get(i));
        }
    }

    protected void checkProperty(IPropertyHolder h, String propertyName, String expectedValue)
    {
        assertEquals("Property " + propertyName + ".", expectedValue, h.getProperty(propertyName));
    }

    protected void checkException(Throwable ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError("Exception " + ex + " does not contain sub-string '" + string + "'.");
    }

    protected void unreachable()
    {
        throw new AssertionFailedError("This code should be unreachable.");
    }
}
