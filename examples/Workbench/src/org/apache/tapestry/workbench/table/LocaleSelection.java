//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.workbench.table;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.simple.ITableColumnEvaluator;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;

/**
 * @author mindbridge
 * @version $Id$
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
