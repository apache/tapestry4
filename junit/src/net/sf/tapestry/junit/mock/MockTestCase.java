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
    
    /**
     *   Begin testing forms using the Register page.
     * 
     **/
    
    public void testRegisterForm()
    throws Exception
    {
        attempt("/net/sf/tapestry/junit/mock/TestRegisterForm.xml");
    }
}
