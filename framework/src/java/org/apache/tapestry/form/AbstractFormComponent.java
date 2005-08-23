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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstants;

/**
 * A base class for building components that correspond to HTML form elements. All such components
 * must be wrapped (directly or indirectly) by a {@link Form} component.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 * @since 1.0.3
 */
public abstract class AbstractFormComponent extends AbstractComponent implements IFormComponent
{
    public abstract IForm getForm();

    public abstract void setForm(IForm form);

    public abstract String getName();

    public abstract void setName(String name);

    /**
     * Returns true if the corresponding field, on the client side, can accept user focus (i.e.,
     * implements the focus() method). Most components can take focus, but a few ({@link Hidden})
     * override this method to return false.
     */

    protected boolean getCanTakeFocus()
    {
        return true;
    }

    /**
     * Should be connected to a parameter named "id" (annotations would be helpful here!). For
     * components w/o such a parameter, this will simply return null.
     */

    public abstract String getIdParameter();

    /**
     * Stores the actual id allocated (or null if the component doesn't support this).
     */

    public abstract void setClientId(String id);

    /**
     * Invoked from {@link #renderFormComponent(IMarkupWriter, IRequestCycle)} (that is, an
     * implementation in a subclass), to obtain an id and render an id attribute. Reads
     * {@link #getIdParameter()}.
     */

    protected void renderIdAttribute(IMarkupWriter writer, IRequestCycle cycle)
    {
        // If the user explicitly sets the id parameter to null, then
        // we honor that!

        String rawId = getIdParameter();

        if (rawId == null)
            return;

        String id = cycle.getUniqueId(TapestryUtils.convertTapestryIdToNMToken(rawId));

        // Store for later access by the FieldLabel (or JavaScript).

        setClientId(id);

        writer.attribute("id", id);
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = TapestryUtils.getForm(cycle, this);

        setForm(form);

        if (form.wasPrerendered(writer, this))
            return;

        IValidationDelegate delegate = form.getDelegate();

        delegate.setFormComponent(this);

        setName(form);

        if (form.isRewinding())
        {
            if (!isDisabled())
            {
                rewindFormComponent(writer, cycle);
            }
        }
        else if (!cycle.isRewinding())
        {
            boolean canFocus = getCanTakeFocus();
            
            if (canFocus && !isDisabled())
                delegate.registerForFocus(this, ValidationConstants.NORMAL_FIELD);

            renderFormComponent(writer, cycle);

            // Can't think of a component that can be in error and not take focus,
            // but whatever ...
            
            if (canFocus && delegate.isInError())
                delegate.registerForFocus(this, ValidationConstants.ERROR_FIELD);
        }
    }

    protected void renderDelegatePrefix(IMarkupWriter writer, IRequestCycle cycle)
    {
        getForm().getDelegate().writePrefix(writer, cycle, this, null);
    }

    protected void renderDelegateAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        getForm().getDelegate().writeAttributes(writer, cycle, this, null);
    }

    protected void renderDelegateSuffix(IMarkupWriter writer, IRequestCycle cycle)
    {
        getForm().getDelegate().writeSuffix(writer, cycle, this, null);
    }

    protected void setName(IForm form)
    {
        form.getElementId(this);
    }

    /**
     * Returns false. Subclasses that might be required must override this method. Typically, this
     * involves checking against the component's validators.
     * 
     * @since 4.0
     */
    public boolean isRequired()
    {
        return false;
    }

    protected abstract void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle);

    protected abstract void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle);
}