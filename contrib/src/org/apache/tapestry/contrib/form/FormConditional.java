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

package org.apache.tapestry.contrib.form;

import java.io.IOException;

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
 *  [<a href="../../../../../ComponentReference/contrib.FormConditional.html">Component Reference</a>]
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

        // call listener
        IActionListener listener = getListener();
        if (listener != null)
            listener.actionTriggered(this, cycle);

        // render the component body only if the condition is true
        if (condition) {
            String element = getElement();
            
            boolean render = !cycleRewinding && Tapestry.isNonBlank(element);
            
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
    public abstract String getElement();

    public abstract IBinding getConditionValueBinding();

    public abstract IActionListener getListener();

}
