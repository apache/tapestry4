//
// Tapestry Web Application Framework
// Copyright (c) 2002 by Howard Lewis Ship
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

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import net.sf.tapestry.IComponentStringsSource;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.DefaultStringsSource;
import net.sf.tapestry.engine.ResourceResolver;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.LibrarySpecification;
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

    public TapestryTestCase(String name)
    {
        super(name);
    }

    protected IPage createPage(String specificationPath, Locale locale)
    {
        IResourceResolver resolver = new ResourceResolver(this);
        IComponentStringsSource source = new DefaultStringsSource(resolver);
        MockEngine engine = new MockEngine();
        engine.setComponentStringsSource(source);

        MockPage result = new MockPage();
        result.setEngine(engine);
        result.setLocale(locale);

        ComponentSpecification spec = new ComponentSpecification();
        spec.setSpecificationResourcePath(specificationPath);
        result.setSpecification(spec);

        return result;
    }

    protected ComponentSpecification parseComponent(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseComponentSpecification(input, simpleName);
    }


    protected ApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseApplicationSpecification(input, simpleName, new ResourceResolver(this));
    }
    
    protected LibrarySpecification parseLib(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser();

        InputStream input = getClass().getResourceAsStream(simpleName);

        return parser.parseLibrarySpecification(input, simpleName, new ResourceResolver(this));
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
        assertEquals("Property " + propertyName + ".",
            expectedValue,
            h.getProperty(propertyName));
    }


}
