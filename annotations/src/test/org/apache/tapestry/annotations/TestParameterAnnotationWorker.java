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

import org.apache.hivemind.Location;
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
    private IParameterSpecification attempt(String propertyName, Location location)
    {
        return attempt(propertyName, propertyName, location);
    }

    private IParameterSpecification attempt(String propertyName, String parameterName,
            Location location)
    {
        Method m = findMethod(AnnotatedPage.class, "get"
                + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType(propertyName);
        opc.setReturnValue(m.getReturnType());

        IComponentSpecification spec = new ComponentSpecification();

        replayControls();

        new ParameterAnnotationWorker().performEnhancement(op, spec, m, location);

        verifyControls();

        return spec.getParameter(parameterName);
    }

    public void testSimple()
    {
        Location l = newLocation();

        IParameterSpecification ps = attempt("simpleParameter", l);

        assertListsEqual(new Object[] {}, ps.getAliasNames().toArray());
        assertEquals(true, ps.getCache());
        assertEquals(null, ps.getDefaultBindingType());
        assertEquals(null, ps.getDefaultValue());
        assertEquals(null, ps.getDescription());
        assertSame(l, ps.getLocation());
        assertEquals("simpleParameter", ps.getParameterName());
        assertEquals("simpleParameter", ps.getPropertyName());
        assertEquals("java.lang.String", ps.getType());
    }

    public void testRequired()
    {
        IParameterSpecification ps = attempt("requiredParameter", null);

        assertEquals(true, ps.isRequired());
    }

    public void testDefaultBinding()
    {
        IParameterSpecification ps = attempt("beanDefaultParameter", null);

        assertEquals("bean", ps.getDefaultBindingType());
        assertEquals("java.lang.Object", ps.getType());
    }

    public void testCacheOff()
    {
        IParameterSpecification ps = attempt("nonCachedParameter", null);

        assertEquals(false, ps.getCache());
    }

    public void testAliases()
    {
        IParameterSpecification ps = attempt("aliasedParameter", null);

        assertListsEqual(new String[]
        { "fred" }, ps.getAliasNames().toArray());
    }

    public void testDeprecated()
    {
        IParameterSpecification ps = attempt("deprecatedParameter", null);
        assertEquals(true, ps.isDeprecated());
    }

    public void testNamed()
    {
        IParameterSpecification ps = attempt("namedParameter", "fred", null);

        assertEquals("fred", ps.getParameterName());
        assertEquals("namedParameter", ps.getPropertyName());
    }
}
