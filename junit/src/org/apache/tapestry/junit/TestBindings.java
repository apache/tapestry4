//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import junit.framework.AssertionFailedError;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.binding.FieldBinding;
import org.apache.tapestry.binding.ListenerBinding;
import org.apache.tapestry.binding.StaticBinding;
import org.apache.tapestry.binding.StringBinding;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 * Do tests of bindings.
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 */

public class TestBindings extends TapestryTestCase
{
    public static final String STRING_FIELD = "stringField";

    public static final Object NULL_FIELD = null;

    private IResourceResolver _resolver = new DefaultResourceResolver();

    private static class TestBinding extends AbstractBinding
    {
        private Object _object;

        private TestBinding(Object object)
        {
            super(null);

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

        private Timestamp _dateValue;

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

        public Timestamp getDateValue()
        {
            return _dateValue;
        }

        public void setDateValue(Timestamp dateValue)
        {
            _dateValue = dateValue;
        }
    }

    public void testGetObject()
    {
        IBinding binding = new FieldBinding(_resolver,
                "org.apache.tapestry.junit.TestBindings.STRING_FIELD", null);

        assertEquals("Object", STRING_FIELD, binding.getObject());
    }

    public void testToString()
    {
        IBinding binding = new FieldBinding(_resolver,
                "org.apache.tapestry.junit.TestBindings.STRING_FIELD", null);

        assertEquals(
                "String value (before access)",
                "FieldBinding[org.apache.tapestry.junit.TestBindings.STRING_FIELD]",
                binding.toString());

        binding.getObject();

        assertEquals(
                "String value (after access)",
                "FieldBinding[org.apache.tapestry.junit.TestBindings.STRING_FIELD (stringField)]",
                binding.toString());
    }

    public void testJavaLang()
    {
        IBinding binding = new FieldBinding(_resolver, "Boolean.TRUE", null);

        assertEquals("Object", Boolean.TRUE, binding.getObject());
    }

    public void testMissingClass()
    {
        IBinding binding = new FieldBinding(_resolver, "foo.bar.Baz.FIELD", null);

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
        IBinding binding = new FieldBinding(_resolver,
                "org.apache.tapestry.junit.TestBindings.MISSING_FIELD", null);

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(
                    ex,
                    "Field org.apache.tapestry.junit.TestBindings.MISSING_FIELD does not exist.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public final String INSTANCE_FIELD = "InstanceField";

    public void testInstanceAccess()
    {
        IBinding binding = new FieldBinding(_resolver,
                "org.apache.tapestry.junit.TestBindings.INSTANCE_FIELD", null);

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(
                    ex,
                    "Field org.apache.tapestry.junit.TestBindings.INSTANCE_FIELD is an instance variable, not a class variable.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testStaticBindingInt()
    {
        IBinding binding = new StaticBinding("107", null);

        assertEquals("Int", 107, binding.getInt());

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Int", 107, binding.getInt());
    }

    public void testInvalidStaticBindingInt()
    {
        IBinding binding = new StaticBinding("barney", null);

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
        IBinding binding = new StaticBinding("fred", null);

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
        IBinding binding = new StaticBinding("3.14", null);

        assertEquals("Double", 3.14, binding.getDouble(), 0.);

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Double", 3.14, binding.getDouble(), 0.);
    }

    public void testStaticBindingToString()
    {
        IBinding binding = new StaticBinding("alfalfa", null);

        assertEquals("ToString", "StaticBinding[alfalfa]", binding.toString());
    }

    public void testStaticBindingObject()
    {
        String value = Long.toString(System.currentTimeMillis());

        IBinding binding = new StaticBinding(value, null);

        assertEquals("Object", value, binding.getObject());
    }

    public void testGetIntWithNull()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.getInt();
        }
        catch (BindingException ex)
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
        catch (BindingException ex)
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
            assertEquals("Parameter foo (Hello) is an instance of java.lang.String, "
                    + "which does not inherit from java.lang.Number.", ex.getMessage());

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
            assertEquals(
                    "Parameter bar (Goodbye) is an instance of java.lang.String, which does not "
                            + "implement interface org.apache.tapestry.IRequestCycle.",
                    ex.getMessage());

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
        catch (BindingException ex)
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
        catch (BindingException ex)
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
        catch (BindingException ex)
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
        catch (BindingException ex)
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
        catch (BindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testInvalidFieldName()
    {
        IBinding binding = new FieldBinding(_resolver, "Baz", null);

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

        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "page", null);

        assertEquals("Expression property", "page", binding.getExpression());
        assertEquals("Root property", page, binding.getRoot());
        assertEquals("Object property", page, binding.getObject());
    }

    public void testMalformedOGNLExpression()
    {
        IPage page = new MockPage();

        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "zip flob boff", null);

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Unable to parse expression 'zip flob boff'");
        }
    }

    public void testUnknownProperty()
    {
        IPage page = new MockPage();

        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "tigris", null);

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

        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "bindings", null);

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
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "booleanValue", null);

