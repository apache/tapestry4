package net.sf.tapestry.junit.utils;

import java.util.Date;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.tapestry.Tapestry;
import ognl.Ognl;
import ognl.OgnlException;

/**
 *  Tests to support the new OGNL oriented approach to
 *  {@link net.sf.tapestry.util.prop.IPublicBean}.  Note that this test
 *  may fail if run by itself (it is reliant on static initializers
 *  in {@link net.sf.tapestry.AbstractComponent}).
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
