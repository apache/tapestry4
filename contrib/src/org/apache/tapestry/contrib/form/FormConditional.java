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

package org.apache.tapestry.contrib.form;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.io.DataSqueezer;

/**
 *  A conditional element on a page which will render its wrapped elements
 *  zero or one times.
 * 
 * This component is a variant of {@link org.apache.tapestry.components.Conditional}, 
 * but is designed for operation in a form. The component parameters are stored in 
 * hidden fields during rendering and are taken from those fields during the rewind, 
 * thus no StaleLink exceptions occur. 
 *
 *  [<a href="../../../../../ComponentReference/Conditional.html">Component Reference</a>]
 *
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public abstract class FormConditional extends AbstractFormComponent
{

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        boolean cycleRewinding = cycle.isRewinding();

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).

        if (cycleRewinding && !form.isRewinding())
            return;

        String name = form.getElementId(this);

        boolean condition = getCondition(cycle, form, name);
        boolean invert = getInvert(cycle, form, name);

        // call listener
        IActionListener listener = getListener();
        if (listener != null)
            listener.actionTriggered(this, cycle);

        // render the component body only if the condition is different from the invert
        if (condition != invert) {
            String element = getElement();
            
            boolean render = cycleRewinding && StringUtils.isNotEmpty(element);
            
            if (render)
            {
                writer.begin(element);
                renderInformalParameters(writer, cycle);
            }

            renderBody(writer, cycle);
            
            if (render)
                writer.end(element);
        }
    }

    private boolean getCondition(IRequestCycle cycle, IForm form, String name)
    {
        boolean condition;
        
        if (!cycle.isRewinding())
        {
            condition = getCondition();
            writeValue(form, name, condition);
        }
        else
        {
            RequestContext context = cycle.getRequestContext();
            String submittedConditions[] = context.getParameters(name);
            condition = convertValue(submittedConditions[0]);
        }

        IBinding conditionValueBinding = getConditionValueBinding();
        if  (conditionValueBinding != null) 
            conditionValueBinding.setBoolean(condition);
        
        return condition;
    }

    private boolean getInvert(IRequestCycle cycle, IForm form, String name)
    {
        boolean invert;
        
        if (!cycle.isRewinding())
        {
            invert = getInvert();
            writeValue(form, name, invert);
        }
        else
        {
            RequestContext context = cycle.getRequestContext();
            String submittedConditions[] = context.getParameters(name);
            invert = convertValue(submittedConditions[1]);
        }

        IBinding invertValueBinding = getInvertValueBinding();
        if (invertValueBinding != null) 
            invertValueBinding.setBoolean(invert);
        
        return invert;
    }

    private void writeValue(IForm form, String name, boolean value)
    {
        String externalValue;

        Object booleanValue = new Boolean(value);
        try
        {
            externalValue = getDataSqueezer().squeeze(booleanValue);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("FormConditional.unable-to-convert-value", booleanValue),
                this,
                null,
                ex);
        }

        form.addHiddenValue(name, externalValue);
    }

    private boolean convertValue(String value)
    {
        try
        {
            Object booleanValue = getDataSqueezer().unsqueeze(value);
            return Tapestry.evaluateBoolean(booleanValue);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("FormConditional.unable-to-convert-string", value),
                this,
                null,
                ex);
        }
    }

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    public boolean isDisabled()
    {
        return false;
    }

    public abstract boolean getCondition();
    public abstract boolean getInvert();
    public abstract String getElement();

    public abstract IBinding getConditionValueBinding();
    public abstract IBinding getInvertValueBinding();

    public abstract IActionListener getListener();

}
