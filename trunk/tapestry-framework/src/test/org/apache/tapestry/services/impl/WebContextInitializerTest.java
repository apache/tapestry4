// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.ApplicationGlobals;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * Tests for {@link org.apache.tapestry.services.impl.WebContextInitializer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class WebContextInitializerTest extends BaseComponentTestCase
{

    public void testInitializer()
    throws Exception
    {
        HttpServlet servlet = new ServletFixture();
        
        ServletContext servletContext = newMock(ServletContext.class);
        
        ServletConfig config = newMock(ServletConfig.class);
        
        expect(config.getServletContext()).andReturn(servletContext);
        
        replay();
        
        servlet.init(config);
        
        ApplicationGlobals globals = new ApplicationGlobalsImpl();
        
        WebContextInitializer initializer = new WebContextInitializer();
        
        initializer.setGlobals(globals);
        
        initializer.initialize(servlet);
        
        verify();
        
        assertSame(servletContext, globals.getServletContext());
        assertNotNull(globals.getWebContext());
    }

}
