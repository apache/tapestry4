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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.IPrimaryKeyConvertor;

/**
 * @author mindbridge
 *
 */
public abstract class LocaleList extends BaseComponent
{
    // immutable values
	private IPrimaryKeyConvertor m_objLocaleConvertor;
    
    public LocaleList()
    {
        // define an IPrimaryKeyConvertor that represents
        // a Locale object as a String and converts it back
        m_objLocaleConvertor = new IPrimaryKeyConvertor()
        {
            public Object getPrimaryKey(Object objValue)
            {
                Locale objLocale = (Locale) objValue;
                return objLocale.toString();
            }

            public Object getValue(Object objPrimaryKey)
            {
                StringTokenizer objTokenizer = new StringTokenizer((String) objPrimaryKey, "_");

                String strLanguage = "";
                if (objTokenizer.hasMoreTokens())
                    strLanguage = objTokenizer.nextToken(); 

                String strCountry = "";
                if (objTokenizer.hasMoreTokens())
                    strCountry = objTokenizer.nextToken(); 

                String strVariant = "";
                if (objTokenizer.hasMoreTokens())
                    strVariant = objTokenizer.nextToken();
                
                return new Locale(strLanguage, strCountry, strVariant); 
            }
        };
    }
    
    public IPrimaryKeyConvertor getLocaleConvertor()
    {
        return m_objLocaleConvertor;
    }

    public boolean getCheckboxSelected() 
    {
        return getSelectedLocales().contains(getCurrentLocale());
    }
    
    public void setCheckboxSelected(boolean bSelected) 
    {
        Locale objLocale = getCurrentLocale();
        Set setSelectedLocales = getSelectedLocales();
        
        if (bSelected)
            setSelectedLocales.add(objLocale);
        else
            setSelectedLocales.remove(objLocale);
        
        // persist value
        setSelectedLocales(setSelectedLocales);
    }

    public void selectLocales(IRequestCycle objCycle)
    {
        Set setSelectedLocales = getSelectedLocales();
        Locale[] arrLocales = new Locale[setSelectedLocales.size()];
        setSelectedLocales.toArray(arrLocales);

        ILocaleSelectionListener objListener = 
            (ILocaleSelectionListener) getLocaleSelectionListenerBinding().getObject();
        objListener.localesSelected(arrLocales);

        // clear selection
        setSelectedLocales(new HashSet());
    }

    public abstract IBinding getLocaleSelectionListenerBinding();
    
    public abstract Locale getCurrentLocale();

    public abstract Set getSelectedLocales();

    public abstract void setSelectedLocales(Set set);
}
