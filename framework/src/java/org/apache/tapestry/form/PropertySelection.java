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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * A component used to render a drop-down list of options that the user may select. [ <a
 * href="../../../../../ComponentReference/PropertySelection.html">Component Reference </a>]
 * <p>
 * Earlier versions of PropertySelection (through release 2.2) were more flexible, they included a
 * <b>renderer </b> property that controlled how the selection was rendered. Ultimately, this proved
 * of little value and this portion of functionality was deprecated in 2.3 and will be removed in
 * 2.3.
 * <p>
 * Typically, the values available to be selected are defined using an
 * {@link org.apache.commons.lang.enum.Enum}. A PropertySelection is dependent on an
 * {@link IPropertySelectionModel} to provide the list of possible values.
 * <p>
 * Often, this is used to select a particular {@link org.apache.commons.lang.enum.Enum} to assign to
 * a property; the {@link EnumPropertySelectionModel} class simplifies this. As of 4.0,
 * PropertySelection can indicate that it is required. Often, a drop-down list will contain an
 * initial option that serves both as a label and to represent that nothing is selected. This can
 * behavior can easily be achieved by decorating an existing {@link IPropertySelectionModel} with a
 * {@link LabeledPropertySelectionModel}.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class PropertySelection extends AbstractRequirableField
{
    /**
     * @see org.apache.tapestry.form.validator.AbstractRequirableField#bind(org.apache.tapestry.IRequestCycle,
     *      java.lang.String)
     */
    public void bind(IMarkupWriter writer, IRequestCycle cycle) throws ValidatorException
    {
        if (isDisabled())
            return;

        setValue(getModel().translateValue(getSubmittedValue(cycle)));
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad()
    {
        setRequiredMessage(ValidationStrings.getMessagePattern(
                ValidationStrings.REQUIRED_SELECT_FIELD,
                getPage().getLocale()));
    }

    /**
     * Renders the component. The possible options, their labels, and the values to be encoded in
     * the form are provided by the {@link IPropertySelectionModel model}.
     * 
     * @see org.apache.tapestry.form.validator.AbstractRequirableField#renderRequirableFormComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderFormComponent(writer, cycle);

        renderDelegatePrefix(writer, cycle);

        writer.begin("select");
        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (getSubmitOnChange())
            writer.attribute("onchange", "this.form.submit()");

        renderDelegateAttributes(writer, cycle);

        // Apply informal attributes.
        renderInformalParameters(writer, cycle);

        writer.println();

        IPropertySelectionModel model = getModel();

        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");

        int count = model.getOptionCount();
        boolean foundSelected = false;
        Object value = getValue();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            writer.begin("option");
            writer.attribute("value", model.getValue(i));

            if (!foundSelected && isEqual(option, value))
            {
                writer.attribute("selected", "selected");

                foundSelected = true;
            }

            writer.print(model.getLabel(i));

            writer.end();

            writer.println();
        }

        writer.end(); // <select>

        renderDelegateSuffix(writer, cycle);
    }

    private boolean isEqual(Object left, Object right)
    {
        // Both null, or same object, then are equal

        if (left == right)
            return true;

        // If one is null, the other isn't, then not equal.

        if (left == null || right == null)
            return false;

        // Both non-null; use standard comparison.

        return left.equals(right);
    }

    public abstract IPropertySelectionModel getModel();

    /** @since 2.2 * */
    public abstract boolean getSubmitOnChange();

    /** @since 2.2 * */
    public abstract Object getValue();

    /** @since 2.2 * */
    public abstract void setValue(Object value);
}