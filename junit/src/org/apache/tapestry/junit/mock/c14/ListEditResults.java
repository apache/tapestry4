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

package org.apache.tapestry.junit.mock.c14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.tapestry.html.BasePage;

/**
 *  Shows user's selected colors in a new page.  A bit of duplication with
 *  ListEdit, but such is testing.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public abstract class ListEditResults extends BasePage
{
    public abstract Map getColorMap();
    public abstract void setColorMap(Map colorMap);

    public abstract String getColorKey();

    private ResourceBundle _colorStrings;

    public List getSortedColorKeys()
    {
        Map map = getColorMap();
        List result = new ArrayList(map.keySet());

        Collections.sort(result);

        return result;
    }

    public String getColorName()
    {
        String key = getColorKey();
        Color color = (Color) getColorMap().get(key);

        return _colorStrings.getString(color.getName());
    }

    protected void finishLoad()
    {
        _colorStrings = ResourceBundle.getBundle(Color.class.getName() + "Strings", getLocale());
    }

}
