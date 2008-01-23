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

import ognl.OgnlRuntime;
import ognl.TypeConverter;
import ognl.enhance.ExpressionCompiler;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.enhance.ClassFactoryImpl;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.spec.IApplicationSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;

/**
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

    // make sure hivemind compiler isn't configured
    @BeforeMethod
    public void setup_Ognl()
    {
        if (HiveMindExpressionCompiler.class.isInstance(OgnlRuntime.getCompiler()))
        {
            OgnlRuntime.setCompiler(new ExpressionCompiler());
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

    public void test_Read()
    {
        Fixture f = new Fixture("Foo");

        ExpressionEvaluator ee = create();
        trainIntialize(ee);

        assertEquals("Foo", ee.read(f, "value"));
    }

    public void test_Read_Fail()
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
        }
    }

    public void test_Write()
    {
        Fixture f = new Fixture("Foo");

        ExpressionEvaluator ee = create();
        trainIntialize(ee);
        
        ee.write(f, "value", "Bar");

        assertEquals("Bar", f.getValue());
    }

    public void test_Write_Fail()
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

    public void test_Is_Constant()
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

    public void test_Is_Constant_Fail()
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

    void trainIntialize(ExpressionEvaluator evaluator)
    {
        ExpressionEvaluatorImpl impl = (ExpressionEvaluatorImpl)evaluator;
        
        IApplicationSpecification as = newMock(IApplicationSpecification.class);

        expect(as.checkExtension(Tapestry.OGNL_TYPE_CONVERTER)).andReturn(false);

        impl.setApplicationSpecification(as);
        impl.setContributions(Collections.EMPTY_LIST);
        impl.setNullHandlerContributions(Collections.EMPTY_LIST);
        impl.setClassFactory(new ClassFactoryImpl());

        replay();

        impl.initializeService();

        verify();
    }

    public void test_Type_Converter() throws Exception
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
