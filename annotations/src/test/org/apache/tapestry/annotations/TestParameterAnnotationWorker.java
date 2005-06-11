// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.annotations.ParameterAnnotationWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestParameterAnnotationWorker extends BaseAnnotationTestCase
{
    private IParameterSpecification attempt(String propertyName)
    {
        return attempt(propertyName, propertyName);
    }

    private IParameterSpecification attempt(String propertyName, String parameterName)
    {
        Method m = findMethod(AnnotatedPage.class, "get"
                + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType(propertyName);
        opc.setReturnValue(m.getReturnType());

        IComponentSpecification spec = new ComponentSpecification();

        Parameter annotation = m.getAnnotation(Parameter.class);

        replayControls();

        new ParameterAnnotationWorker().performEnhancement(op, spec, annotation, m);

        verifyControls();

        return spec.getParameter(parameterName);
    }

    public void testSimple()
    {
        IParameterSpecification ps = attempt("simpleParameter");

        assertListsEqual(new Object[] {}, ps.getAliasNames().toArray());
        assertEquals(true, ps.getCache());
        assertEquals("", ps.getDefaultBindingType());
        assertEquals("", ps.getDefaultValue());
        assertEquals(null, ps.getDescription());
        assertNull(ps.getLocation());
        assertEquals("simpleParameter", ps.getParameterName());
        assertEquals("simpleParameter", ps.getPropertyName());
        assertEquals("java.lang.String", ps.getType());
    }

    public void testRequired()
    {
        IParameterSpecification ps = attempt("requiredParameter");

        assertEquals(true, ps.isRequired());
    }

    public void testDefaultBinding()
    {
        IParameterSpecification ps = attempt("beanDefaultParameter");

        assertEquals("bean", ps.getDefaultBindingType());
        assertEquals("java.lang.Object", ps.getType());
    }

    public void testCacheOff()
    {
        IParameterSpecification ps = attempt("nonCachedParameter");

        assertEquals(false, ps.getCache());
    }

    public void testAliases()
    {
        IParameterSpecification ps = attempt("aliasedParameter");

        assertListsEqual(new String[]
        { "fred" }, ps.getAliasNames().toArray());
    }

    public void testDeprecated()
    {
        IParameterSpecification ps = attempt("deprecatedParameter");
        assertEquals(true, ps.isDeprecated());
    }

    public void testNamed()
    {
        IParameterSpecification ps = attempt("namedParameter", "fred");

        assertEquals("fred", ps.getParameterName());
        assertEquals("namedParameter", ps.getPropertyName());
    }
}
