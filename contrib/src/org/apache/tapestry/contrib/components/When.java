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

package org.apache.tapestry.contrib.components;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.Conditional;

/**
 *  Represents an alternative whithin a {@link Choose} component. 
 *  The default alternative is described by the Otherwise component.
 *
 *  [<a href="../../../../../../ComponentReference/contrib.When.html">Component Reference</a>]
 *
 *  @author David Solis
 *  @version $Id$
 * 
 **/
public abstract class When extends Conditional
{
    /** Parent of this component. */

    private Choose _choose;

    /**
     *  Renders its wrapped components only if the condition is true and its parent {@link Choose}
     *  allows it. In addition, if element is specified, can emulate that HTML element.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Choose choose = getChoose();

        if (choose == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("When.must-be-contained-by-choose"),
                this,
                null,
                null);

        if (!choose.isConditionMet() && getCondition())
        {
            choose.setConditionMet(true);
            super.renderComponent(writer, cycle);
        }
    }

    protected boolean evaluateCondition()
    {
        return true;
    }

    public boolean getInvert()
    {
        // This component doesn't require invert parameter.
        return false;
    }

    /**
     *  @return Choose
     */
    public Choose getChoose()
    {
        return _choose;
    }

    /**
     *  Sets the choose.
     *  @param value The choose to set
     */
    public void setChoose(Choose value)
    {
        _choose = value;
    }

}
