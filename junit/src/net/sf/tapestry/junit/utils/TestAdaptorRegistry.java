package net.sf.tapestry.junit.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.IImmutable;

/**
 *  Tests the {@link net.sf.tapestry.util.AdaptorRegistry} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class TestAdaptorRegistry extends TapestryTestCase
{

    public TestAdaptorRegistry(String name)
    {
        super(name);
    }

    private AdaptorRegistry build()
    {
        AdaptorRegistry result = new AdaptorRegistry();

        result.register(Object.class, "OBJECT");
        result.register(Object[].class, "OBJECT[]");
        result.register(String.class, "STRING");
        result.register(IImmutable.class, "IIMMUTABLE");
        result.register(List.class, "LIST");
        result.register(Map.class, "MAP");
        result.register(Serializable.class, "SERIALIZABLE");
        result.register(int[].class, "INT[]");
        result.register(double.class, "DOUBLE");
        result.register(Number[].class, "NUMBER[]");

        return result;
    }

    private void expect(String expected, Class subjectClass)
    {
        Object actual = build().getAdaptor(subjectClass);

        assertEquals(expected, actual);
    }

    public void testDefaultMatch()
    {
        expect("OBJECT", TestAdaptorRegistry.class);
    }

    public void testClassBeforeInterface()
    {
        expect("STRING", String.class);
    }

    public void testInterfaceMatch()
    {
        expect("SERIALIZABLE", Boolean.class);
    }

    public void testObjectArrayMatch()
    {
        expect("OBJECT[]", Object[].class);
    }

    public void testObjectSubclassArray()
    {
        expect("OBJECT[]", String[].class);
    }
    
    public void testRegisteredSubclassArray()
    {
    	expect("NUMBER[]", Number[].class);
    }

    public void testScalarArrayMatch()
    {
        expect("INT[]", int[].class);
    }

    public void testScalarArrayDefault()
    {
        // This won't change, scalar arrays can't be cast to Object[].

        expect("SERIALIZABLE", short[].class);
    }

    public void testScalar()
    {
        expect("DOUBLE", double.class);
    }

    public void testScalarDefault()
    {
        expect("OBJECT", float.class);
    }

    public void testSearchNoInterfaces()
    {
        expect("OBJECT", Object.class);
    }

    public void testNoMatch()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        try
        {
            r.getAdaptor(Boolean.class);

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("Could not find an adaptor for class java.lang.Boolean.", ex.getMessage());
        }
    }

    public void testToString()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        assertEquals("AdaptorRegistry[java.lang.String=STRING]", r.toString());
    }

    public void testDuplicateRegistration()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        try
        {

            r.register(String.class, "STRING2");

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("A registration for class java.lang.String already exists.", ex.getMessage());
        }
    }
}
