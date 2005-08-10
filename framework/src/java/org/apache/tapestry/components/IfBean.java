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

package org.apache.tapestry.components;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.services.DataSqueezer;

/**
 * @author mb
 */
public abstract class IfBean extends AbstractFormComponent
{
    public final static String IF_VALUE_ATTRIBUTE = "org.mb.tapestry.base.IfValue";

    public abstract IBinding getConditionValueBinding();

    public abstract boolean getCondition();

    public abstract boolean getVolatile();

    public abstract String getElement();

    public abstract IActionListener getListener();

    private boolean _rendering = false;

    private boolean _conditionValue;

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean cycleRewinding = cycle.isRewinding();

        // form may be null if component is not located in a form
        IForm form = (IForm) cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).
        if (cycleRewinding && form != null && !cycleRewinding)
            return;

        // get the condition. work with a hidden field if necessary
        _conditionValue = evaluateCondition(cycle, form, cycleRewinding);
        _rendering = true;

        try
        {
            // call listener
            IActionListener listener = getListener();
            if (listener != null)
                listener.actionTriggered(this, cycle);

            // now render if condition is true
            if (_conditionValue)
            {
                String element = getElement();

                boolean render = !cycleRewinding && HiveMind.isNonBlank(element);

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
        finally
        {
            _rendering = false;
        }

        cycle.setAttribute(IF_VALUE_ATTRIBUTE, new Boolean(_conditionValue));
    }

    protected boolean evaluateCondition(IRequestCycle cycle, IForm form, boolean cycleRewinding)
    {
        boolean condition;

        if (form == null || getVolatile())
        {
            condition = getCondition();
        }
        else
        {
            // we are in a form and we care -- load/store the condition in a hidden field
            String name = form.getElementId(this);

            if (!cycleRewinding)
            {
                condition = getCondition();
                writeValue(form, name, condition);
            }
            else
            {
                condition = readValue(cycle, name);
            }
        }

        // write condition value if parameter is bound
        IBinding conditionValueBinding = getConditionValueBinding();
        if (conditionValueBinding != null)
            conditionValueBinding.setObject(new Boolean(condition));

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
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "If.unable-to-convert-value",
                    booleanValue), this, null, ex);
        }

        form.addHiddenValue(name, externalValue);
    }

    private boolean readValue(IRequestCycle cycle, String name)
    {
        String submittedValue = cycle.getParameter(name);

        try
        {
            Object valueObject = getDataSqueezer().unsqueeze(submittedValue);
            if (!(valueObject instanceof Boolean))
                throw new ApplicationRuntimeException(Tapestry.format(
                        "If.invalid-condition-type",
                        submittedValue));

            return ((Boolean) valueObject).booleanValue();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "If.unable-to-convert-string",
                    submittedValue), this, null, ex);
        }
    }

    public abstract DataSqueezer getDataSqueezer();

    public boolean isDisabled()
    {
        return false;
    }

    /**
     * Returns the value of the condition
     * 
     * @return the condition value
     */
    public boolean getConditionValue()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "conditionValue");

        return _conditionValue;
    }

    // Do nothing in those methods, but make the JVM happy
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

}
