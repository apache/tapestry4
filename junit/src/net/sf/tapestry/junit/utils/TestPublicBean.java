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
