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

package org.apache.tapestry.junit.enhance;

import java.lang.reflect.Method;

import org.apache.tapestry.*;
import org.apache.tapestry.enhance.MethodSignature;
import org.apache.tapestry.junit.TapestryTestCase;

/**
 *  Tests for the {@link org.apache.tapestry.enhance.MethodSignature}
 *  class used by the {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/
public class TestMethodSignature extends TapestryTestCase
{

    public TestMethodSignature(String name)
    {
        super(name);
    }

    private MethodSignature build(Class subject, String methodName)
    {
        Method[] m = subject.getDeclaredMethods();

        for (int i = 0; i < m.length; i++)
        {
            if (m[i].getName().equals(methodName))
                return new MethodSignature(m[i]);
        }

        unreachable();

        return null;
    }

    public void testEquals()
    {
    	// No parameters
        MethodSignature m1 = build(Object.class, "hashCode");
        MethodSignature m2 = build(Integer.class, "hashCode");
        
        // With parameters
		MethodSignature m3 = build(AbstractComponent.class, "renderComponent");
		MethodSignature m4 = build(BaseComponent.class, "renderComponent");
		
        assertEquals(true, m1.equals(m2));
        assertEquals(true, m3.equals(m4));
    }

    public void testUnequal()
    {
        MethodSignature m1 = build(Object.class, "hashCode");
        MethodSignature m2 = build(Integer.class, "toString");

        assertEquals(false, m1.equals(m2));

        assertEquals(false, m1.equals(null));

        assertEquals(false, m1.equals("hashCode"));
    }

    public void testHashCode()
    {
        MethodSignature m1 = build(Object.class, "toString");
        MethodSignature m2 = build(StringBuffer.class, "toString");
        MethodSignature m3 = build(AbstractComponent.class, "renderComponent");
        MethodSignature m4 = build(BaseComponent.class, "renderComponent");

        assertEquals(true, m1.hashCode() == m2.hashCode());
        assertEquals(true, m3.hashCode() == m4.hashCode());
        
        // Different signatures should have different hashcode
        
        assertEquals(true, m1.hashCode() != m3.hashCode());
    }

}
