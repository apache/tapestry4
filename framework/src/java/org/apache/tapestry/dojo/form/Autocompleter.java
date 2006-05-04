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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IDirect;
import org.apache.tapestry.IJSONRender;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.ValidatableField;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.valid.ValidatorException;

/**
 * An html field similar to a <code>select</code> input field that 
 * is wrapped by a dojo ComboBox widget.
 * 
 * @author jkuhnert
 */
public abstract class Autocompleter extends AbstractFormWidget 
    implements ValidatableField, IJSONRender, IDirect, IWidget
{
    
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
        
        if (getSubmitOnChange())
            writer.attribute("onchange", "javascript:   this.form.events.submit();");
        
        renderIdAttribute(writer, cycle);
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        // Apply informal attributes.
        renderInformalParameters(writer, cycle);
        
        writer.end();
        renderDelegateSuffix(writer, cycle);
        
        DirectServiceParameter dsp = 
            new DirectServiceParameter(this, new Object[]{}, 
                    new String[]{getId()}, true);
        ILink link = getDirectService().getLink(true, dsp);
        
        Map parms = new HashMap();
        parms.put("id", getClientId());
        
        StringBuffer str = new StringBuffer("{");
        str.append("dataUrl:'").append(link.getURL()).append("&filter=%{searchString}',")
        .append("mode:'remote',")
        .append("forceValidOption:true,")
        .append("name:'").append(getName()).append("'")
        .append("}");
        
        parms.put("props", str.toString());
        
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
        String value = cycle.getParameter(getName() + "_selected");
        
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
        setFilter(cycle.getParameter("filter"));
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
