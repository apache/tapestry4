/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
 *  @version $Id$
 *  @since 2.4
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
