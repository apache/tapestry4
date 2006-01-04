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

package org.apache.tapestry.binding;

import org.apache.tapestry.BindingException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.ExpressionBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestExpressionBinding extends BindingTestCase
{

    public void testInvariant()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        MockControl cc = newControl(IComponent.class);
        IComponent component = (IComponent) cc.getMock();

        Object compiled = new Object();

        Object expressionValue = "EXPRESSION-VALUE";

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(true);

        ev.readCompiled(component, compiled);
        evc.setReturnValue(expressionValue);

        component.getExtendedId();
        cc.setReturnValue("Foo/bar.baz");

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        assertEquals(true, b.isInvariant());

        // A second time, to test the 'already initialized'
        // code path.

        assertEquals(true, b.isInvariant());

        // Get the object, which should be cached.

        assertSame(expressionValue, b.getObject());

        assertSame(component, b.getComponent());

        assertEquals("ExpressionBinding[Foo/bar.baz exp]", b.toString());

        verifyControls();
    }

    public void testVariant()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent();
        Object compiled = new Object();

        Object expressionValue1 = new Object();
        Object expressionValue2 = new Object();

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(false);

        ev.readCompiled(component, compiled);
        evc.setReturnValue(expressionValue1);

        ev.readCompiled(component, compiled);
        evc.setReturnValue(expressionValue2);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        assertEquals(false, b.isInvariant());

        // Check that the expression is re-evaluated on
        // each call to getObject().

        assertSame(expressionValue1, b.getObject());

        assertSame(expressionValue2, b.getObject());

        verifyControls();
    }

    public void testSetObject()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(false);

        Object newValue = new Object();

        ev.writeCompiled(component, compiled, newValue);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        b.setObject(newValue);

        verifyControls();
    }

    public void testSetObjectInvariant()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent("Foo/bar.baz");
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(true);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("parameter foo", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        try
        {
            b.setObject(new Object());
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Binding for parameter foo (ExpressionBinding[Foo/bar.baz exp]) may not be updated.",
                    ex.getMessage());
        }

        verifyControls();
    }

    public void testSetObjectFailure()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(false);

        Object newValue = new Object();

        RuntimeException innerException = new RuntimeException("Failure");

        ev.writeCompiled(component, compiled, newValue);
        evc.setThrowable(innerException);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        try
        {
            b.setObject(newValue);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Failure", ex.getMessage());
            assertSame(innerException, ex.getRootCause());
        }

        verifyControls();
    }

    public void testCompileExpressionFailure()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent();
        ValueConverter vc = newValueConverter();

        Throwable innerException = new RuntimeException("Failure");

        ec.getCompiledExpression("exp");
        ecc.setThrowable(innerException);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        try
        {
            b.isInvariant();
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Failure", ex.getMessage());
            assertSame(innerException, ex.getRootCause());
        }

        verifyControls();
    }

    public void testResolveExpressionFailure()
    {
        MockControl evc = newControl(ExpressionEvaluator.class);
        ExpressionEvaluator ev = (ExpressionEvaluator) evc.getMock();

        MockControl ecc = newControl(ExpressionCache.class);
        ExpressionCache ec = (ExpressionCache) ecc.getMock();

        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        ec.getCompiledExpression("exp");
        ecc.setReturnValue(compiled);

        ev.isConstant("exp");
        evc.setReturnValue(false);

        Throwable innerException = new RuntimeException("Failure");

        ev.readCompiled(component, compiled);
        evc.setThrowable(innerException);

        replayControls();

        ExpressionBinding b = new ExpressionBinding("param", fabricateLocation(1), vc, component,
                "exp", ev, ec);

        try
        {
            b.getObject();
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Failure", ex.getMessage());
            assertSame(innerException, ex.getRootCause());
        }

        verifyControls();
    }
}