// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;

import ognl.TypeConverter;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.easymock.AbstractMatcher;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestExpressionEvaluator extends HiveMindTestCase
{
    private ExpressionEvaluator create()
    {
        ExpressionCache cache = new ExpressionCacheImpl();

        ExpressionEvaluatorImpl result = new ExpressionEvaluatorImpl();

        result.setExpressionCache(cache);

        return result;
    }

    private static class NullMeansIgnoreMatcher extends AbstractMatcher
    {
        public boolean matches(Object[] expected, Object[] actual)
        {
            for (int i = 0; i < expected.length; i++)
            {
                if (expected[i] == null)
                    continue;

                if (!argumentMatches(expected[i], actual[i]))
                    return false;
            }

            return true;
        }

    }

    public static class Fixture
    {
        private String _value;

        public Fixture()
        {
        }

        public Fixture(String value)
        {
            _value = value;
        }

        public String getValue()
        {
            return _value;
        }

        public void setValue(String value)
        {
            _value = value;
        }
    }

    public void testRead()
    {
        Fixture f = new Fixture("Foo");

        ExpressionEvaluator ee = create();

        assertEquals("Foo", ee.read(f, "value"));
    }

    public void testReadFail()
    {
        Fixture f = new Fixture();

        ExpressionEvaluator ee = create();

        try
        {
            ee.read(f, "bar");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Unable to read OGNL expression");
            assertSame(f, ex.getComponent());
        }
    }

    public void testWrite()
    {
        Fixture f = new Fixture("Foo");

        ExpressionEvaluator ee = create();

        ee.write(f, "value", "Bar");

        assertEquals("Bar", f.getValue());
    }

    public void testWriteFail()
    {
        Fixture f = new Fixture();

        ExpressionEvaluator ee = create();

        try
        {
            ee.write(f, "class", "Foo");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Unable to update OGNL expression");
            assertSame(f, ex.getComponent());
        }
    }

    public void testIsConstant()
    {
        ExpressionEvaluator ee = create();

        assertEquals(true, ee.isConstant("true"));
        assertEquals(true, ee.isConstant("'OGNL'"));
        assertEquals(false, ee.isConstant("foo.bar"));
        assertEquals(false, ee.isConstant("bar()"));
        assertEquals(true, ee.isConstant("@org.apache.tapestry.Tapestry@HOME_SERVICE"));
    }

    public void testIsConstantFail()
    {
        ExpressionEvaluator ee = create();

        try
        {
            ee.isConstant("@foo@BAR");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Error evaluating OGNL expression");
        }

    }

    public void testTypeConverter() throws Exception
    {
        MockControl asc = newControl(IApplicationSpecification.class);
        IApplicationSpecification as = (IApplicationSpecification) asc.getMock();

        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        // Training

        as.checkExtension(Tapestry.OGNL_TYPE_CONVERTER);
        asc.setReturnValue(true);

        as.getExtension(Tapestry.OGNL_TYPE_CONVERTER, TypeConverter.class);
        asc.setReturnValue(tc);

        replayControls();

        ExpressionCache cache = new ExpressionCacheImpl();

        ExpressionEvaluatorImpl ee = new ExpressionEvaluatorImpl();

        ee.setExpressionCache(cache);
        ee.setApplicationSpecification(as);
        ee.setContributions(Collections.EMPTY_LIST);
        
        ee.initializeService();

        verifyControls();

        Fixture f = new Fixture();

        Method m = Fixture.class.getMethod("setValue", new Class[]
        { String.class });

        Date d = new Date();

        // Training

        // Since we have no idea what OGNL will stuff into that Map parameter,
        // we just ignore it.
        tc.convertValue(null, f, m, "value", d, String.class);
        tcc.setMatcher(new NullMeansIgnoreMatcher());

        tcc.setReturnValue("FROM-TYPE-CONVERTER");

        replayControls();

        ee.write(f, "value", d);

        assertEquals("FROM-TYPE-CONVERTER", f.getValue());

        verifyControls();
    }
}