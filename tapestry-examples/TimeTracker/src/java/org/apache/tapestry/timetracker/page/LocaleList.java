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

import java.util.Locale;

import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ResponseBuilder;


/**
 * Simple locale listing example.
 *
 * @author jkuhnert
 */
public abstract class LocaleList extends BasePage
{
    
    public abstract Locale getCurrLocale();
    
    public abstract void setSelected(Locale locale);
    
    public abstract void setStatus(String status);
    
    public abstract ResponseBuilder getBuilder();
    
    public Locale[] getLocales()
    {
        return Locale.getAvailableLocales();
    }
    
    public void selectLocale(String language, String country, String variant)
    {
        setSelected(new Locale(language, country, variant));
    }
    
    @EventListener(targets = {"localeLink"}, events = "onmousedown")
    public void localeHovered(BrowserEvent event)
    {
        setStatus(event.toString());
        getBuilder().updateComponent("status");
    }
}
