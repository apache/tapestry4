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
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * A base class for building components that correspond to HTML form elements.
 * All such components must be wrapped (directly or indirectly) by
 * a {@link Form} component.
 *
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 * @since 1.0.3
 */
public abstract class AbstractFormComponent extends AbstractComponent implements IFormComponent
{
    private static final String SELECTED_ATTRIBUTE_NAME = "org.apache.tapestry.form.SelectedField";
    
    public abstract IForm getForm();

    public abstract void setForm(IForm form);

    public abstract String getName();

    public abstract void setName(String name);
    
    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
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
            renderFormComponent(writer, cycle);

            if (delegate.isInError())
            {
                select(cycle);
            }
        }
    }

    protected void select(IRequestCycle cycle)
    {
        if (cycle.getAttribute(SELECTED_ATTRIBUTE_NAME) == null)
        {
            PageRenderSupport pageRenderSupport = TapestryUtils.getOptionalPageRenderSupport(cycle);
            
            if (pageRenderSupport != null)
            {
                String formName = getForm().getName();
                String fieldName = getName();

                String script = "focus(document." + formName + "." + fieldName + ")";

                pageRenderSupport.addInitializationScript(script);
                
                // Put a marker in, indicating that the selected field is known.

                cycle.setAttribute(SELECTED_ATTRIBUTE_NAME, Boolean.TRUE);
            }
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
    
    protected abstract void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle);
    
    protected abstract void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle);
}