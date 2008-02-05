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

package org.apache.tapestry.form;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  [<a href="../../../../../ComponentReference/Radio.html">Component Reference</a>]
 *
 *
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 *
 *  @author Howard Lewis Ship
 *
 **/

public abstract class Radio extends AbstractFormComponent
{
    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *
     **/

    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        RadioGroup group = RadioGroup.get(cycle);

        if (group == null)
            throw new ApplicationRuntimeException(Tapestry.getMessage("Radio.must-be-contained-by-group"),
                                                  this,
                                                  null,
                                                  null);

        int option = group.getNextOptionId();

        setClientId(group.getName()+option);
        setName(group.getName());

        writer.beginEmpty("input");

        writer.attribute("type", "radio");

        writer.attribute("name", getName());

        renderIdAttribute(writer, cycle);

        // As the group if the value for this Radio matches the selection
        // for the group as a whole; if so this is the default radio and is checked.

        if (group.isSelection(getValue()))
            writer.attribute("checked", "checked");

        if (isDisabled() || group.isDisabled())
            writer.attribute("disabled", "disabled");

        // The value for the Radio matches the option number (provided by the RadioGroup).
        // When the form is submitted, the RadioGroup will know which option was,
        // in fact, selected by the user.

        writer.attribute("value", option);

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        RadioGroup group = RadioGroup.get(cycle);

        if (group == null)
            throw new ApplicationRuntimeException(
                    Tapestry.getMessage("Radio.must-be-contained-by-group"),
                    this,
                    null,
                    null);

        int option = group.getNextOptionId();

        setClientId(group.getName()+option);
        setName(group.getName());

        // If not disabled and this is the selected button within the radio group,
        // then update set the selection from the group to the value for this
        // radio button.  This will update the selected parameter of the RadioGroup.

        if (!isDisabled() && !group.isDisabled() && group.isSelected(option))
            group.updateSelection(getValue());
    }

    /**
     * Overridden to do nothing so that special {@link RadioGroup} semantics are handled properly.
     * @param form The form to set the name on.
     */
    protected void setName(IForm form)
    {
    }

    public abstract boolean isDisabled();

    public abstract Object getValue();
}
