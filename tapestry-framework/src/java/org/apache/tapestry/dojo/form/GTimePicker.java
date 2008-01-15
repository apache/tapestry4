package org.apache.tapestry.dojo.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.TranslatedField;
import org.apache.tapestry.form.TranslatedFieldSupport;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidatorException;

import java.util.*;

/**
 * Implementation of an html form input field that has a dynamic drop down selection list of
 * time segments displayed in the {@link org.apache.tapestry.IPage}'s {@link java.util.Locale}.
 */
public abstract class GTimePicker  extends AbstractFormWidget implements TranslatedField
{
    /**
     * For a full day - broken up in to half hour segments.
     */
    static final int TIME_SEGMENT_LENGTH = 48;

    /**
     * Core value used to place input in to.
     * @return The current bound value, may be null.
     */
    public abstract Object getValue();

    public abstract void setValue(Object value);

    public abstract boolean isDisabled();

    /**
     * {@inheritDoc}
     */
    protected void renderFormWidget(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = getTranslatedFieldSupport().format(this, getValue());

        renderDelegatePrefix(writer, cycle);

        writer.beginEmpty("input");
        writer.attribute("type", "text");
        writer.attribute("autocomplete", "off");
        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        if (value != null)
            writer.attribute("value", value);

        renderIdAttribute(writer, cycle);
        renderDelegateAttributes(writer, cycle);

        getTranslatedFieldSupport().renderContributions(this, writer, cycle);
        getValidatableFieldSupport().renderContributions(this, writer, cycle);

        renderInformalParameters(writer, cycle);

        writer.closeTag();

        renderDelegateSuffix(writer, cycle);

        // Build up options value list

        Locale locale = getPage().getLocale();
        
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance(getPage().getLocale());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);

        StringBuffer optStr = new StringBuffer("[");
        int selectedIndex = -1;
        
        for(int i=0, hour=0; i < TIME_SEGMENT_LENGTH; i++)
        {
            if (i != 0)
            {
                optStr.append(",");
            }
            
            if (i == 24)
            {
                hour = 0;
                cal.set(Calendar.AM_PM, Calendar.PM);
            }
            
            cal.set(Calendar.HOUR,  hour);
            cal.set(Calendar.MINUTE, (i % 2 > 0) ? 30 : 0);

            String option = getTranslator().format(this, locale, cal.getTime());

            optStr.append("\"").append(option).append("\"");
            
            if (selectedIndex < 0 && value != null && value.equals(option))
            {
                selectedIndex = i;
            }

            if (i % 2 > 0)
            {
                hour++;
            }
        }

        optStr.append("]");
        
        // now create widget parms

        JSONObject json = new JSONObject();
        json.put("inputNodeId", getClientId());
        json.put("optionValues", new JSONLiteral(optStr.toString()));

        if (selectedIndex > -1)
        {
            json.put("selectedIndex", selectedIndex);
        }

        Map parms = new HashMap();
        parms.put("clientId", getClientId());
        parms.put("props", json.toString());
        parms.put("widget", this);

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
            Object translated = getTranslatedFieldSupport().parse(this, value);

            getValidatableFieldSupport().validate(this, writer, cycle, translated);

            setValue(translated);
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