        binding.setBoolean(true);

        assertEquals(true, page.getBooleanValue());

        binding.setBoolean(false);

        assertEquals(false, page.getBooleanValue());
    }

    public void testUpdateInt()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "intValue", null);

        binding.setInt(275);

        assertEquals(275, page.getIntValue());
    }

    public void testUpdateDouble()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "doubleValue", null);

        binding.setDouble(3.14);

        assertEquals(3.14, page.getDoubleValue(), 0.0);
    }

    public void testUpdateObject()
    {
        BoundPage page = new BoundPage();
        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "objectValue", null);

        Object object = new HashMap();

        binding.setObject(object);

        assertEquals(object, page.getObjectValue());
    }

    /**
     * Test error cases for {@link org.apache.tapestry.binding.ListenerBinding}.
     * 
     * @since 3.0
     */

    public void testListenerBindingInt()
    {
        IBinding b = new ListenerBinding(null, null, null, null);

        try
        {
            b.getInt();

            unreachable();
        }
        catch (BindingException ex)
        {
            checkException(
                    ex,
                    "Inappropriate invocation of getInt() on instance of ListenerBinding.");
        }
    }

    /** @since 3.0 * */

    public void testListenerBindingDouble()
    {
        IBinding b = new ListenerBinding(null, null, null, null);

        try
        {
            b.getDouble();

            unreachable();
        }
        catch (BindingException ex)
        {
            checkException(
                    ex,
                    "Inappropriate invocation of getDouble() on instance of ListenerBinding.");
        }
    }

    /** @since 3.0 * */

    public void testListenerBindingObject()
    {
        IBinding b = new ListenerBinding(null, null, null, null);

        assertSame(b, b.getObject());
    }

    /** @since 3.0 * */

    public void testStringBinding()
    {
        IComponent c = new MockPage();

        StringBinding b = new StringBinding(c, "foo", null);

        assertSame(c, b.getComponent());
        assertEquals("foo", b.getKey());
    }

    public void testTypeConverter()
    {
        BoundPage page = new BoundPage();
        MockEngine engine = new MockEngine();
        page.setEngine(engine);

        ExpressionBinding binding = new ExpressionBinding(_resolver, page, "dateValue", null);
        Date date = new Date();

        // This part of the test no longer fails; likely it is due to the latest
        // version of OGNL.
        //        try {
        //            // try without a converter first, which should fail
        //            binding.setObject(date);
        //            fail("Should not be able to call setDateValue(Date)");
        //        }
        //        catch (BindingException expected) {
        //            assertTrue(true);
        //        }

        // now test with a custom converter enabled
        MockApplicationSpecification appSpec = new MockApplicationSpecification();
        HashMap extensions = new HashMap();
        extensions.put(Tapestry.OGNL_TYPE_CONVERTER, new MockTypeConverter());
        appSpec.setExtensions(extensions);
        engine.setSpecification(appSpec);

        binding.setObject(date);
        // note - cannot treat java.sql.Timestamp as a Date as the nanos store the
        // fractional part of the second, including the millis. JDK 1.3 Javadocs
        // for Timestamp suggest using nanos/1000000 to find the actual millis.
        // Otherwise, just using assertEquals(date, page.getDateValue()) fails
        // under JDK 1.3.
        assertEquals(date, new Date((page.getDateValue().getTime() / 1000) * 1000
                + page.getDateValue().getNanos() / 1000000));
    }
}