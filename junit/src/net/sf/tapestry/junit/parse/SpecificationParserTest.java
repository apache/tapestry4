/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.junit.parse;

import junit.framework.AssertionFailedError;
import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.xml.DocumentParseException;



/**
 *  Tests the specification parser (which reads page and component
 *  specifications).  Came into being somewhat late, so it just
 *  tests new features for the meantime.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class SpecificationParserTest extends TapestryTestCase
{

    public SpecificationParserTest(String name)
    {
        super(name);
    }

    /** 
     * 
     *  Asserts that the exception message contains the
     *  indicated substring.
     * 
     *  @since 2.2 
     * 
     **/

    private void checkException(DocumentParseException ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError("Exception " + ex + " does not contain sub-string '" + string + "'.");
    }

    /**
     *  Tests that the parser can handle a 1.2 specification
     *  that includes a &lt;string-binding&gt; element.
     * 
     **/

    public void testStringBinding() throws Exception
    {
        ComponentSpecification spec = parseComponent("TestStringBinding.jwc");

        BindingSpecification bs = spec.getComponent("hello").getBinding("value");

        assertEquals("type", BindingType.STRING, bs.getType());
        assertEquals("key", "label.hello", bs.getValue());
    }

    /**
     *  Test valid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testValidParameterName() throws Exception
    {
        ComponentSpecification spec = parseComponent("ValidParameterName.jwc");

        ParameterSpecification ps = spec.getParameter("valid");

        assertNotNull("Parameter specification.", ps);
    }

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidParameterName() throws Exception
    {
        try
        {
            ComponentSpecification spec = parseComponent("InvalidParameterName.jwc");

            throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in-valid");
            checkException(ex, "Parameter");
        }
    }

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidComponentId() throws Exception
    {
        try
        {
            ComponentSpecification spec = parseComponent("InvalidComponentId.jwc");

            throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "component id");
        }
    }

    /**
     *  Test invalid library id in a library specification.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidLibraryId() throws Exception
    {
        try
        {
            ILibrarySpecification spec = parseLib("InvalidLibraryId.library");

            throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "library id");
        }
    }

    /**
     *  Parse a valid library.
     * 
     *  @since 2.2
     * 
     **/
    
    public void testValidLibrary() throws Exception
    {
        ILibrarySpecification spec = parseLib("ValidLibrary.library");
        
        checkList("serviceNames",
            new String[] { "service1", "service2" },
            spec.getServiceNames());
            
        checkList("pageNames", 
            new String[] { "FirstPage", "SecondPage" },
            spec.getPageNames());
            
        checkList("componentAliases",
            new String[] { "FirstComponent", "SecondComponent" },
            spec.getComponentAliases());
            
        checkList("libraryIds",
            new String[] { "lib1", "lib2" },
            spec.getLibraryIds());
    } 
    
    

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidAssetName() throws Exception
    {
        try
        {
            ComponentSpecification spec = parseComponent("InvalidAssetName.jwc");

            throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "asset name");
        }
    }

    /**
     *  Test invalid page name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidPageName() throws Exception
    {
        try
        {
            IApplicationSpecification spec = parseApp("InvalidPageName.application");

            throw new AssertionFailedError("Should not be able to parse document.");

        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in$valid");
            checkException(ex, "page name");
        }
    }
        
    /**
     *  Test invalid service name.
     * 
     *  @since 2.2
     * 
     **/
    
    public void testInvalidServiceName()
    throws Exception
    {
        try
        {
            IApplicationSpecification spec = 
                parseApp("InvalidServiceName.application");
                
             throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in$valid");
            checkException(ex, "service");
        }
    }
        
    /**
     *  Test invalid service name.
     * 
     *  @since 2.2
     * 
     **/
    
    public void testInvalidComponentAlias()
    throws Exception
    {
        try
        {
            IApplicationSpecification spec =  
                parseApp("InvalidComponentAlias.application");
                
             throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Invalid$Component");
            checkException(ex, "alias");
        }
    }   
    
    /**
     *  Test invalid extension name.
     * 
     *  @since 2.2
     * 
     **/
    
    public void testInvalidExtensionName()
    throws Exception
    {
        try
        {
            IApplicationSpecification spec = 
                parseApp("InvalidExtensionName.application");
                
             throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Invalid$Extension");
            checkException(ex, "extension name");
        }
    }      
              
}