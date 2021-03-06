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
package org.apache.tapestry.timetracker.page;

import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ResponseBuilder;

import java.util.Locale;


/**
 * Simple locale listing example.
 *
 */
public abstract class LocaleList extends BasePage
{

    public static Locale[] LOCALES = Locale.getAvailableLocales();

    public abstract Locale getStoredCurrent();
    public abstract void setStoredCurrent(Locale locale);

    public abstract int getCurrentHash();
    public abstract void setCurrentHash(int hash);

    public Locale getCurrLocale()
    {
        return getStoredCurrent();
    }

    public void setCurrLocale(Locale locale)
    {
        setStoredCurrent(locale);
        setCurrentHash(locale.hashCode());
    }
    
    public abstract void setSelected(Locale locale);
    public abstract Locale getSelected();

    public abstract void setSelectedHash(int hash);
    public abstract int getSelectedHash();

    public abstract void setStatus(String status);
    
    public abstract ResponseBuilder getBuilder();
    
    public void selectLocale(BrowserEvent event, String language, String country, String variant)
    {
        setSelected(new Locale(language, country, variant));
        setSelectedHash(getSelected().hashCode());
        
        setStatus(event.toString());
        getBuilder().updateComponent("status");
    }

    public boolean isCurrentSelected()
    {
        return getSelected() != null && getCurrentHash() == getSelectedHash();
    }

    public String getRoundedUrl(String anchor)
    {
        return "rounded?c=" +
               (isCurrentSelected() ? "efefef" : "2A78B0")
               + "&bc=white&w=8&h=8&a=" + anchor;
    }
}
