/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.workbench.table;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.components.Table;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;

/**
 * @author mindbridge
 *
 */
public abstract class LocaleSelection extends BaseComponent implements ILocaleSelectionListener
{
    // immutable
    private VerbosityRating m_objVerbosityRating;

    // temporary
    private Locale m_objCurrentLocale;

    // properties
    public abstract Locale getCurrentLocale();
    public abstract Set getLocaleSet();
    public abstract void setLocaleSet(Set objLocaleSet);

    /**
     * Creates a new LocaleSelection component
     */
    public LocaleSelection()
    {
        m_objVerbosityRating = new VerbosityRating();
    }

    /**
     * @see org.apache.tapestry.workbench.table.ILocaleSelectionListener#localesSelected(Locale[])
     */
    public void localesSelected(Locale[] arrLocales)
    {
        Set objLocaleSet = getLocaleSet();
        CollectionUtils.addAll(objLocaleSet, arrLocales);
        // ensure that the framework knows about the change and the set is persisted
        setLocaleSet(objLocaleSet);
    }

    public ITableColumn getCurrencyColumn()
    {
        // The column value is extracted in a custom evaluator class
        return new SimpleTableColumn("Currency", new CurrencyEvaluator(), true);
    }

    public ITableColumn getDateFormatColumn()
    {
        // The entire column is defined using a custom column class
        return new DateFormatColumn(new Date());
    }

    /**
     * Returns the verbosity of the current locale. 
     * This is used by the Block rendering the 'Verbosity' column
     * @return int the current locale verbosity
     */
    public int getCurrentLocaleVerbosity()
    {
        int nVerbosity = VerbosityRating.calculateVerbosity(getCurrentLocale());
        return nVerbosity;
    }

    /**
     * Generates the context that will be passed to the deleteLocale() listener 
     * if a "remove" link is selected. <p>
     * 
     * This is used by the Block rendering the 'Remove' column.
     * 
     * @return String[] the context for the deleteLocale() listener
     */
    public String[] getDeleteLocaleContext()
    {
        Locale objLocale = getCurrentLocale();
        return new String[] {
            objLocale.getLanguage(),
            objLocale.getCountry(),
            objLocale.getVariant()};
    }

    /**
     * A listener invoked when a "remove" link is selected. 
     * It removes from the data model the locale corresponding to the link. <p>
     * 
     * @param objCycle the request cycle
     */
    public void deleteLocale(IRequestCycle objCycle)
    {
        Object[] arrParams = objCycle.getServiceParameters();
        Locale objLocale =
            new Locale(arrParams[0].toString(), arrParams[1].toString(), arrParams[2].toString());
        getLocaleSet().remove(objLocale);
    }

    /**
     * A class defining the logic for getting the currency symbol from a locale
     */
    private static class CurrencyEvaluator implements ITableColumnEvaluator
    {
        /**
         * @see org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator#getColumnValue(ITableColumn, Object)
         */
        public Object getColumnValue(ITableColumn objColumn, Object objRow)
        {
            Locale objLocale = (Locale) objRow;
            String strCountry = objLocale.getCountry();
            if (strCountry == null || strCountry.equals(""))
                return "";

            DecimalFormatSymbols objSymbols = new DecimalFormatSymbols(objLocale);
            return objSymbols.getCurrencySymbol();
        }
    }

    /**
     * A class defining a column for displaying the date format
     */
    private static class DateFormatColumn extends SimpleTableColumn
    {
        private Date m_objDate;

        public DateFormatColumn(Date objDate)
        {
            super("Date Format", true);
            m_objDate = objDate;
        }

        public Object getColumnValue(Object objRow)
        {
            Locale objLocale = (Locale) objRow;
            DateFormat objFormat =
                DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, objLocale);
            return objFormat.format(m_objDate);
        }
    }

}
