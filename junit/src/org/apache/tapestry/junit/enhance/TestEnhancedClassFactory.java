/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.junit.enhance;

import org.apache.commons.hivemind.ApplicationRuntimeException;
import org.apache.commons.hivemind.ClassResolver;
import org.apache.commons.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.enhance.IEnhancedClass;
import org.apache.tapestry.enhance.IEnhancedClassFactory;
import org.apache.tapestry.enhance.javassist.EnhancedClassFactory;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Tests the classes used by the 
 *  {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer} to
 *  construct new classes.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/

public class TestEnhancedClassFactory extends TapestryTestCase
{

    public TestEnhancedClassFactory(String name)
    {
        super(name);
    }


    /**
     *  Test the ability to create a class that implements a read/write property.
     * 
     **/

    public void testCreateProperty() throws Exception
    {
        ClassResolver resolver = new DefaultClassResolver();
        IEnhancedClassFactory factory = new EnhancedClassFactory(resolver);

        String className = "org.apache.tapestry.junit.enhance.PropertyHolder";
        IEnhancedClass enhancedClass = factory.createEnhancedClass(className, Object.class);

        String propertyName = "name";
        enhancedClass.createProperty(propertyName, "java.lang.String");

        Class holderClass = enhancedClass.createEnhancedSubclass();

        Object holder = holderClass.newInstance();

        String value = "abraxis";

        OgnlUtils.set("name", holder, value);

        Object actual = OgnlUtils.get("name", holder);

        assertEquals("Holder name property.", value, actual);
    }


    public void testCreateFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();
        IEnhancedClassFactory factory = new EnhancedClassFactory(resolver);

        String className = "org.apache.tapestry.junit.enhance.Invalid";
        IEnhancedClass enhancedClass = factory.createEnhancedClass(className, Boolean.class);

        try
        {
            enhancedClass.createEnhancedSubclass();

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Cannot inherit from final class");
        }

    }
}