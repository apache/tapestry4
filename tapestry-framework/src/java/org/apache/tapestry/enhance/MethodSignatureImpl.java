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

import org.apache.hivemind.service.ClassFabUtils;

import java.lang.reflect.Method;


/**
 * JDK 1.4 based version of {@link MethodSignature}. 
 */
public class MethodSignatureImpl implements MethodSignature
{
    protected int _hashCode = -1;

    protected Class _returnType;

    protected String _name;

    protected Class[] _parameterTypes;

    protected Class[] _exceptionTypes;

    public MethodSignatureImpl(Class returnType, String name, 
            Class[] parameterTypes, Class[] exceptionTypes)
    {
        _returnType = returnType;
        _name = name;
        _parameterTypes = parameterTypes;
        _exceptionTypes = exceptionTypes;
    }
    
    public MethodSignatureImpl(Method m)
    {
        this(m.getReturnType(), m.getName(), m.getParameterTypes(), m.getExceptionTypes());
    }
    
    public Class[] getExceptionTypes()
    {
        return _exceptionTypes;
    }

    public String getName()
    {
        return _name;
    }
    
    public Class[] getParameterTypes()
    {
        return _parameterTypes;
    }

    public Class getReturnType()
    {
        return _returnType;
    }

    public int hashCode()
    {
        if (_hashCode == -1)
        {
            _hashCode = _returnType.hashCode();

            _hashCode = 31 * _hashCode + _name.hashCode();

            int count = count(_parameterTypes);

            for (int i = 0; i < count; i++)
                _hashCode = 31 * _hashCode + _parameterTypes[i].hashCode();

            count = count(_exceptionTypes);

            for (int i = 0; i < count; i++)
                _hashCode = 31 * _hashCode + _exceptionTypes[i].hashCode();
        }

        return _hashCode;
    }

    protected static int count(Object[] array)
    {
        return array == null ? 0 : array.length;
    }

    /**
     * Returns true if the other object is an instance of MethodSignature with identical values for
     * return type, name, parameter types and exception types.
     */
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof MethodSignatureImpl))
            return false;

        MethodSignatureImpl ms = (MethodSignatureImpl) o;

        if (_returnType != ms._returnType)
            return false;

        if (!_name.equals(ms._name))
            return false;

        if (mismatch(_parameterTypes, ms._parameterTypes))
            return false;

        return !mismatch(_exceptionTypes, ms._exceptionTypes);
    }

    protected boolean mismatch(Class[] a1, Class[] a2)
    {
        int a1Count = count(a1);
        int a2Count = count(a2);

        if (a1Count != a2Count)
            return true;

        // Hm. What if order is important (for exceptions)? We're really saying here that they
        // were derived from the name Method.

        for (int i = 0; i < a1Count; i++)
        {
            if (!a1[i].isAssignableFrom(a2[i]))
                return true;
        }

        return false;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append(ClassFabUtils.getJavaClassName(_returnType));
        buffer.append(" ");
        buffer.append(_name);
        buffer.append("(");

        for (int i = 0; i < count(_parameterTypes); i++)
        {
            if (i > 0)
                buffer.append(", ");

            buffer.append(ClassFabUtils.getJavaClassName(_parameterTypes[i]));
        }

        buffer.append(")");

        for (int i = 0; i < count(_exceptionTypes); i++)
        {
            if (i == 0)
                buffer.append(" throws ");
            else
                buffer.append(", ");

            buffer.append(_exceptionTypes[i].getName());
        }

        return buffer.toString();
    }

    public String getUniqueId()
    {
        StringBuffer buffer = new StringBuffer(_name);
        buffer.append("(");

        for (int i = 0; i < count(_parameterTypes); i++)
        {
            if (i > 0)
                buffer.append(",");

            buffer.append(ClassFabUtils.getJavaClassName(_parameterTypes[i]));
        }

        buffer.append(")");

        return buffer.toString();
    }
    
    public boolean isGeneric()
    {
        return false;
    }
    
    public boolean isOverridingSignatureOf(MethodSignature ms)
    {
        if (!(ms instanceof MethodSignatureImpl))
            return false;
        
        MethodSignatureImpl sig = (MethodSignatureImpl)ms;
        
        if (_returnType != sig._returnType)
            return false;

        if (!_name.equals(sig._name))
            return false;

        if (mismatch(_parameterTypes, sig._parameterTypes))
            return false;

        return exceptionsEncompass(sig._exceptionTypes);
    }

    /**
     * The nuts and bolts of checking that another method signature's exceptions are a subset of
     * this signature's.
     */

    protected boolean exceptionsEncompass(Class[] otherExceptions)
    {
        int ourCount = count(_exceptionTypes);
        int otherCount = count(otherExceptions);

        // If we have no exceptions, then ours encompass theirs only if they
        // have no exceptions, either.

        if (ourCount == 0)
            return otherCount == 0;

        boolean[] matched = new boolean[otherCount];
        int unmatched = otherCount;

        for (int i = 0; i < ourCount && unmatched > 0; i++)
        {
            for (int j = 0; j < otherCount; j++)
            {
                // Ignore exceptions that have already been matched
                
                if (matched[j])
                    continue;

                // When one of our exceptions is a super-class of one of their exceptions,
                // then their exceptions is matched.
                
                if (_exceptionTypes[i].isAssignableFrom(otherExceptions[j]))
                {
                    matched[j] = true;
                    unmatched--;
                }
            }
        }

        return unmatched == 0;
    }
}
