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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.util.AdaptorRegistry;
import net.sf.tapestry.util.IImmutable;

/**
 *  Tests the {@link net.sf.tapestry.util.AdaptorRegistry} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class TestAdaptorRegistry extends TapestryTestCase
{

    public TestAdaptorRegistry(String name)
    {
        super(name);
    }

    private AdaptorRegistry build()
    {
        AdaptorRegistry result = new AdaptorRegistry();

        result.register(Object.class, "OBJECT");
        result.register(Object[].class, "OBJECT[]");
        result.register(String.class, "STRING");
        result.register(IImmutable.class, "IIMMUTABLE");
        result.register(List.class, "LIST");
        result.register(Map.class, "MAP");
        result.register(Serializable.class, "SERIALIZABLE");
        result.register(int[].class, "INT[]");
        result.register(double.class, "DOUBLE");
        result.register(Number[].class, "NUMBER[]");

        return result;
    }

    private void expect(String expected, Class subjectClass)
    {
        Object actual = build().getAdaptor(subjectClass);

        assertEquals(expected, actual);
    }

    public void testDefaultMatch()
    {
        expect("OBJECT", TestAdaptorRegistry.class);
    }

    public void testClassBeforeInterface()
    {
        expect("STRING", String.class);
    }

    public void testInterfaceMatch()
    {
        expect("SERIALIZABLE", Boolean.class);
    }

    public void testObjectArrayMatch()
    {
        expect("OBJECT[]", Object[].class);
    }

    public void testObjectSubclassArray()
    {
        expect("OBJECT[]", String[].class);
    }
    
    public void testRegisteredSubclassArray()
    {
    	expect("NUMBER[]", Number[].class);
    }

    public void testScalarArrayMatch()
    {
        expect("INT[]", int[].class);
    }

    public void testScalarArrayDefault()
    {
        // This won't change, scalar arrays can't be cast to Object[].

        expect("SERIALIZABLE", short[].class);
    }

    public void testScalar()
    {
        expect("DOUBLE", double.class);
    }

    public void testScalarDefault()
    {
        expect("OBJECT", float.class);
    }

    public void testSearchNoInterfaces()
    {
        expect("OBJECT", Object.class);
    }

    public void testNoMatch()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        try
        {
            r.getAdaptor(Boolean.class);

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("Could not find an adaptor for class java.lang.Boolean.", ex.getMessage());
        }
    }

    public void testToString()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        assertEquals("AdaptorRegistry[java.lang.String=STRING]", r.toString());
    }

    public void testDuplicateRegistration()
    {
        AdaptorRegistry r = new AdaptorRegistry();

        r.register(String.class, "STRING");

        try
        {

            r.register(String.class, "STRING2");

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("A registration for class java.lang.String already exists.", ex.getMessage());
        }
    }
}
