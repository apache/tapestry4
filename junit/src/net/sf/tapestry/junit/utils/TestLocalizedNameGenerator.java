package net.sf.tapestry.junit.utils;

import java.util.Locale;
import java.util.NoSuchElementException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.util.LocalizedNameGenerator;

/**
 *  Suite of tests for {@link net.sf.tapestry.util.LocalizedNameGenerator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class TestLocalizedNameGenerator extends TapestryTestCase
{

    public TestLocalizedNameGenerator(String name)
    {
        super(name);
    }

    public void testBasic()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("basic", Locale.US, ".test");

        assertTrue(g.more());
        assertEquals("basic_en_US.test", g.next());

        assertTrue(g.more());
        assertEquals("basic_en.test", g.next());

        assertTrue(g.more());
        assertEquals("basic.test", g.next());

        assertTrue(!g.more());
    }

    public void testNoCountry()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("noCountry", Locale.FRENCH, ".zap");

        assertTrue(g.more());
        assertEquals("noCountry_fr.zap", g.next());

        assertTrue(g.more());
        assertEquals("noCountry.zap", g.next());

        assertTrue(!g.more());        
    }
    
    public void testVariantWithoutCountry()
    {
        LocalizedNameGenerator g = new
            LocalizedNameGenerator(
            "fred",
            new Locale("en", "", "Geek"),
            ".foo");
            
    assertTrue(g.more());
    
    // The double-underscore is correct, it's a kind
    // of placeholder for the null country.
    
    assertEquals("fred_en__Geek.foo", g.next());
    
    assertTrue(g.more());
    assertEquals("fred_en.foo", g.next());
    
    assertTrue(    g.more());
    assertEquals("fred.foo", g.next());
    
    assertTrue(!g.more());   
    }   
    
    public void testNullLocale()
    {
         LocalizedNameGenerator g = new LocalizedNameGenerator("nullLocale", null, ".bar");
         
         assertTrue(g.more());
         assertEquals("nullLocale.bar", g.next());
         
         assertTrue(!g.more());
    }
    
    public void testNullSuffix()
    {
          LocalizedNameGenerator g = new LocalizedNameGenerator("nullSuffix", null, null);
          
          assertTrue(g.more());
          assertEquals("nullSuffix", g.next());
          
          assertTrue(!g.more());
    }
       
      
    
    public void testForException()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("bob", null, ".foo");
        
        assertTrue(g.more());
        assertEquals("bob.foo", g.next());
        
        assertTrue(!g.more());
        
        try
        {
            g.next();
            
         throw new AssertionFailedError("Unreachable.");
        }
        catch (NoSuchElementException ex)
        {
        }
    }
}
