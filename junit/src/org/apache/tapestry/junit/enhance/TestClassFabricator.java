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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.enhance.ClassFabricator;
import org.apache.tapestry.enhance.EnhanceClassLoader;
import org.apache.tapestry.enhance.MethodFabricator;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.junit.TapestryTestCase;

import org.apache.tapestry.util.DefaultResourceResolver;
import org.apache.tapestry.util.prop.OgnlUtils;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 *  Tests the classes used by the 
 *  {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer} to
 *  construct new classes.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class TestClassFabricator extends TapestryTestCase
{

    public TestClassFabricator(String name)
    {
        super(name);
    }

    /**
     *  Test the ability to create a class that implements a read/write property.
     * 
     **/

    public void testCreateProperty() throws Exception
    {
        String className = "org.apache.tapestry.junit.enhance.PropertyHolder";
        String fieldName = "_name";

        ClassFabricator cf = new ClassFabricator(className, "java.lang.Object");

        InstructionFactory f = cf.getInstructionFactory();

        cf.addDefaultConstructor();
        cf.addField(Type.STRING, fieldName);

        MethodFabricator mf = cf.createMethod(Constants.ACC_PUBLIC, Type.VOID, "setName");
        int nameArgIndex = mf.addArgument(Type.STRING, "name");

        InstructionList il = mf.getInstructionList();

        il.append(f.createThis());
        il.append(f.createLoad(Type.STRING, nameArgIndex));
        il.append(f.createPutField(className, fieldName, Type.STRING));
        il.append(InstructionConstants.RETURN);

        mf.commit();

        mf = cf.createMethod(Constants.ACC_PUBLIC, Type.STRING, "getName");
        il = mf.getInstructionList();

        il.append(f.createThis());
        il.append(f.createGetField(className, fieldName, Type.STRING));
        il.append(f.createReturn(Type.STRING));

        mf.commit();

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        Class holderClass = cl.defineClass(jc);

        Object holder = holderClass.newInstance();

        String value = "abraxis";

        OgnlUtils.set("name", resolver, holder, value);

        Object actual = OgnlUtils.get("name", resolver, holder);

        assertEquals("Holder name property.", value, actual);
    }

    /**
     *  Test ability to create a subclass of an existing class
     *  and implement an interface.
     * 
     **/

    public void testAddInterface() throws Exception
    {
        ClassFabricator cf =
            new ClassFabricator("org.apache.tapestry.junit.enhance.Foobar", BasePage.class.getName());

        cf.addDefaultConstructor();
        cf.addInterface(PageDetachListener.class);

        MethodFabricator mf = cf.createMethod("pageDetached");

        mf.addArgument(new ObjectType(PageEvent.class.getName()), "event");

        InstructionList il = mf.getInstructionList();

        il.append(InstructionConstants.RETURN);

        mf.commit();

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        Class holderClass = cl.defineClass(jc);

        Object instance = holderClass.newInstance();

        assertEquals("Implements interface", true, instance instanceof PageDetachListener);
    }

    public void testFailureAddArgumentsAfterLocals()
    {
        ClassFabricator cf = new ClassFabricator("Foo", "java.lang.Object");

        MethodFabricator mf = cf.createMethod("bar");

        mf.addArgument(Type.STRING, "param1");
        mf.addLocalVariable(Type.BOOLEAN, "local1");

        try
        {
            mf.addArgument(Type.LONG, "param2");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            checkException(
                ex,
                "No more arguments may be added once any local variables are added.");
        }
    }

    public void testCreateFailure()
    {
        ClassFabricator cf =
            new ClassFabricator("org.apache.tapestry.junit.enhance.Invalid", "java.lang.Boolean");

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        try
        {
            cl.defineClass(jc);

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Cannot inherit from final class");
        }

    }
}