package net.sf.tapestry.junit.mock;

import net.sf.tapestry.junit.TapestryTestCase;

/**
 *  Test case for Mock Servlet API tests using the Simple application.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockTestCase extends TapestryTestCase
{

    public MockTestCase(String name)
    {
        super(name);
    }

    private MockTester attempt(String name)
    throws Exception
    {
        String path = "/net/sf/tapestry/junit/mock/" + name;
        
        MockTester tester = new MockTester(path);
        
        tester.execute();
        
        return tester;
    }

    // Note: JUnit runs tests in order they are defined here.  I typically
    // add new tests first since they run first that way.  Perhaps at some
    // point, I will order them differently (though since they all pass,
    // the order isn't that important).
    
    // Should also look at JUnit documentation; perhaps there's a way to
    // implicitly define tests based on the found XML files?  Possibly
    // in a static suite() method.
    
    /**
     *  Test that relative specification paths in the application specification
     *  work.
     * 
     **/
    
    public void testRelative() throws Exception
    {
        // Note, this needs to be expanded to include relative
        // paths to components and libraries.
        
        attempt("TestRelative.xml");
    }    
    
    /**
     *  Test the reset service.
     *
     **/
    
    public void testReset() throws Exception
    {
        attempt("TestReset.xml");
    }

    /**
     *  Test failure for application that doesn't provide a home page.
     * 
     **/
    
    public void testImplicitPages()
    throws Exception
    {
        attempt("TestImplicitPages.xml");
    }

    /**
     *  Test failure for application that doesn't provide a home page.
     * 
     **/
    
    public void testFailNoHome()
    throws Exception
    {
        attempt("TestFailNoHome.xml");
    }

    /**
     *  Test when the class specified for a page does not exist.
     * 
     **/
    
    public void testFailMissingClass()
    throws Exception
    {
        attempt("TestFailMissingClass.xml");
    }
    
    /**
     *  Test when the class specified for a page 
     *  does not implement {@link net.sf.tapestry.IPage}
     * 
     **/
    
    public void testFailNotPage()
    throws Exception
    {
        attempt("TestFailNotPage.xml");
    } 
    
    /**
     *  Test when the class specified for a component 
     *  does not implement {@link net.sf.tapestry.IComponent}
     * 
     **/
    
    public void testFailNotComponent()
    throws Exception
    {
        attempt("TestFailNotComponent.xml");
    }     
       
    /**
     *  Test basics including the PageLink and DirectLink (w/o parameters).
     * 
     **/
    
    public void testSimple()
    throws Exception
    {
        attempt("TestSimple.xml");
    }
    
    /**
     *  Test ability to embed component in a library and reference
     *  those components.  Also, test RenderBody component.
     * 
     **/
    
    public void testLibrary()
    throws Exception
    {
        attempt("TestLibrary.xml");
    }
       
    /**
     *  Test the External service, ServiceLink and a page implementing
     *  IExternalPage.
     * 
     **/
    
    public void testExternal()
    throws Exception
    {
        attempt("TestExternal.xml");
    }       
    
    /**
     * 
     *  Test some error cases involving the page service.
     * 
     **/
    
    public void testPage()
    throws Exception
    {
        attempt("TestPage.xml");
    }
    
    public void testLocalization()
    throws Exception
    {
        attempt("TestLocalization.xml");
    }
    
    /**
     *   Begin testing forms using the Register page.
     * 
     **/
    
    public void testRegisterForm()
    throws Exception
    {
        attempt("TestRegisterForm.xml");
    }
    
    /**
     *  Tests the validate() method, tests handling
     *  of {@link net.sf.tapestry.PageRedirectException}, and tests
     *  {@link net.sf.tapestry.callback.PageCallback} along the way.
     * 
     *  @since 2.3
     * 
     **/
    
    public void testValidate()
    throws Exception
    {
        attempt("TestValidate.xml");
    }
    
    /**
     *  Tests the use of {@link net.sf.tapestry.callback.DirectCallback}
     *  to protect a link.
     * 
     *  @since 2.3
     * 
     **/
    
    public void testProtectedLink()
    throws Exception
    {
        attempt("TestProtectedLink.xml");
    }
    
    /**
     *  Tests {@link net.sf.tapestry.StaleLinkException} with
     *  DirectLink (ActionLink and Form to come).
     * 
     **/
    
    public void testStaleLinkException()
    throws Exception
    {
        attempt("TestStaleSessionException.xml");
    }
    
    public void testStrings()
    throws Exception
    {
        attempt("TestStrings.xml");
    }
    
    /**
     *  Test case for a ValidField with a validator and client-side scripting, but
     *  no Body.
     * 
     **/
    
    public void testValidFieldNoBody()
    throws Exception
    {
        attempt("TestValidFieldNoBody.xml");
    }
    
    /**
     *  A series of tests for components where parameters are bound
     *  to expressions in the template, using the new "[[ expression ]]" syntax.
     * 
     **/
    
    public void testTemplateExpressions()
    throws Exception
    {
        attempt("TestTemplateExpr.xml");
    }
    
    public void testImplicitComponents()
    throws Exception
    {
        attempt("TestImplicitComponents.xml");
    }
    
    /**
     *  Perform basic tests of the home service.
     * 
     **/
    
    public void testHome()
    throws Exception
    {
        attempt("TestHome.xml");
    }
    
    /**
     *  Test cases where the page's template comes from
     *  a $template asset.
     * 
     **/
    
    public void testAssetTemplates()
    throws Exception
    {
        attempt("TestAssetTemplates.xml");
    }
    
    /**
     *  Test case for relative context and private assets.
     * 
     **/
    
    public void testRelativeAssets()
    throws Exception
    {
        attempt("TestRelativeAssets.xml");
    }
}
