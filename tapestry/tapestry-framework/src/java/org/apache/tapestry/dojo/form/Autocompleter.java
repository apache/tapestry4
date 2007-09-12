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

import org.apache.tapestry.*;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.ValidatableField;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.valid.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An html field similar to a <code>select</code> input field that 
 * is wrapped by a dojo ComboBox widget.
 * 
 * This component uses the {@link IAutocompleteModel} to retrieve and match against
 * selected values.
 * 
 * @author jkuhnert
 */
public abstract class Autocompleter extends AbstractFormWidget implements ValidatableField, IJSONRender, IDirect
{
    // mode, can be remote or local (local being from html rendered option elements)
    private static final String MODE_REMOTE = "remote";
    private static final String MODE_LOCAL = "local";    
    
    /**
     * 
     * {@inheritDoc}
     */
    protected void renderFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        IAutocompleteModel model = getModel();
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        Object value = getValue();
        Object key = value != null && !"".equals(value.toString()) ? model.getPrimaryKey(value) : null;
        
        renderDelegatePrefix(writer, cycle);
        
        writer.begin("select");
        writer.attribute("name", getName());
        writer.attribute("autocomplete", "off"); // turn off native html autocomplete
        
        if (isDisabled())
            writer.attribute("disabled", "disabled");
        
        renderIdAttribute(writer, cycle);
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        // Apply informal attributes.
        renderInformalParameters(writer, cycle);
        
        writer.print(" ");
        
        if (isLocal()) 
        {
            List list = model.getValues("");
            for (int i=0; i<list.size(); i++) 
            {
                Object optionKey = model.getPrimaryKey(list.get(i));

                writer.begin("option");
                writer.attribute("value", getDataSqueezer().squeeze(optionKey));

                if (optionKey!=null && optionKey.equals(key))
                    writer.attribute("selected", "selected");
                
                writer.print(model.getLabelFor(list.get(i)));
                writer.end();
            }
        }
        
        writer.end();
        renderDelegateSuffix(writer, cycle);
        
        Map parms = new HashMap();
        parms.put("id", getClientId());
        
        JSONObject json = new JSONObject();
        if (!isLocal())
        {
            ILink link = getDirectService().getLink(true, new DirectServiceParameter(this));
            json.put("dataUrl", link.getURL() + "&filter=%{searchString}");
        }
        
        json.put("mode", isLocal() ? MODE_LOCAL : MODE_REMOTE);
        json.put("widgetId", getName());
        json.put("name", getName());
        json.put("searchDelay", getSearchDelay());
        json.put("fadeTime", getFadeTime());
        json.put("maxListLength", getMaxListLength());
        json.put("forceValidOption", isForceValidOption());
        json.put("disabled", isDisabled());
        
        json.put("value", key != null ? getDataSqueezer().squeeze(key) : "");
        json.put("label", value != null ? model.getLabelFor(value) : "");
        
        parms.put("props", json.toString());
        parms.put("form", getForm().getName());
        parms.put("widget", this);
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
        getScript().execute(this, cycle, prs, parms);
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderComponent(IJSONWriter writer, IRequestCycle cycle)
    {
        IAutocompleteModel model = getModel();
        
        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");
        
        List filteredValues = model.getValues(getFilter());
        
        if (filteredValues == null)
            return;
        
        Object key = null;
        String label = null;
        
        JSONObject json = writer.object();
        
        for (int i=0; i < filteredValues.size(); i++) {
            Object value = filteredValues.get(i);
            
            key = model.getPrimaryKey(value);
            label = model.getLabelFor(value);
            
            json.put(getDataSqueezer().squeeze(key), label );
        }
        
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        Object object = null;
        
        try
        {
            if (value != null && value.length() > 0)
                object = getModel().getValue(getDataSqueezer().unsqueeze(value));
            
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
    
    public abstract IAutocompleteModel getModel();
    
    /** How long to wait(in ms) before searching after input is received. */
    public abstract int getSearchDelay();
    
    /** The duration(in ms) of the fade effect of list going away. */
    public abstract int getFadeTime();
    
    /** The maximum number of items displayed in select list before the scrollbar is activated. */
    public abstract int getMaxListLength();
    
    /** Forces select to only allow valid option strings. */
    public abstract boolean isForceValidOption();
    
    /** Forces select to work in local mode (no xhr). */
    public abstract boolean isLocal();    
    
    /** @since 2.2 * */
    public abstract Object getValue();

    /** @since 2.2 * */
    public abstract void setValue(Object value);
    
    /** @since 4.1 */
    public abstract void setFilter(String value);
    
    /** @since 4.1 */
    public abstract String getFilter();
    
    /** Injected. */
    public abstract DataSqueezer getDataSqueezer();
    
    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * Injected.
     */
    public abstract IEngineService getDirectService();
    
    /**
     * Injected.
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
    public List getUpdateComponents()
    {
        List comps = new ArrayList();
        comps.add(getClientId());
        
        return comps;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isAsync()
    {
        return true;
    }
    
    /** 
     * {@inheritDoc}
     */
    public boolean isJson()
    {
        return true;
    }
}
