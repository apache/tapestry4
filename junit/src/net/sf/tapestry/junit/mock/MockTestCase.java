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

    private MockTester attempt(String path)
    throws Exception
    {
        MockTester tester = new MockTester(path);
        
        tester.execute();
        
        return tester;
    }

    /**
     *  Test basics including the PageLink and DirectLink (w/o parameters).
     * 
     **/
    
    public void testSimple()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestSimple.xml");
    }
    
    /**
     *  Test ability to embed component in a library and reference
     *  those components.  Also, test RenderBody component.
     * 
     **/
    
    public void testLibrary()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestLibrary.xml");
    }
       
    /**
     *  Test the External service, ServiceLink and a page implementing
     *  IExternalPage.
     * 
     **/
    
    public void testExternal()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestExternal.xml");
    }       
    
    /**
     * 
     *  Test some error cases involving the page service.
     * 
     **/
    
    public void testPage()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestPage.xml");
    }
    
    public void testLocalization()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestLocalization.xml");
    }
    
    /**
     *   Begin testing forms using the Register page.
     * 
     **/
    
    public void testRegisterForm()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestRegisterForm.xml");
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
        attempt("/net/sf/tapestry/junit/mock/TestValidate.xml");
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
        attempt("/net/sf/tapestry/junit/mock/TestProtectedLink.xml");
    }
    
    /**
     *  Tests {@link net.sf.tapestry.StaleLinkException} with
     *  DirectLink (ActionLink and Form to come).
     * 
     **/
    
    public void testStaleLinkException()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestStaleSessionException.xml");
    }
    
    public void testStrings()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestStrings.xml");
    }
    
    /**
     *  Test case for a ValidField with a validator and client-side scripting, but
     *  no Body.
     * 
     **/
    
    public void testValidFieldNoBody()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestValidFieldNoBody.xml");
    }
    
    /**
     *  A series of tests for components where parameters are bound
     *  to expressions in the template, using the new "[[ expression ]]" syntax.
     * 
     **/
    
    public void testTemplateExpressions()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestTemplateExpr.xml");
    }
    
    public void testImplicitComponents()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestImplicitComponents.xml");
    }
}
