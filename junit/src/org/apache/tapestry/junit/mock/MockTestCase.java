/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.junit.mock;

import java.io.File;

import org.apache.tapestry.junit.TapestryTestCase;

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

    private MockTester attempt(String name) throws Exception
    {
        String path = "/org/apache/tapestry/junit/mock/" + name;

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
    // in a static suite() method?

	/**
	 *  Tests {@link org.apache.tapestry.form.TextArea} and
	 *  {@link org.apache.tapestry.html.InsertText}.
	 * 
	 **/
	
	public void testTextArea()
	throws Exception
	{
		attempt("TestTextArea.xml");
	}

	/**
	 *  Tests for file uploads and the Upload component.
	 * 
	 **/
	
	public void testUpload()
	throws Exception
	{
		attempt("TestUpload.xml");
	}

	/**
	 *  Tests for the Radio and RadioGroup components.
	 * 
	 **/
	
	public void testRadio()
	throws Exception
	{
		attempt("TestRadio.xml");
	}

	/**
	 *  Tests for the TextField component.
	 * 
	 **/
	
	public void testTextField()
	throws Exception
	{
		attempt("TestTextField.xml");
	}

	/**
	 *  Test downloading content via the asset service.
	 * 
	 **/
	
	public void testAssetService()
	throws Exception
	{
		attempt("TestAssetService.xml");
	}

	/**
	 *  Test externalization of private assets.
	 * 
	 **/
	
	public void testAssets()
	throws Exception
	{
		deleteDir(".private");	
		
		// Run the test twice; this catches some coverage related to
		// files already being externalized.
		
		attempt("TestAssets.xml");
		attempt("TestAssets.xml");		
	}

	/**
	 *  Test the ListEdit component.
	 * 
	 **/
	
	public void testListEdit()
	throws Exception
	{
		attempt("TestListEdit.xml");
	}

	/**
	 *  Test handling of internal and external redirects.
	 * 
	 **/
	
	public void testRedirect()
	throws Exception
	{
		attempt("TestRedirect.xml");
	}

	/**
	 *  Test ability of the enhancer to create properties for
	 *  connected parameters.
	 * 
	 **/
	
	public void testEnhancedParameterProperties()
	throws Exception
	{
		attempt("TestEnhancedParameterProperties.xml");
	}

	/**
	 *  Tests the Select and Option components (and a bit of Form as well).
	 * 
	 **/
	
	public void testSelectOption()
	throws Exception
	{
		attempt("TestSelectOption.xml");
	}

	/**
	 *  Tests related to link renderers.
	 * 
	 **/
	
	public void testLinkRenderers()
	throws Exception
	{
		attempt("TestLinkRenderers.xml");
	}

	/**
	 *  Tests related to specified properties.
	 * 
	 **/
	
	public void testPropertySpecification()
	throws Exception
	{
		attempt("TestPropertySpecification.xml");
	}

	/**
	 *  Tests ability to override default template extension.
	 * 
	 **/
	
	public void testTemplateExtension()
	throws Exception
	{
		attempt("TestTemplateExtension.xml");
	}

    /**
     *  Tests related to the listener binding (added in 1.4 DTD).
     * 
     **/
    
    public void testListenerBinding()
    throws Exception
    {
        attempt("TestListenerBinding.xml");
    }

    /**
     *  Test that default class names for pages and components work.
     * 
     **/
    
    public void testDefaultComponentClass()
    throws Exception
    {
        attempt("TestDefaultComponentClass.xml");
    }
    
    /**
     *  Test that the default class for pages can be overridden
     *  with a configuration parameter.
     * 
     **/
    
    public void testOverrideDefaultComponentClass()
    throws Exception
    {
        attempt("TestOverrideDefaultComponentClass.xml");
    }

    public void testPersistentProperties()
    throws Exception
    {
        attempt("TestPersistentProperties.xml");
    }

    /**
     *  Test several Stale Link scenarios for the Form component.
     * 
     **/
    
    public void testStaleForm()
    throws Exception
    {
        attempt("TestStaleForm.xml");
    }

    /**
     *  Test Block and InsertBlock, especially w.r.t. links and forms
     *  inside the Block on foriegn pages.
     * 
     **/

    public void testBlock() throws Exception
    {
        attempt("TestBlock.xml");
    }

    /**
     *  Test behavior when the application specification doesn't exist.
     * 
     **/

    public void testMissingAppSpec() throws Exception
    {
        attempt("TestMissingAppSpec.xml");
    }

    /**
     *   Demonstrates that libraries defined in application specifications
     *   within the context can still be located in the classpath.
     * 
     **/

    public void testLibraryInWebInfApplication() throws Exception
    {
        attempt("TestLibraryInWebInfApplication.xml");
    }

    /**
     *  Test ability to search for components specifications for 
     *  component types in the application namespace.
     * 
     **/

    public void testSearchComponents() throws Exception
    {
        attempt("TestSearchComponents.xml");
    }

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
     *  Test ability to search for page specifications for pages
     *  in the application namespace.
     * 
     **/

    public void testSearchPages() throws Exception
    {
        attempt("TestSearchPages.xml");
    }

    /**
     *  Test failure for application that doesn't provide a home page.
     * 
     **/

    public void testFailNoHome() throws Exception
    {
        attempt("TestFailNoHome.xml");
    }

    /**
     *  Test when the class specified for a page does not exist.
     * 
     **/

    public void testFailMissingClass() throws Exception
    {
        attempt("TestFailMissingClass.xml");
    }

    /**
     *  Test when the class specified for a page 
     *  does not implement {@link org.apache.tapestry.IPage}
     * 
     **/

    public void testFailNotPage() throws Exception
    {
        attempt("TestFailNotPage.xml");
    }

    /**
     *  Test when the class specified for a component 
     *  does not implement {@link org.apache.tapestry.IComponent}
     * 
     **/

    public void testFailNotComponent() throws Exception
    {
        attempt("TestFailNotComponent.xml");
    }

    /**
     *  Test basics including the PageLink and DirectLink (w/o parameters).
     * 
     **/

    public void testSimple() throws Exception
    {
        attempt("TestSimple.xml");
    }

    /**
     *  Test ability to embed component in a library and reference
     *  those components.  Also, test RenderBody component.
     * 
     **/

    public void testLibrary() throws Exception
    {
        attempt("TestLibrary.xml");
    }

    /**
     *  Test the External service, ServiceLink and a page implementing
     *  IExternalPage.
     * 
     **/

    public void testExternal() throws Exception
    {
        attempt("TestExternal.xml");
    }

    /**
     * 
     *  Test some error cases involving the page service.
     * 
     **/

    public void testPage() throws Exception
    {
        attempt("TestPage.xml");
    }

    public void testLocalization() throws Exception
    {
        attempt("TestLocalization.xml");
    }

    /**
     *   Begin testing forms using the Register page.
     * 
     **/

    public void testRegisterForm() throws Exception
    {
        attempt("TestRegisterForm.xml");
    }

    /**
     *  Tests the validate() method, tests handling
     *  of {@link org.apache.tapestry.PageRedirectException}, and tests
     *  {@link org.apache.tapestry.callback.PageCallback} along the way.
     * 
     *  @since 2.3
     * 
     **/

    public void testValidate() throws Exception
    {
        attempt("TestValidate.xml");
    }

    /**
     *  Tests the use of {@link org.apache.tapestry.callback.DirectCallback}
     *  to protect a link.
     * 
     *  @since 2.3
     * 
     **/

    public void testProtectedLink() throws Exception
    {
        attempt("TestProtectedLink.xml");
    }

    /**
     *  Tests {@link org.apache.tapestry.StaleSessionException} with
     *  DirectLink (ActionLink and Form to come).
     * 
     **/

    public void testStaleSessionException() throws Exception
    {
        attempt("TestStaleSessionException.xml");
    }

    public void testStrings() throws Exception
    {
        attempt("TestStrings.xml");
    }

    /**
     *  Test case for a ValidField with a validator and client-side scripting, but
     *  no Body.
     * 
     **/

    public void testValidFieldNoBody() throws Exception
    {
        attempt("TestValidFieldNoBody.xml");
    }

    /**
     *  A series of tests for components where parameters are bound
     *  to expressions in the template, using the new "[[ expression ]]" syntax.
     * 
     **/

    public void testTemplateExpressions() throws Exception
    {
        attempt("TestTemplateExpr.xml");
    }

    public void testImplicitComponents() throws Exception
    {
        attempt("TestImplicitComponents.xml");
    }

    /**
     *  Perform basic tests of the home service.
     * 
     **/

    public void testHome() throws Exception
    {
        attempt("TestHome.xml");
    }

    /**
     *  Test cases where the page's template comes from
     *  a $template asset.
     * 
     **/

    public void testAssetTemplates() throws Exception
    {
        attempt("TestAssetTemplates.xml");
    }

    /**
     *  Test case for relative context and private assets.
     * 
     **/

    public void testRelativeAssets() throws Exception
    {
        attempt("TestRelativeAssets.xml");
    }
    
    private void deleteDir(String path)
    throws Exception
    {
    	File file = new File(path);
    	
    	if (!file.exists())
    		return;
    		
 		deleteRecursive(file);
    }
    
    private void deleteRecursive(File file)
    {
    	if (file.isFile())
    	{
    		file.delete();
    		return;
    	}
    	
    	String[] names = file.list();
    	
    	for (int i = 0; i < names.length; i++)
    	{
    		File f = new File(file, names[i]);
    		deleteRecursive(f);
    	}
    	
    	file.delete();
    }
}
