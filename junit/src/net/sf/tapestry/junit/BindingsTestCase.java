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

import java.util.HashMap;

import junit.framework.AssertionFailedError;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.ReadOnlyBindingException;
import net.sf.tapestry.binding.AbstractBinding;
import net.sf.tapestry.binding.ExpressionBinding;
import net.sf.tapestry.binding.FieldBinding;
import net.sf.tapestry.binding.StaticBinding;

/**
 *  Do tests of bindings.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class BindingsTestCase extends TapestryTestCase
{
    public static final String STRING_FIELD = "stringField";

    public static final Object NULL_FIELD = null;
    
    private IResourceResolver _resolver = new DefaultResourceResolver();

    private static class TestBinding extends AbstractBinding
    {
        private Object _object;

        private TestBinding(Object object)
        {
            _object = object;
        }

        public Object getObject()
        {
            return _object;
        }

    }

    public class BoundPage extends MockPage
    {
        private boolean _booleanValue;
        private int _intValue;
        private double _doubleValue;
        private Object _objectValue;
        
        public boolean getBooleanValue()
        {
            return _booleanValue;
        }

        public double getDoubleValue()
        {
            return _doubleValue;
        }

        public int getIntValue()
        {
            return _intValue;
        }

        public Object getObjectValue()
        {
            return _objectValue;
        }

        public void setBooleanValue(boolean booleanValue)
        {
            _booleanValue = booleanValue;
        }

        public void setDoubleValue(double doubleValue)
        {
            _doubleValue = doubleValue;
        }

        public void setIntValue(int intValue)
        {
            _intValue = intValue;
        }

        public void setObjectValue(Object objectValue)
        {
            _objectValue = objectValue;
        }

    }    

    public BindingsTestCase(String name)
    {
        super(name);
    }

    public void testGetObject()
    {
        IBinding binding =
            new FieldBinding(_resolver, "net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD");

        assertEquals("Object", STRING_FIELD, binding.getObject());
    }

    public void testToString()
    {
        IBinding binding =
            new FieldBinding(_resolver, "net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD");

        assertEquals(
            "String value (before access)",
            "FieldBinding[net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD]",
            binding.toString());

        binding.getObject();

        assertEquals(
            "String value (after access)",
            "FieldBinding[net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD (stringField)]",
            binding.toString());
    }

    public void testJavaLang()
    {
        IBinding binding = new FieldBinding(_resolver, "Boolean.TRUE");

        assertEquals("Object", Boolean.TRUE, binding.getObject());
    }

    public void testMissingClass()
    {
        IBinding binding = new FieldBinding(_resolver, "foo.bar.Baz.FIELD");

        try
        {

            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to resolve class foo.bar.Baz.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testMissingField()
    {
        IBinding binding =
            new FieldBinding(_resolver, "net.sf.tapestry.junit.BindingsTestCase.MISSING_FIELD");

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Field net.sf.tapestry.junit.BindingsTestCase.MISSING_FIELD does not exist.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public final String INSTANCE_FIELD = "InstanceField";

    public void testInstanceAccess()
    {
        IBinding binding =
            new FieldBinding(_resolver, "net.sf.tapestry.junit.BindingsTestCase.INSTANCE_FIELD");

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(
                ex,
                "Field net.sf.tapestry.junit.BindingsTestCase.INSTANCE_FIELD is an instance variable, not a class variable.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testStaticBindingInt()
    {
        IBinding binding = new StaticBinding("107");

        assertEquals("Int", 107, binding.getInt());

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Int", 107, binding.getInt());
    }

    public void testInvalidStaticBindingInt()
    {
        IBinding binding = new StaticBinding("barney");

        try
        {
            binding.getInt();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (NumberFormatException ex)
        {
            checkException(ex, "barney");
        }
    }

    public void testInvalidStaticBindingDouble()
    {
        IBinding binding = new StaticBinding("fred");

        try
        {
            binding.getDouble();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (NumberFormatException ex)
        {
            checkException(ex, "fred");
        }
    }

    public void testStaticBindingDouble()
    {
        IBinding binding = new StaticBinding("3.14");

        assertEquals("Double", 3.14, binding.getDouble(), 0.);

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Double", 3.14, binding.getDouble(), 0.);
    }

    public void testStaticBindingToString()
    {
        IBinding binding = new StaticBinding("alfalfa");

        assertEquals("ToString", "StaticBinding[alfalfa]", binding.toString());
    }

    public void testStaticBindingObject()
    {
        String value = Long.toString(System.currentTimeMillis());

        IBinding binding = new StaticBinding(value);

        assertEquals("Object", value, binding.getObject());
    }

    public void testGetIntWithNull()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.getInt();
        }
        catch (NullValueForBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testGetDoubleWithNull()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.getDouble();
        }
        catch (NullValueForBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public static final double DOUBLE_FIELD = 3.14;

    public void testGetDoubleWithDouble()
    {
        IBinding binding = new TestBinding(new Double(3.14));

        assertEquals("Double", 3.14, binding.getDouble(), 0);
    }

    public void testGetDoubleWithBoolean()
    {
        IBinding binding = new TestBinding(Boolean.TRUE);

        assertEquals("Double", 1.0, binding.getDouble(), 0);

        binding = new TestBinding(Boolean.FALSE);

        assertEquals("Double", 0, binding.getDouble(), 0);
    }

    public void testGetDoubleWithString()
    {
        IBinding binding = new TestBinding("-102.7");

        assertEquals("Double", -102.7, binding.getDouble(), 0);
    }

    public void testGetIntWithInt()
    {
        IBinding binding = new TestBinding(new Integer(37));

        assertEquals("Int", 37, binding.getInt());
    }

    public void testGetIntWithBoolean()
    {
        IBinding binding = new TestBinding(Boolean.TRUE);

        assertEquals("Int", 1, binding.getInt());

        binding = new TestBinding(Boolean.FALSE);

        assertEquals("Int", 0, binding.getInt());
    }

    public void testGetIntWithString()
    {
        IBinding binding = new TestBinding("3021");

        assertEquals("Int", 3021, binding.getInt());
    }

    public void testGetObjectNull()
    {
        IBinding binding = new TestBinding(null);

        assertNull("Object", binding.getObject("parameter", Number.class));
    }

    public void testGetObjectWithCheck()
    {
        IBinding binding = new TestBinding("Hello");

        assertEquals("Object", "Hello", binding.getObject("baz", String.class));
    }

    public void testGetObjectFailureClass()
    {
        IBinding binding = new TestBinding("Hello");

        try
        {
            binding.getObject("foo", Number.class);
        }
        catch (BindingException ex)
        {
            checkException(ex, "Parameter foo (Hello) is not type java.lang.Number.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testGetObjectFailureInterface()
    {
        IBinding binding = new TestBinding("Goodbye");

        try
        {
            binding.getObject("bar", IRequestCycle.class);
        }
        catch (BindingException ex)
        {
            checkException(ex, "Parameter bar (Goodbye) does not implement interface net.sf.tapestry.IRequestCycle.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testGetStringForNull()
    {
        IBinding binding = new TestBinding(null);

        assertNull("String", binding.getString());
    }

    public void testSetBoolean()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.setBoolean(true);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ReadOnlyBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testSetString()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.setString("Wilma");

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ReadOnlyBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testSetInt()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.setInt(98);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ReadOnlyBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testSetDouble()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.setDouble(-1.5);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ReadOnlyBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testSetObject()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.setObject(Boolean.TRUE);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ReadOnlyBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testInvalidFieldName()
    {
        IBinding binding = new FieldBinding(_resolver, "Baz");

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Unreachable");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Invalid field name: Baz.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }
    
    public void testExpressionBinding()
    {
        IPage page = new MockPage();
        
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "page");
        
        assertEquals("Expression property", "page", binding.getExpression());
        assertEquals("Root property", page, binding.getRoot()); 
        assertEquals("Object property", page, binding.getObject());              
    }
    
    public void testMalformedOGNLExpression()
    {
        IPage page = new MockPage();
        
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "zip flob boff");
        
        try
        {
            binding.getObject();
            
            throw new AssertionFailedError("Unreachable.");
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Malformed OGNL");
            checkException(ex, "zip flob boff");
        }
    }
    
    public void testUnknownProperty()
    {
        IPage page = new MockPage();
        
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "tigris");
        
        try
        {
            binding.getObject();
            
            throw new AssertionFailedError("Unreachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to resolve expression");
            checkException(ex, "tigris");
        }
    }
    
    public void testUpdateReadonlyProperty()
    {
         IPage page = new MockPage();
        
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "bindings");
        
        try
        {
            binding.setObject(new HashMap());
            
            throw new AssertionFailedError("Unreachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to update expression");
            checkException(ex, "bindings");
        }
       
    }
    
    public void testUpdateBoolean()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "booleanValue");
        
        binding.setBoolean(true);
        
        assertEquals(true, page.getBooleanValue());
        
        binding.setBoolean(false);
        
        assertEquals(false, page.getBooleanValue());
    }
    
    public void testUpdateInt()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "intValue");
        
        binding.setInt(275);
        
        assertEquals(275, page.getIntValue());
    }
    
    public void testUpdateDouble()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "doubleValue");
        
        binding.setDouble(3.14);
        
        assertEquals(3.14, page.getDoubleValue(), 0.0);
    }
    
    public void testUpdateObject()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "objectValue");
        
        Object object = new HashMap();
        
        binding.setObject(object);
        
        assertEquals(object, page.getObjectValue());
    }            
    
    
}
