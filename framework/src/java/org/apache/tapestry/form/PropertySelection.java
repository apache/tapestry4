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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IJSONRender;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.json.IJSONWriter;
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
 * a property; the {@link EnumPropertySelectionModel} class simplifies this.
 * <p>
 * Often, a drop-down list will contain an initial option that serves both as a label and to represent 
 * that nothing is selected. This can behavior can easily be achieved by decorating an existing 
 * {@link IPropertySelectionModel} with a {@link LabeledPropertySelectionModel}.
 * <p>
 * As of 4.0, this component can be validated.
 * 
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 * @author Jesse Kuhnert
 */
public abstract class PropertySelection extends AbstractFormComponent 
    implements ValidatableField, IJSONRender, IDirect
{
    /* logger */
    protected static final Logger _log = Logger.getLogger(PropertySelection.class);
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderDelegatePrefix(writer, cycle);
        
        writer.begin("select");
        writer.attribute("name", getName());
        
        if (isDisabled())
            writer.attribute("disabled", "disabled");
        
        if (getSubmitOnChange())
            writer.attribute("onchange", "javascript:   this.form.events.submit();");
        
        renderIdAttribute(writer, cycle);
        
        // if filtering add additional scripts
        // TODO: This isn't real yet, just getting things mostly working first
        if (isFilterOnChange()) {
            
            DirectServiceParameter dsp = 
                new DirectServiceParameter(this, new Object[]{}, 
                        new String[]{getId()}, true);
            ILink link = getDirectService().getLink(true, dsp);
            
            writer.attribute("dojoType", "ComboBox");
            writer.attribute("dataUrl", link.getURL());
            writer.attribute("mode", "remote");
            
            Map parms = new HashMap();
            parms.put("select", this);
            
            PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
            getScript().execute(cycle, prs, parms);
        }
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
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

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        Object object = getModel().translateValue(value);
        
        try
        {
            getValidatableFieldSupport().validate(this, writer, cycle, object);
            
            setValue(object);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderComponent(IJSONWriter writer, IRequestCycle cycle)
    {
        _log.warn("renderComponent() JSON request");
        IPropertySelectionModel model = getModel();
        
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        int count = model.getOptionCount();
        _log.warn("total count:" + count);
        
        for (int i = 0; i < count; i++)
        {
            String value = model.getValue(i);
            String label = model.getLabel(i);
            
            _log.warn("Filter value:" + getFilter() + "with label:" + label + " on count:" + i);
            
            if (getFilter() == null || getFilter().length() <= 0) {
                writer.put(value, label);
                _log.warn("Writing filter value");
                continue;
            }
            
            // primitive filter, for now
            // TODO: Create filter interface in IPropertySelectionModel
            if (getFilter() != null 
                    && label.toLowerCase().indexOf(getFilter().toLowerCase()) > -1) {
                writer.put(value, label);
            }
        }
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isStateful()
    {
        return false;
    }

    /** 
     * Triggerd by using filterOnChange logic.
     * 
     * {@inheritDoc}
     */
    public void trigger(IRequestCycle cycle)
    {
        setFilter("zeit");
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
    
    /** @since 4.1 */
    public abstract boolean isFilterOnChange();
    
    /** @since 2.2 * */
    public abstract boolean getSubmitOnChange();

    /** @since 2.2 * */
    public abstract Object getValue();

    /** @since 2.2 * */
    public abstract void setValue(Object value);
    
    /** @since 4.1 */
    public abstract void setFilter(String value);
    
    /** @since 4.1 */
    public abstract String getFilter();
    
    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * Injected.
     * @return
     */
    public abstract IEngineService getDirectService();
    
    /**
     * Injected.
     * @return
     */
    public abstract IScript getScript();
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
}
