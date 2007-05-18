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
package org.apache.tapestry.enhance;

import org.apache.tapestry.IPage;
import org.apache.tapestry.TestBase;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * Tests functionality of {@link MethodSignature} implementations.
 */
@Test
public class MethodSignatureTest extends TestBase {
    
    // used for non generic tests
    class Simple {
        
        public String getValue()
        {
            return "foo";
        }
        
        public void setMax(Integer val)
        {
        }
    }
    
    public void test_Simple_Properties()
    {
        ClassInspector ins = new ClassInspectorImpl();
        
        MethodSignature sig = ins.getPropertyAccessor(Simple.class, "value");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), String.class);
        
        sig = ins.getPropertyAccessor(Simple.class, "max");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), void.class);
        assertEquals(sig.getParameterTypes().length, 1);
        assertEquals(sig.getParameterTypes()[0], Integer.class);
    }
    
    public void test_Generic_Properties()
    {
        ClassInspector ins = new GenericsClassInspectorImpl();
        
        MethodSignature sig = ins.getPropertyAccessor(FooGenericComponent.class, "value");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), FooGeneric.class);
    }
    
    public void test_Generic_Parameters()
    {
        ClassInspector ins = new GenericsClassInspectorImpl();
        
        MethodSignature sig = ins.getPropertyAccessor(FooGenericComponent.class, "somethingCrazy");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), void.class);
        assert sig.getParameterTypes() != null && sig.getParameterTypes().length > 0;
        assertEquals(sig.getParameterTypes()[0], FooGeneric.class);
        
        sig = ins.getPropertyAccessor(AbstractGenericBase.class, "somethingCrazy");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), void.class);
        assert sig.getParameterTypes() != null && sig.getParameterTypes().length > 0;
        assertEquals(sig.getParameterTypes()[0], SimpleGeneric.class);
    }
    
    public void test_Generic_Simple_Property()
    {
        ClassInspector ins = new GenericsClassInspectorImpl();
        
        MethodSignature sig = ins.getPropertyAccessor(FooGenericComponent.class, "map");
        
        assert sig != null;
        assertEquals(sig.getReturnType(), Map.class);
    }
    
    public void test_Generic_Inheritance()
    {
        ClassInspector ins = new GenericsClassInspectorImpl();
        
        MethodSignature child = ins.getPropertyAccessor(FooGenericComponent.class, "operationValue");
        MethodSignature base = ins.getPropertyAccessor(AbstractGenericBase.class, "operationValue");
        
        assert child != null;
        assert base != null;
        
        assert child.isOverridingSignatureOf(base);
        
        assert !child.equals(base);
        assert !base.equals(child);
    }

    public void test_Generic_Method_Hash()
    {
        Class testClass = MyTest.class;

        Method[] methods = testClass.getMethods();

        for (Method method : methods) {
            
            MethodSignatureImpl msi = new GenericsMethodSignatureImpl(testClass, method);
            msi.hashCode();
        }

        ClassInspector ins = new GenericsClassInspectorImpl();
        MethodSignature sig = ins.getPropertyAccessor(MyTest.class, "relativeObject");

        assert sig.getReturnType() != null;
        assertEquals(sig.getReturnType(), BaseTest.class);
    }

    public void test_Generic_Service_Property()
            throws Exception
    {
        ClassInspector ins = new GenericsClassInspectorImpl();
        MethodSignature m  = ins.getPropertyAccessor(GenericServiceImpl.class, "currentFoo");

        assertEquals(m.getReturnType(), BasicObject.class);
    }

    public static abstract class BaseTest<T>{ }
    
    public static abstract class MyTest<T,E extends BaseTest<T>> extends BaseTest<T> {

        public abstract E getRelativeObject();
        public abstract void setRelativeObject(E e);
    }

    public void test_Find_Type()
    {
        Class clazz=TestGeneric.class;
        Method[] ms = clazz.getMethods();
        
        for (Method m : ms) {

            MethodSignature sig = new GenericsMethodSignatureImpl(clazz, m);

            assertEquals(sig.getName(), m.getName());
            assertEquals(sig.getReturnType(), m.getReturnType());
            assertEquals(sig.getParameterTypes(), m.getParameterTypes());
            assertEquals(sig.getExceptionTypes(), m.getExceptionTypes());
            assertEquals(sig.getReturnType(), m.getReturnType());
        }
    }

    public class TestGeneric<T> extends BaseGeneric<T>{

    }
    public class BaseGeneric<T> {
        public IPage doDeleteEntityAction(T entity) {
            return null;
        }
    }

}
