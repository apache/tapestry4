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
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;

/**
 *  Test cases for page and component specifications.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestComponentSpecification extends TapestryTestCase
{

    public TestComponentSpecification(String name)
    {
        super(name);
    }

    public void testBeanProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("BeanProperty.jwc");
        BeanSpecification fred = s.getBeanSpecification("fred");

        checkList("propertyNames", new String[] { "bruce", "nicole", "zeta" }, fred.getPropertyNames());

        checkProperty(fred, "bruce", "wayne");
        checkProperty(fred, "nicole", "kidman");
        checkProperty(fred, "zeta", "jones");

    }

    public void testComponentProperty() throws Exception
    {
        ComponentSpecification s = parseComponent("ComponentProperty.jwc");
        ContainedComponent c = s.getComponent("barney");

        checkList("propertyNames", new String[] { "apple", "chocolate", "frozen" }, c.getPropertyNames());

        checkProperty(c, "apple", "pie");
        checkProperty(c, "chocolate", "cake");
        checkProperty(c, "frozen", "yogurt");

    }
}
