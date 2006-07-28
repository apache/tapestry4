// Copyright May 4, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.ValidatableField;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidatorException;

/**
 * An html field similar to a <code>select</code> input field that 
 * is wrapped by a dojo ComboBox widget.
 * 
 * @author jkuhnert
 */
public abstract class Autocompleter extends AbstractFormWidget 
    implements ValidatableField, IJSONRender, IDirect
{
    // mode, can be remote or local (local being from html rendered option elements)
    private static final String MODE_REMOTE = "remote";
    
    /**
     * 
     * {@inheritDoc}
     */
    protected void renderFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        renderDelegatePrefix(writer, cycle);
        
        writer.begin("select");
        writer.attribute("name", getName());
        
        if (isDisabled())
            writer.attribute("disabled", "disabled");
        
        renderIdAttribute(writer, cycle);
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        // Apply informal attributes.
        renderInformalParameters(writer, cycle);
        
        writer.end();
        renderDelegateSuffix(writer, cycle);
        
        ILink link = getDirectService().getLink(true, new DirectServiceParameter(this));
        
        Map parms = new HashMap();
        parms.put("id", getClientId());
        
        JSONObject json = new JSONObject();
        json.put("dataUrl", link.getURL() + "&filter=%{searchString}");
        json.put("mode", MODE_REMOTE);
        json.put("widgetId", getName());
        json.put("name", getName());
        
        IPropertySelectionModel model = getModel();
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        int count = model.getOptionCount();
        Object value = getValue();
        
        for (int i = 0; i < count; i++) {
            Object option = model.getOption(i);
            
            if (isEqual(option, value)) {
                json.put("value", model.getValue(i));
                json.put("label", model.getLabel(i));
                break;
            }
        }
        
        parms.put("props", json.toString());
        parms.put("form", getForm().getName());
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(cycle, prs, parms);
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderComponent(IJSONWriter writer, IRequestCycle cycle)
    {
        IPropertySelectionModel model = getModel();
        
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        int count = model.getOptionCount();
        
        for (int i = 0; i < count; i++)
        {
            String value = model.getValue(i);
            String label = model.getLabel(i);
            
            if (getFilter() == null || getFilter().trim().length() <= 0) {
                writer.put(value, label);
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
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormWidget(IMarkupWriter writer, IRequestCycle cycle)
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
    
    /** 
     * {@inheritDoc}
     */
    public boolean isStateful()
    {
        return true;
    }
    
    /**
     * Triggerd by using filterOnChange logic.
     * 
     * {@inheritDoc}
     */
    public void trigger(IRequestCycle cycle)
    {
        setFilter(cycle.getParameter("filter"));
    }
    
    public abstract IPropertySelectionModel getModel();
    
    /** @since 4.1 */
    public abstract boolean isFilterOnChange();
    
    /** whether or not to autocomplete the input text. */
    public abstract boolean isAutocomplete();
    
    /** How long to wait(in ms) before searching after input is received. */
    public abstract int getSearchDelay();
    
    /** The duration(in ms) of the fade effect of list going away. */
    public abstract int getFadeTime();
    
    /** The maximum number of items displayed in select list before the scrollbar is activated. */
    public abstract int getMaxListLength();
    
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

    /** 
     * {@inheritDoc}
     */
    public Collection getUpdateComponents()
    {
        List comps = new ArrayList();
        comps.add(getId());
        
        return comps;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isAsync()
    {
        return Boolean.TRUE;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isJson()
    {
        return Boolean.TRUE;
    }
}
