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
