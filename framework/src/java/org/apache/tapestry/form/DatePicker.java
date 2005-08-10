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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.hivemind.Resource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Provides a Form <tt>java.util.Date</tt> field component for selecting dates. [ <a
 * href="../../../../../ComponentReference/DatePicker.html">Component Reference </a>] As of 4.0,
 * DatePicker can indicate that it is required, use a custom translator (e.g. for java.sql.Date),
 * and perform validation on the submitted date.
 * <p>
 * As of 4.0, this component can be configurably translated and validated.
 * 
 * @author Paul Geerts
 * @author Malcolm Edgar
 * @author Paul Ferraro
 * @since 2.2
 */

public abstract class DatePicker extends AbstractFormComponent implements TranslatedField
{
    public abstract Date getValue();

    public abstract void setValue(Date value);

    public abstract boolean isDisabled();

    public abstract boolean getIncludeWeek();

    public abstract IAsset getIcon();

    private IScript _script;

    private static final String SYM_NAME = "name";

    private static final String SYM_FORMNAME = "formName";

    private static final String SYM_MONTHNAMES = "monthNames";

    private static final String SYM_SHORT_MONTHNAMES = "shortMonthNames";

    private static final String SYM_WEEKDAYNAMES = "weekDayNames";

    private static final String SYM_SHORT_WEEKDAYNAMES = "shortWeekDayNames";

    private static final String SYM_FIRSTDAYINWEEK = "firstDayInWeek";

    private static final String SYM_MINDAYSINFIRSTWEEK = "minimalDaysInFirstWeek";

    private static final String SYM_FORMAT = "format";

    private static final String SYM_INCL_WEEK = "includeWeek";

    private static final String SYM_VALUE = "value";

    private static final String SYM_BUTTONONCLICKHANDLER = "buttonOnclickHandler";

    /**
     * Injected
     * 
     * @since 4.0
     */
    public abstract IScriptSource getScriptSource();

    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad()
    {
        super.finishLoad();

        IScriptSource source = getScriptSource();

        Resource location = getSpecification().getSpecificationLocation().getRelativeResource(
                "DatePicker.script");

        _script = source.getScript(location);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);

        boolean disabled = isDisabled();
        DateTranslator translator = (DateTranslator) getTranslator();
        Locale locale = getPage().getLocale();
        SimpleDateFormat format = translator.getDateFormat(locale);

        DateFormatSymbols dfs = format.getDateFormatSymbols();
        Calendar cal = Calendar.getInstance(locale);

        String name = getName();
        
        String value = getTranslatedFieldSupport().format(this, getValue());
        
        Map symbols = new HashMap();

        symbols.put(SYM_NAME, name);
        symbols.put(SYM_FORMAT, format.toPattern());
        symbols.put(SYM_INCL_WEEK, getIncludeWeek() ? Boolean.TRUE : Boolean.FALSE);

        symbols.put(SYM_MONTHNAMES, makeStringList(dfs.getMonths(), 0, 12));
        symbols.put(SYM_SHORT_MONTHNAMES, makeStringList(dfs.getShortMonths(), 0, 12));
        symbols.put(SYM_WEEKDAYNAMES, makeStringList(dfs.getWeekdays(), 1, 8));
        symbols.put(SYM_SHORT_WEEKDAYNAMES, makeStringList(dfs.getShortWeekdays(), 1, 8));
        symbols.put(SYM_FIRSTDAYINWEEK, new Integer(cal.getFirstDayOfWeek() - 1));
        symbols.put(SYM_MINDAYSINFIRSTWEEK, new Integer(cal.getMinimalDaysInFirstWeek()));
        symbols.put(SYM_FORMNAME, getForm().getName());
        symbols.put(SYM_VALUE, getValue());

        _script.execute(cycle, pageRenderSupport, symbols);

        renderDelegatePrefix(writer, cycle);

        writer.beginEmpty("input");
        writer.attribute("type", "text");
        writer.attribute("name", name);
        writer.attribute("value", value);
        writer.attribute("title", format.toLocalizedPattern());

        if (disabled)
            writer.attribute("disabled", "disabled");

        renderIdAttribute(writer, cycle);

        renderDelegateAttributes(writer, cycle);

        getTranslatedFieldSupport().renderContributions(this, writer, cycle);
        getValidatableFieldSupport().renderContributions(this, writer, cycle);

        renderInformalParameters(writer, cycle);

        writer.printRaw("&nbsp;");

        if (!disabled)
        {
            writer.begin("a");
            writer.attribute("href", (String) symbols.get(SYM_BUTTONONCLICKHANDLER));
        }

        IAsset icon = getIcon();

        writer.beginEmpty("img");
        writer.attribute("src", icon.buildURL(cycle));
        writer.attribute("border", 0);

        if (!disabled)
            writer.end();

        renderDelegateSuffix(writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = cycle.getParameter(getName());
        
        try
        {
            Date date = (Date) getTranslatedFieldSupport().parse(this, value);
            
            setValue(date);
            
            getValidatableFieldSupport().validate(this, writer, cycle, date);
        }
        catch (ValidatorException e)
        {
            getForm().getDelegate().record(e);
        }
    }

    /**
     * Create a list of quoted strings. The list is suitable for initializing a JavaScript array.
     */
    private String makeStringList(String[] a, int offset, int length)
    {
        StringBuffer b = new StringBuffer();
        for (int i = offset; i < length; i++)
        {
            // JavaScript is sensitive to some UNICODE characters. So for
            // the sake of simplicity, we just escape everything
            b.append('"');
            char[] ch = a[i].toCharArray();
            for (int j = 0; j < ch.length; j++)
            {
                if (ch[j] < 128)
                {
                    b.append(ch[j]);
                }
                else
                {
                    b.append(escape(ch[j]));
                }
            }

            b.append('"');
            if (i < length - 1)
            {
                b.append(", ");
            }
        }
        return b.toString();

    }

    /**
     * Create an escaped Unicode character
     * 
     * @param c
     * @return The unicode character in escaped string form
     */
    private static String escape(char c)
    {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < 4; i++)
        {
            b.append(Integer.toHexString(c & 0x000F).toUpperCase());
            c >>>= 4;
        }
        b.append("u\\");
        return b.reverse().toString();
    }

    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    /**
     * Injected.
     */
    public abstract TranslatedFieldSupport getTranslatedFieldSupport();

    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#isRequired()
     */
    public boolean isRequired()
    {
        return getValidatableFieldSupport().isRequired(this);
    }
}