/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.util.exception.ExceptionDescription;

/**
 *  Component used to display an already formatted exception.
 * 
 *  [<a href="../../../../../ComponentReference/ExceptionDisplay.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionDisplay extends BaseComponent
{
    private IBinding _exceptionsBinding;
    private ExceptionDescription _exception;
    private int _count;
    private int _index;
    private EvenOdd _evenOdd;

    public void setExceptionsBinding(IBinding value)
    {
        _exceptionsBinding = value;
    }

    public IBinding getExceptionsBinding()
    {
        return _exceptionsBinding;
    }

    /**
     *  Each time the current exception is set, as a side effect,
     *  the evenOdd helper bean is reset to even.
     * 
     **/
    
    public void setException(ExceptionDescription value)
    {
        _exception = value;
        
        _evenOdd.setEven(true);
    }

    public ExceptionDescription getException()
    {
        return _exception;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        ExceptionDescription[] exceptions =
            (ExceptionDescription[]) _exceptionsBinding.getObject(
                "exceptions",
                ExceptionDescription[].class);

        _count = exceptions.length;
        
        try
        {
            _evenOdd = (EvenOdd)getBeans().getBean("evenOdd");
            
            super.renderComponent(writer, cycle);
        }
        finally
        {
            _exception = null;
            _evenOdd = null;
        }
    }

    public void setIndex(int value)
    {
        _index = value;
    }

    public boolean isLast()
    {
        return _index == (_count - 1);
    }
}