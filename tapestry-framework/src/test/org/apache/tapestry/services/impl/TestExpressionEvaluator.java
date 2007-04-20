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

import ognl.TypeConverter;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.enhance.ClassFactoryImpl;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.spec.IApplicationSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestExpressionEvaluator extends BaseComponentTestCase
{
    private ExpressionEvaluatorImpl create()
    {
        ExpressionCacheImpl cache = new ExpressionCacheImpl();

        ExpressionEvaluatorImpl result = new ExpressionEvaluatorImpl();
        result.setClassFactory(new ClassFactoryImpl());

        result.setExpressionCache(cache);
        
        cache.setEvaluator(result);

        return result;
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
            assertExceptionSubstring(ex, "Unable to parse OGNL expression");
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
        ExpressionEvaluatorImpl ee = create();

        ee.setApplicationSpecification(newAppSpec());
        ee.setContributions(Collections.EMPTY_LIST);
        ee.setNullHandlerContributions(Collections.EMPTY_LIST);

        replay();

        ee.initializeService();

        assertEquals(true, ee.isConstant("true"));
        assertEquals(true, ee.isConstant("'OGNL'"));
        assertEquals(false, ee.isConstant("foo.bar"));
        assertEquals(false, ee.isConstant("bar()"));
        assertEquals(true, ee.isConstant("@org.apache.tapestry.Tapestry@HOME_SERVICE"));

        verify();
    }

    private IApplicationSpecification newAppSpec()
    {
        IApplicationSpecification spec = newMock(IApplicationSpecification.class);

        expect(spec.checkExtension(Tapestry.OGNL_TYPE_CONVERTER)).andReturn(false);

        return spec;
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
        IApplicationSpecification as = newMock(IApplicationSpecification.class);
        
        TypeConverter tc = newMock(TypeConverter.class);

        // Training

        expect(as.checkExtension(Tapestry.OGNL_TYPE_CONVERTER)).andReturn(true);

        expect(as.getExtension(Tapestry.OGNL_TYPE_CONVERTER, TypeConverter.class)).andReturn(tc);

        replay();

        ExpressionCacheImpl cache = new ExpressionCacheImpl();

        ExpressionEvaluatorImpl ee = new ExpressionEvaluatorImpl();
        
        ee.setExpressionCache(cache);
        ee.setApplicationSpecification(as);
        ee.setContributions(Collections.EMPTY_LIST);
        ee.setNullHandlerContributions(Collections.EMPTY_LIST);
        ee.setClassFactory(new ClassFactoryImpl());
        
        ee.initializeService();
        
        verify();
        
        cache.setEvaluator(ee);

        Fixture f = new Fixture();

        Date d = new Date();

        // Training
        
        replay();
        
        ee.write(f, "value", d);

        assertEquals(f.getValue(), d.toString());

        verify();
    }
}
