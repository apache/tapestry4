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

package org.apache.tapestry.enhance;

import java.lang.reflect.Method;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  The signature of a {@link java.lang.reflect.Method}, including
 *  the name, return type, and parameter types.  Used when checking
 *  for unimplemented methods in enhanced subclasses.  
 * 
 *  <p>
 *  The modifiers (i.e., "public", "abstract") and thrown
 *  exceptions are not relevant for these purposes, and
 *  are not part of the signature.
 * 
 *  <p>
 *  Instances of MethodSignature are immutable and
 *  implement equals() and hashCode() properly for use
 *  in Sets or as Map keys.
 *
 *  @author Howard Lewis Ship
 *  @since 3.0
 *
 **/

public class MethodSignature
{
    private String _name;
    private Class _returnType;
    private Class[] _parameterTypes;
    private int _hashCode = 0;

    public MethodSignature(Method m)
    {
        _name = m.getName();
        _returnType = m.getReturnType();

        // getParameterTypes() returns a copy for us to keep.

        _parameterTypes = m.getParameterTypes();
    }

    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof MethodSignature))
            return false;

        MethodSignature other = (MethodSignature) obj;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(_name, other._name);
        builder.append(_returnType, other._returnType);
        builder.append(_parameterTypes, other._parameterTypes);

        return builder.isEquals();
    }

    public int hashCode()
    {
        if (_hashCode == 0)
        {
            HashCodeBuilder builder = new HashCodeBuilder(253, 97);

            builder.append(_name);
            builder.append(_returnType);
            builder.append(_parameterTypes);

            _hashCode = builder.toHashCode();
        }

        return _hashCode;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("name", _name);
        builder.append("returnType", _returnType);
        builder.append("parameterTypes", _parameterTypes);

        return builder.toString();
    }

}
