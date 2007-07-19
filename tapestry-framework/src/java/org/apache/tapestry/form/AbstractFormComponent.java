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

import org.apache.tapestry.*;
import org.apache.tapestry.engine.NullWriter;
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
     * implements the focus() method). Most components can take focus (if not disabled), but a few ({@link Hidden})
     * override this method to always return false.
     */

    protected boolean getCanTakeFocus()
    {
        return !isDisabled();
    }

    /**
     * Should be connected to a parameter named "id" (annotations would be helpful here!). For
     * components w/o such a parameter, this will simply return null.
     */

    public abstract String getIdParameter();
    
    /**
     * Invoked by {@link AbstractComponent#render(IMarkupWriter, IRequestCycle)} to actually 
     * render the component (with any parameter values already set). 
     * This implementation checks the rewinding state of the {@link IForm} that contains the
     * component and forwards processing to either 
     * {@link #renderFormComponent(IMarkupWriter, IRequestCycle)} or 
     * {@link #rewindFormComponent(IMarkupWriter, IRequestCycle)}. 
     * Those two are the methods that subclasses should implement. 
     *  
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
            
            // This is for the benefit of the couple of components (LinkSubmit) that allow a body.
            // The body should render when the component rewinds.
            
            if (getRenderBodyOnRewind())
                renderBody(writer, cycle);
        }
        else if (!cycle.isRewinding())
        {
            if (!NullWriter.class.isInstance(writer))
                form.setFormFieldUpdating(true);
            
            renderFormComponent(writer, cycle);
            
            if (getCanTakeFocus() && !isDisabled())
            {
                delegate.registerForFocus(
                        this,
                        delegate.isInError() ? ValidationConstants.ERROR_FIELD
                                : ValidationConstants.NORMAL_FIELD);
            }

        }
    }

    /**
     * A small number of components should always render their body on rewind (even if the component
     * is itself disabled) and should override this method to return true. Components that
     * explicitly render their body inside
     * {@link #rewindFormComponent(IMarkupWriter, IRequestCycle)} should leave this method returning
     * false. Remember that if the component is {@link IFormComponent#isDisabled() disabled} then
     * {@link #rewindFormComponent(IMarkupWriter, IRequestCycle)} won't be invoked.
     * 
     * @return false; override this method to change.
     */
    protected boolean getRenderBodyOnRewind()
    {
        return false;
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
        setName(form.getElementId(this));
    }
    
    /**
     * {@inheritDoc}
     */
    protected void generateClientId()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    public String peekClientId()
    {
        if (getPage() == null)
            return null;
        
        IForm form = (IForm) getPage().getRequestCycle().getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        if (form == null)
            return null;

        return form.peekClientId(this);
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

    /**
     * Invoked from {@link #renderComponent(IMarkupWriter, IRequestCycle)} 
     * to render the component. 
     *  
     * @param writer
     * @param cycle
     */
    protected abstract void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Invoked from {@link #renderComponent(IMarkupWriter, IRequestCycle)} to rewind the 
     * component. If the component is {@link IFormComponent#isDisabled() disabled} 
     * this will not be invoked. 
     * 
     * @param writer
     * @param cycle
     */
    protected abstract void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle);
}
