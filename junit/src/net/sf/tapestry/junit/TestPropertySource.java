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
package net.sf.tapestry.junit;

import java.util.ResourceBundle;

import junit.framework.TestCase;

import net.sf.tapestry.IPropertySource;
import net.sf.tapestry.junit.mock.MockContext;
import net.sf.tapestry.junit.mock.MockServletConfig;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.util.DelegatingPropertySource;
import net.sf.tapestry.util.PropertyHolderPropertySource;
import net.sf.tapestry.util.ResourceBundlePropertySource;
import net.sf.tapestry.util.ServletContextPropertySource;
import net.sf.tapestry.util.ServletPropertySource;
import net.sf.tapestry.util.SystemPropertiesPropertySource;

/**
 *  Test case for several classes
 *  implementing {@link net.sf.tapestry.IPropertySource}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class TestPropertySource extends TestCase
{
    public TestPropertySource(String name)
    {
        super(name);
    }

    private void expect(IPropertySource source, String key, String expected)
    {
        String actual = source.getPropertyValue(key);

        assertEquals(key, expected, actual);
    }

    public void testServlet()
    {
        IPropertySource source = buildServletSource();

        expect(source, "alpha", "bravo");
        expect(source, "gamma", null);
    }

    private IPropertySource buildServletSource()
    {
        MockServletConfig config = new MockServletConfig(null, null);

        config.setInitParameter("alpha", "bravo");
        IPropertySource source = new ServletPropertySource(config);
        return source;
    }

    public void testServletContext()
    {
        MockContext context = new MockContext();

        context.setInitParameter("howard", "suzanne");

        IPropertySource source = new ServletContextPropertySource(context);

        expect(source, "howard", "suzanne");
        expect(source, "godzilla", null);
    }

    public void testApplicationSpecification()
    {
        ApplicationSpecification spec = new ApplicationSpecification();

        spec.setProperty("fred", "barney");

        IPropertySource source = new PropertyHolderPropertySource(spec);

        expect(source, "fred", "barney");
        expect(source, "wilma", null);
    }

    public void testSystemProperties()
    {
        IPropertySource source = SystemPropertiesPropertySource.getInstance();

        expect(source, "java.os", System.getProperty("java.os"));
        expect(source, "foo", null);
    }

    public void testDelegator()
    {
        DelegatingPropertySource source = new DelegatingPropertySource(buildServletSource());

        source.addSource(SystemPropertiesPropertySource.getInstance());

        expect(source, "java.os", System.getProperty("java.os"));
        expect(source, "alpha", "bravo");
        expect(source, "gamma", null);
    }

    public void testResourceBundle()
    {
        ResourceBundle bundle = ResourceBundle.getBundle("net.sf.tapestry.junit.Properties");

        ResourceBundlePropertySource source = new ResourceBundlePropertySource(bundle);

        expect(source, "fred", "flintstone");
        expect(source, "barney", "rubble");
        expect(source, "wilma", null);
    }
}
