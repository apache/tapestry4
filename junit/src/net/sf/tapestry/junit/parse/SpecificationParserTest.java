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

package net.sf.tapestry.junit.parse;

import java.io.InputStream;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.ResourceResolver;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.LibrarySpecification;
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

public class SpecificationParserTest extends TestCase
{

    public SpecificationParserTest(String name)
    {
        super(name);
    }

    private ComponentSpecification parseComponent(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseComponentSpecification(input, simpleName);
    }

    /** @since 2.2 **/

    private ApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseApplicationSpecification(input, simpleName, new ResourceResolver(this));
    }
    
    /** @since 2.2 **/

    private LibrarySpecification parseLib(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseLibrarySpecification(input, simpleName, new ResourceResolver(this));
    }
        
    private void checkList(String propertyName, String[] expected, List actual)
    {
        int count = Tapestry.size(actual);
        
        assertEquals(propertyName + " element count", expected.length, count);
        
        for (int i = 0; i < count; i++)
        {
            assertEquals("propertyName[" + i + "]", expected[i], actual.get(i));
        }                  
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
            LibrarySpecification spec = parseLib("InvalidLibraryId.library");

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
        LibrarySpecification spec = parseLib("ValidLibrary.library");
        
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
            ApplicationSpecification spec = parseApp("InvalidPageName.application");

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
            ApplicationSpecification spec = 
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
            ApplicationSpecification spec = 
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
            ApplicationSpecification spec = 
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