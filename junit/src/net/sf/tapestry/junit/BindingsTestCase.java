//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.junit;

import junit.framework.AssertionFailedError;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.binding.AbstractBinding;
import net.sf.tapestry.binding.FieldBinding;
import net.sf.tapestry.binding.StaticBinding;
import net.sf.tapestry.engine.ResourceResolver;

/**
 *  Do tests of bindings.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class BindingsTestCase extends TapestryTestCase
{
    public static final String STRING_FIELD = "stringField";

    public static final Object NULL_FIELD = null;

    private static class TestBinding extends AbstractBinding
    {
        private Object _object;

        private TestBinding(Object object)
        {
            _object = object;
        }

        public Object getObject()
        {
            return _object;
        }

        public Class getType()
        {
            return null;
        }

    }

    public BindingsTestCase(String name)
    {
        super(name);
    }

    public void testGetTypeObject()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD");

        assertEquals("Type", String.class, binding.getType());
    }

    public void testGetTypeNull()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.NULL_FIELD");

        assertNull("Type", binding.getType());
    }

    public void testGetObject()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD");

        assertEquals("Object", STRING_FIELD, binding.getObject());
    }

    public void testToString()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD");

        assertEquals(
            "String value (before access)",
            "FieldBinding[net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD]",
            binding.toString());

        binding.getObject();

        assertEquals(
            "String value (after access)",
            "FieldBinding[net.sf.tapestry.junit.BindingsTestCase.STRING_FIELD (stringField)]",
            binding.toString());
    }

    public void testJavaLang()
    {
        IBinding binding = new FieldBinding(new ResourceResolver(this), "Boolean.TRUE");

        assertEquals("Object", Boolean.TRUE, binding.getObject());
    }

    public void testMissingClass()
    {
        IBinding binding = new FieldBinding(new ResourceResolver(this), "foo.bar.Baz.FIELD");

        try
        {

            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Unable to resolve class foo.bar.Baz.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testMissingField()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.MISSING_FIELD");

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(ex, "Field net.sf.tapestry.junit.BindingsTestCase.MISSING_FIELD does not exist.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public final String INSTANCE_FIELD = "InstanceField";

    public void testInstanceAccess()
    {
        IBinding binding =
            new FieldBinding(new ResourceResolver(this), "net.sf.tapestry.junit.BindingsTestCase.INSTANCE_FIELD");

        try
        {
            binding.getObject();

            throw new AssertionFailedError("Not reachable.");
        }
        catch (BindingException ex)
        {
            checkException(
                ex,
                "Field net.sf.tapestry.junit.BindingsTestCase.INSTANCE_FIELD is an instance variable, not a class variable.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testStaticBindingType()
    {
        IBinding binding = new StaticBinding("foo");

        assertEquals("Type", String.class, binding.getType());
    }

    public void testStaticBindingInt()
    {
        IBinding binding = new StaticBinding("107");

        assertEquals("Int", 107, binding.getInt());

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Int", 107, binding.getInt());
    }

    public void testInvalidStaticBindingInt()
    {
        IBinding binding = new StaticBinding("barney");

        try
        {
            binding.getInt();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (NumberFormatException ex)
        {
            checkException(ex, "barney");
        }
    }

    public void testInvalidStaticBindingDouble()
    {
        IBinding binding = new StaticBinding("fred");

        try
        {
            binding.getDouble();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (NumberFormatException ex)
        {
            checkException(ex, "fred");
        }
    }

    public void testStaticBindingDouble()
    {
        IBinding binding = new StaticBinding("3.14");

        assertEquals("Double", 3.14, binding.getDouble(), 0.);

        // Do it a second time, just to fill a code coverage gap.

        assertEquals("Double", 3.14, binding.getDouble(), 0.);
    }

    public void testStaticBindingToString()
    {
        IBinding binding = new StaticBinding("alfalfa");

        assertEquals("ToString", "StaticBinding[alfalfa]", binding.toString());
    }

    public void testStaticBindingObject()
    {
        String value = Long.toString(System.currentTimeMillis());

        IBinding binding = new StaticBinding(value);

        assertEquals("Object", value, binding.getObject());
    }

    public void testGetIntWithNull()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.getInt();
        }
        catch (NullValueForBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testGetDoubleWithNull()
    {
        IBinding binding = new TestBinding(null);

        try
        {
            binding.getDouble();
        }
        catch (NullValueForBindingException ex)
        {
            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public static final double DOUBLE_FIELD = 3.14;

    public void testGetDoubleWithDouble()
    {
        IBinding binding = new TestBinding(new Double(3.14));

        assertEquals("Double", 3.14, binding.getDouble(), 0);
    }

    public void testGetDoubleWithBoolean()
    {
        IBinding binding = new TestBinding(Boolean.TRUE);

        assertEquals("Double", 1.0, binding.getDouble(), 0);

        binding = new TestBinding(Boolean.FALSE);

        assertEquals("Double", 0, binding.getDouble(), 0);
    }

    public void testGetDoubleWithString()
    {
        IBinding binding = new TestBinding("-102.7");

        assertEquals("Double", -102.7, binding.getDouble(), 0);
    }

    public void testGetIntWithInt()
    {
        IBinding binding = new TestBinding(new Integer(37));

        assertEquals("Int", 37, binding.getInt());
    }

    public void testGetIntWithBoolean()
    {
        IBinding binding = new TestBinding(Boolean.TRUE);

        assertEquals("Int", 1, binding.getInt());

        binding = new TestBinding(Boolean.FALSE);

        assertEquals("Int", 0, binding.getInt());
    }

    public void testGetIntWithString()
    {
        IBinding binding = new TestBinding("3021");

        assertEquals("Int", 3021, binding.getInt());
    }

    public void testGetObjectNull()
    {
        IBinding binding = new TestBinding(null);

        assertNull("Object", binding.getObject("parameter", Number.class));
    }

    public void testGetObjectWithCheck()
    {
        IBinding binding = new TestBinding("Hello");

        assertEquals("Object", "Hello", binding.getObject("baz", String.class));
    }

    public void testGetObjectFailureClass()
    {
        IBinding binding = new TestBinding("Hello");

        try
        {
            binding.getObject("foo", Number.class);
        }
        catch (BindingException ex)
        {
            checkException(ex, "Parameter foo (Hello) is not type java.lang.Number.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }

    public void testGetObjectFailureInterface()
    {
        IBinding binding = new TestBinding("Goodbye");

        try
        {
            binding.getObject("bar", IRequestCycle.class);
        }
        catch (BindingException ex)
        {
            checkException(ex, "Parameter bar (Goodbye) does not implement interface net.sf.tapestry.IRequestCycle.");

            assertEquals("Binding", binding, ex.getBinding());
        }
    }
}
