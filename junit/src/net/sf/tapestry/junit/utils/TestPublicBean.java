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
package net.sf.tapestry.junit.utils;

import java.util.Date;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.tapestry.Tapestry;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Tests to support the new OGNL oriented approach to
 *  {@link net.sf.tapestry.util.prop.IPublicBean}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class TestPublicBean extends TestCase
{
    // Mostly, we want to fire the Tapestry static initializers,
    // which registers a property accessor with OGNL for
    // IPublicBean.

    private static final String TEST_STRING = Tapestry.VERSION;

    public TestPublicBean(String name)
    {
        super(name);
    }

    private void assertProperty(String propertyName, Object bean, Object expected) throws OgnlException
    {
        Object actual = Ognl.getValue(propertyName, bean);

        assertEquals("Property " + propertyName, expected, actual);
    }

    private void setAndCheck(String propertyName, Object bean, Object newValue) throws OgnlException
    {
        Ognl.setValue(propertyName, bean, newValue);

        assertProperty(propertyName, bean, newValue);
    }

    public void testReadString() throws Exception
    {
        PublicBean b = new PublicBean();

        assertProperty("stringProperty", b, b.stringProperty);
    }

    public void testUpdateString() throws Exception
    {
        PublicBean b = new PublicBean();

        setAndCheck("stringProperty", b, TEST_STRING);
    }

    public void testReadObjectProperty() throws Exception
    {
        PublicBean b = new PublicBean();

        assertProperty("objectProperty", b, b.objectProperty);
    }

    public void testUpdateObjectProperty() throws Exception
    {
        PublicBean b = new PublicBean();

        setAndCheck("objectProperty", b, new Date());
    }

    public void testReadLongProperty() throws Exception
    {
        PublicBean b = new PublicBean();

        assertProperty("longProperty", b, new Long(b.longProperty));
    }

    public void testUpdateLongProperty() throws Exception
    {
        PublicBean b = new PublicBean();

        setAndCheck("longProperty", b, new Long(System.currentTimeMillis()));
    }

    public void testReadSyntheticProperty() throws Exception
    {
        PublicBean b = new PublicBean();

        assertProperty("syntheticProperty", b, new Double(3.14));
    }

    public void testReadPrivateProperty()
    {
        PublicBean b = new PublicBean();

        try
        {
            Ognl.getValue("privateLongProperty", b);
        }
        catch (OgnlException ex)
        {
            // Expected.
            return;
        }

        throw new AssertionFailedError("Should not be able to access private property.");
    }
}
