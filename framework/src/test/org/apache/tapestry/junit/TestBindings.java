// Copyright 2004 The Apache Software Foundation
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
import java.util.HashMap;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.binding.ListenerBinding;
import org.apache.tapestry.binding.StaticBinding;
import org.apache.tapestry.binding.StringBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.impl.ExpressionCacheImpl;
import org.apache.tapestry.services.impl.ExpressionEvaluatorImpl;

/**
 * Do tests of bindings.
 * 
 * @author Howard Lewis Ship
 */

public class TestBindings extends TapestryTestCase
{
    public static final String STRING_FIELD = "stringField";

    public static final Object NULL_FIELD = null;

    private static class TestBinding extends AbstractBinding
    {
        private Object _object;

        private TestBinding(Object object, ValueConverter valueConverter)
        {
            super("foo", valueConverter, null);

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

    public final String INSTANCE_FIELD = "InstanceField";

    public void testStaticBindingToString()
    {
        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding binding = new StaticBinding("foo", "alfalfa", vc, null);

        assertEquals("ToString", "StaticBinding[alfalfa]", binding.toString());

        verifyControls();
    }

    public void testStaticBindingObject()
    {
        ValueConverter vc = newValueConverter();

        replayControls();

        String value = Long.toString(System.currentTimeMillis());

        IBinding binding = new StaticBinding("foo", value, vc, null);

        assertEquals("Object", value, binding.getObject());

        verifyControls();
    }

    public static final double DOUBLE_FIELD = 3.14;

    public void testSetObject()
    {
        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding binding = new TestBinding(null, vc);

        try
        {
            binding.setObject(Boolean.TRUE);

            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }

        verifyControls();
    }

    private ExpressionBinding newBinding(IComponent root, String parameterName, String expression,
            ValueConverter valueConverter)
    {
        ExpressionCache cache = new ExpressionCacheImpl();
        ExpressionEvaluatorImpl evaluator = new ExpressionEvaluatorImpl();
        evaluator.setExpressionCache(cache);

        return new ExpressionBinding(root, parameterName, expression, valueConverter, null,
                evaluator, cache);
    }

    public void testExpressionBinding()
    {
        IPage page = new MockPage();

        ValueConverter vc = newValueConverter();

        replayControls();

        ExpressionBinding binding = newBinding(page, "foo", "page", vc);

        assertEquals("Expression property", "page", binding.getExpression());
        assertEquals("Root property", page, binding.getRoot());
        assertEquals("Object property", page, binding.getObject());

        verifyControls();
    }

    public void testMalformedOGNLExpression()
    {
        IPage page = new MockPage();

        ValueConverter vc = newValueConverter();

        replayControls();

        ExpressionBinding binding = newBinding(page, "foo", "zip flob boff", vc);

        try
        {
            binding.getObject();

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Unable to parse OGNL expression 'zip flob boff'");
        }

        verifyControls();
    }

    public void testUnknownProperty()
    {
        IPage page = new MockPage();

        ValueConverter vc = newValueConverter();

        replayControls();

        ExpressionBinding binding = newBinding(page, "foo", "tigris", vc);

        try
        {
            binding.getObject();

            unreachable();
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to read OGNL expression");
            checkException(ex, "tigris");
        }

        verifyControls();
    }

    public void testUpdateReadOnlyProperty()
    {
        IPage page = new MockPage();

        ValueConverter vc = newValueConverter();

        replayControls();

        ExpressionBinding binding = newBinding(page, "foo", "bindings", vc);

        try
        {
            binding.setObject(new HashMap());

            unreachable();
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to update OGNL expression");
            checkException(ex, "bindings");
        }

        verifyControls();
    }

    public void testUpdateObject()
    {
        BoundPage page = new BoundPage();
        ValueConverter vc = newValueConverter();

        replayControls();

        ExpressionBinding binding = newBinding(page, "foo", "objectValue", vc);

        Object object = new HashMap();

        binding.setObject(object);

        assertEquals(object, page.getObjectValue());

        verifyControls();
    }

    /** @since 3.0 * */

    public void testListenerBindingObject()
    {
        ValueConverter vc = newValueConverter();

        replayControls();

        IBinding b = new ListenerBinding(null, "foo", null, null, vc, null);

        assertSame(b, b.getObject());

        verifyControls();
    }

    /** @since 3.0 * */

    public void testStringBinding()
    {
        ValueConverter vc = newValueConverter();

        replayControls();

        IComponent c = new MockPage();

        StringBinding b = new StringBinding(c, "parameter", "foo", vc, null);

        assertEquals("foo", b.getKey());

        verifyControls();
    }

    /** @since 3.1 */
    private ValueConverter newValueConverter()
    {
        return (ValueConverter) newMock(ValueConverter.class);
    }

}