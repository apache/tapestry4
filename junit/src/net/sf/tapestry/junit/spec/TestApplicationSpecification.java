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
package net.sf.tapestry.junit.spec;

import net.sf.tapestry.IMonitor;
import net.sf.tapestry.junit.TapestryTestCase;
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
        assertEquals(
            "stringProperty",
            "Tapestry: Java Web Components",
            extension.getStringProperty());
    }

    public void testExtensionType() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        TestBean extension = (TestBean) spec.getExtension("testBean", Object.class);

        assertNotNull(extension);
    }

    public void testExtensionTypeFailClass() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", Number.class);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            checkException(ex, "does not inherit from class java.lang.Number");
        }

    }

    public void testExtensionTypeFailInterface() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", IMonitor.class);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            checkException(ex, "does not implement interface net.sf.tapestry.IMonitor");
        }

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

        parseApp("ImmediateExtension.application");

        assertEquals("instanceCount", 1, ImmediateExtension.getInstanceCount());
    }
}
