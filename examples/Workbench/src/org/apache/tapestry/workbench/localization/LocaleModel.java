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

package org.apache.tapestry.workbench.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class LocaleModel implements IPropertySelectionModel
{
    private Locale locale;
    private List locales = new ArrayList();

    public LocaleModel(Locale locale)
    {
        this.locale = locale;
    }

    public void add(Locale locale)
    {
        locales.add(locale);
    }

    private Locale get(int index)
    {
        return (Locale) locales.get(index);
    }

    public String getLabel(int index)
    {
        return get(index).getDisplayName(locale);
    }

    public int getOptionCount()
    {
        return locales.size();
    }

    public Object getOption(int index)
    {
        return locales.get(index);
    }

    /**
     *  Returns the String version of the integer index.
     *
     **/

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public Object translateValue(String value)
    {
        int index = Integer.parseInt(value);

        return locales.get(index);
    }
}