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

package net.sf.tapestry.junit.spec;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.ExtensionSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;

/**
 *  Tests related to {@link net.sf.tapestry.spec.ApplicationSpecification}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestApplicationSpecification extends TapestryTestCase
{

    public TestApplicationSpecification(String name)
    {
        super(name);
    }


    public void testBasicExtension() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        TestBean extension = (TestBean) spec.getExtension("testBean");

        assertEquals("booleanProperty", true, extension.getBooleanProperty());
        assertEquals("intProperty", 18, extension.getIntProperty());
        assertEquals("longProperty", 383838L, extension.getLongProperty());
        assertEquals("doubleProperty", -3.14, extension.getDoubleProperty(), 0.0);
        assertEquals("stringProperty", "Tapestry: Java Web Components", extension.getStringProperty());
    }
    
    public void testExtensionProperty() throws Exception
    {
        IApplicationSpecification a = parseApp("ExtensionProperty.application");
        
        ExtensionSpecification e = a.getExtensionSpecification("testBean");
        
        assertEquals("Property fred.", "flintstone", e.getProperty("fred"));
    }
    
    public void testImmediateExtension() throws Exception
    {
        assertEquals("instanceCount", 0, ImmediateExtension.getInstanceCount());
        
        IApplicationSpecification a = parseApp("ImmediateExtension.application");
        
        assertEquals("instanceCount", 1, ImmediateExtension.getInstanceCount());
    }
}
