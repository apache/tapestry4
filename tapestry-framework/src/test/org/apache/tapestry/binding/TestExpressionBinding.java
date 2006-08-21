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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.binding.ExpressionBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestExpressionBinding extends BindingTestCase
{

    public void testInvariant()
    {   
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        IComponent component = newMock(IComponent.class);
        Location l = fabricateLocation(1);
        
        Object compiled = new Object();
        
        Object expressionValue = "EXPRESSION-VALUE";
        
        ValueConverter vc = newValueConverter();
        
        expect(ec.getCompiledExpression("exp")).andReturn(compiled);
        
        expect(ev.isConstant("exp")).andReturn(true);
        
        expect(ev.readCompiled(component, compiled)).andReturn(expressionValue);
        
        expect(component.getExtendedId()).andReturn("Foo/bar.baz");
        
        replay();
        
        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
                "exp", ev, ec);
        
        assertEquals(true, b.isInvariant());
        
        // A second time, to test the 'already initialized'
        // code path.

        assertEquals(true, b.isInvariant());

        // Get the object, which should be cached.

        assertSame(expressionValue, b.getObject());

        assertSame(component, b.getComponent());

        assertEquals("ExpressionBinding[Foo/bar.baz exp]", b.toString());

        verify();
    }

    public void testVariant()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent();
        Object compiled = new Object();

        Object expressionValue1 = new Object();
        Object expressionValue2 = new Object();

        ValueConverter vc = newValueConverter();
        
        expect(ec.getCompiledExpression("exp")).andReturn(compiled);

        expect(ev.isConstant("exp")).andReturn(false);

        expect(ev.readCompiled(component, compiled)).andReturn(expressionValue1);

        expect(ev.readCompiled(component, compiled)).andReturn(expressionValue2);
        
        replay();
        
        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
                "exp", ev, ec);
        
        assertEquals(false, b.isInvariant());

        // Check that the expression is re-evaluated on
        // each call to getObject().

        assertSame(expressionValue1, b.getObject());

        assertSame(expressionValue2, b.getObject());

        verify();
    }

    public void testSetObject()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        expect(ec.getCompiledExpression("exp")).andReturn(compiled);

        expect(ev.isConstant("exp")).andReturn(false);

        Object newValue = new Object();

        ev.writeCompiled(component, compiled, newValue);

        replay();

        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
                "exp", ev, ec);

        b.setObject(newValue);

        verify();
    }

    public void testSetObjectInvariant()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent("Foo/bar.baz");
        Object compiled = new Object();
        
        ValueConverter vc = newValueConverter();

        expect(ec.getCompiledExpression("exp")).andReturn(compiled);

        expect(ev.isConstant("exp")).andReturn(true);

        replay();

        ExpressionBinding b = new ExpressionBinding("parameter foo", l, vc, component,
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

        verify();
    }

    public void testSetObjectFailure()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        expect(ec.getCompiledExpression("exp")).andReturn(compiled);

        expect(ev.isConstant("exp")).andReturn(false);

        Object newValue = new Object();

        RuntimeException innerException = new RuntimeException("Failure");

        ev.writeCompiled(component, compiled, newValue);
        expectLastCall().andThrow(innerException);

        replay();

        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
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

        verify();
    }

    public void testCompileExpressionFailure()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent();
        ValueConverter vc = newValueConverter();

        Throwable innerException = new RuntimeException("Failure");

        expect(ec.getCompiledExpression("exp")).andThrow(innerException);

        replay();

        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
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

        verify();
    }

    public void testResolveExpressionFailure()
    {
        ExpressionEvaluator ev = newMock(ExpressionEvaluator.class);
        ExpressionCache ec = newMock(ExpressionCache.class);
        Location l = fabricateLocation(1);
        
        IComponent component = newComponent();
        Object compiled = new Object();

        ValueConverter vc = newValueConverter();

        expect(ec.getCompiledExpression("exp")).andReturn(compiled);

        expect(ev.isConstant("exp")).andReturn(false);

        Throwable innerException = new RuntimeException("Failure");

        ev.readCompiled(component, compiled);
        expectLastCall().andThrow(innerException);

        replay();

        ExpressionBinding b = new ExpressionBinding("param", l, vc, component,
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

        verify();
    }
}