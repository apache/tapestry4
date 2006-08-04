// Copyright Jun 10, 2006 The Apache Software Foundation
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.TranslatedField;
import org.apache.tapestry.form.TranslatedFieldSupport;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.Strftime;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Implementation of the dojo DropdownDatePicker widget as a tapestry
 * component. Wraps a form input field with a date picker icon next to it
 * that when clicked on reveals a calendar to choose date values from. 
 * 
 * @author jkuhnert
 */
public abstract class DropdownDatePicker extends AbstractFormWidget
    implements TranslatedField
{
    
    /** parameter. */
    public abstract Date getValue();
    
    public abstract void setValue(Date value);
    
    public abstract boolean isDisabled();
    
    /** Alt html text for the date icon, what is displayed when mouse hovers over icon. */
    public abstract String getIconAlt();
    
    /**
     * {@inheritDoc}
     */
    protected void renderFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        // dojo dates are in POSIX style formats so we format the value manually
        DateTranslator translator = (DateTranslator) getTranslator();
        
        renderDelegatePrefix(writer, cycle);
        
        // the html output doesn't matter very much as dojo
        // will create an inline input field for us anyways, but we do need
        // a node to reference
        writer.begin("div");
        renderIdAttribute(writer, cycle);
        
        renderDelegateAttributes(writer, cycle);
        
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
        
        renderInformalParameters(writer, cycle);
        
        writer.end();
        renderDelegateSuffix(writer, cycle);
        
        // now create widget parms
        JSONObject json = new JSONObject();
        json.put("inputId", getClientId());
        json.put("inputName", getName());
        json.put("iconAlt", getIconAlt());
        json.put("dateFormat", Strftime.convertToPosixFormat(translator.getPattern()));
        if (getValue() != null)
            json.put("date", getTranslatedFieldSupport().format(this, getValue()));
        
        Map parms = new HashMap();
        parms.put("clientId", getClientId());
        parms.put("props", json.toString());
        
        getScript().execute(this, cycle, TapestryUtils.getPageRenderSupport(cycle, this), parms);
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        try
        {
            Date date = (Date) getTranslatedFieldSupport().parse(this, value);

            getValidatableFieldSupport().validate(this, writer, cycle, date);

            setValue(date);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
    
    /** Injected. */
    public abstract IScript getScript();
    
    /** Injected. */
    public abstract TranslatedFieldSupport getTranslatedFieldSupport();
    
    /** Injected. */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();
    
}
