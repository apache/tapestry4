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

package org.apache.tapestry;

/**
 *  A binding is the mechanism used to provide values for parameters of
 *  specific {@link IComponent} instances. The component doesn't
 *  care where the required value comes from, it simply requires that
 *  a value be provided when needed.
 *
 *  <p>Bindings are set inside the containing component's specification.
 *  Bindings may be static or dynamic (though that is irrelevant to the
 *  component).  Components may also use a binding to write a value
 *  back through a property to some other object (typically, another component).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IBinding extends ILocatable
{
    /**
     *  Invokes {@link #getObject()}, then coerces the value to a boolean.  
     *  The following rules are used to perform the coercion:
     *  <ul>
     *  <li>null is always false
     *  <li>A {@link Boolean} value is self-evident
     *  <li>A {@link Number} value is true if non-zero
     *  <li>A {@link String} value is true if non-empty and contains
     *  non-whitespace characters
     *  <li>Any {@link java.util.Collection} value is true if it has a non-zero
     *  {@link java.util.Collection#size() size}
     *  <li>Any array type is true if it has a non-zero length
     *  <li>Any other non-null value is true
     *  </ul>
     * 
     *  @see Tapestry#evaluateBoolean(Object)
     * 
     **/

    public boolean getBoolean();

    /**
     *  Gets the value of the Binding using {@link #getObject} and coerces it
     *  to an <code>int</code>.  Strings will be parsed, and other
     *  <code>java.lang.Number</code> classes will have <code>intValue()</code>
     *  invoked.  
     *
     *  @throws ClassCastException if the binding's value is not of a usable class.
     *  @throws BindingException if the binding's value is null.
     **/

    public int getInt();

    /**
     *  Gets the value of the Binding using {@link #getObject()} and coerces it
     *  to a <code>double</code>.  Strings will be parsed, and other
     *  <code>java.lang.Number</code> classes will have <code>doubleValue()</code>
     *  invoked.
     *
     *  @throws ClassCastException if the binding's value is not of a usable class.
     *  @throws BindingException if the binding's value is null.
     **/

    public double getDouble();

    /**
     *  Invokes {@link #getObject()} and converts the result to <code>java.lang.String</code>.
     **/

    public String getString();

    /**
     *  Returns the value of this binding.  This is the essential method.  Other methods
     *  get this value and cast or coerce the value.
     *
     **/

    public Object getObject();

    /**
     *  Returns the value for the binding after performing some basic checks.
     *
     *  @param parameterName the name of the parameter (used to build
     *  the message if an exception is thrown).
     *  @param type if not null, the value must be assignable to the specific
     *  class
     *  @throws BindingException if the value is not assignable to the
     *  specified type
     *
     *  @since 0.2.9
     **/

    public Object getObject(String parameterName, Class type);

    /**
     *  Returns true if the value is invariant (not changing; the
     *  same value returned each time).  Static and field bindings
     *  are always invariant, and {@link org.apache.tapestry.binding.ExpressionBinding}s
     *  may be marked invariant (as an optimization).
     * 
     *  @since 2.0.3
     * 
     **/

    public boolean isInvariant();

    /**
     *  Constructs a <code>Boolean</code> and invokes {@link #setObject(Object)}.
     *
     **/

    public void setBoolean(boolean value);

    /**
     *  Constructs an <code>Integer</code> and invokes {@link #setObject(Object)}.
     *
     **/

    public void setInt(int value);

    /**
     *  Constructs an <code>Double</code> and invokes {@link #setObject(Object)}.
     *
     **/

    public void setDouble(double value);

    /**
     *  Invokes {@link #setObject(Object)}.
     *
     **/

    public void setString(String value);

    /**
     *  Updates the value of the binding, if possible.
     *
     *  @exception BindingException If the binding is read only.
     *
     **/

    public void setObject(Object value);

}