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

package org.apache.tapestry.junit.enhance;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.enhance.IEnhancedClass;
import org.apache.tapestry.enhance.IEnhancedClassFactory;
import org.apache.tapestry.enhance.javassist.EnhancedClassFactory;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.DefaultResourceResolver;
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
    /**
     *  Test the ability to create a class that implements a read/write property.
     * 
     **/

    public void testCreateProperty() throws Exception
    {
        IResourceResolver resolver = new DefaultResourceResolver();
        IEnhancedClassFactory factory = new EnhancedClassFactory(resolver);

        String className = "org.apache.tapestry.junit.enhance.PropertyHolder";
        IEnhancedClass enhancedClass = factory.createEnhancedClass(className, Object.class);

        String propertyName = "name";
        enhancedClass.createProperty(propertyName, "java.lang.String");

        Class holderClass = enhancedClass.createEnhancedSubclass();

        Object holder = holderClass.newInstance();

        String value = "abraxis";

        OgnlUtils.set("name", resolver, holder, value);

        Object actual = OgnlUtils.get("name", resolver, holder);

        assertEquals("Holder name property.", value, actual);
    }

    public void testCreateFailure()
    {
        IResourceResolver resolver = new DefaultResourceResolver();
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