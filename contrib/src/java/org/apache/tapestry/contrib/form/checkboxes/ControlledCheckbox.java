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

package org.apache.tapestry.contrib.form.checkboxes;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.Checkbox;

/**
 * @author mb
 * @since 4.0
 */
public abstract class ControlledCheckbox extends BaseComponent
{

    public abstract CheckboxGroup getGroup();

    public String getCheckboxName()
    {
        Checkbox checkbox = (Checkbox) getComponent("checkbox");
        String name = checkbox.getName();
        return name;
    }

    public IForm getForm()
    {
        Checkbox checkbox = (Checkbox) getComponent("checkbox");
        IForm form = checkbox.getForm();
        return form;
    }

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderComponent(writer, cycle);

        registerCheckbox();
    }

    protected void registerCheckbox()
    {
        getCheckboxGroup().registerControlledCheckbox(this);
    }

    public CheckboxGroup getCheckboxGroup()
    {
        CheckboxGroup group = getGroup();
        if (group == null)
        {
            IRequestCycle cycle = getPage().getRequestCycle();
            group = (CheckboxGroup) cycle
                    .getAttribute(CheckboxGroup.CHECKBOX_GROUP_ATTRIBUTE);
        }
        if (group == null)
            throw new ApplicationRuntimeException(
                    "The component "
                            + getExtendedId()
                            + " must be wrapped by a CheckboxGroup or the 'group' parameter must be set.");

        return group;
    }

}
