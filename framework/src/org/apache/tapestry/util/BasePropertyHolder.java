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

package org.apache.tapestry.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Base class implementation for the {@link IPropertyHolder} interface.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class BasePropertyHolder implements IPropertyHolder
{
    private static final int MAP_SIZE = 7;
    private Map properties;

    public String getProperty(String name)
    {
        if (properties == null)
            return null;

        return (String) properties.get(name);
    }

    public void setProperty(String name, String value)
    {
        if (value == null)
        {
            removeProperty(name);
            return;
        }

        if (properties == null)
            properties = new HashMap(MAP_SIZE);

        properties.put(name, value);
    }

    public void removeProperty(String name)
    {
        if (properties == null)
            return;

        properties.remove(name);
    }

    public List getPropertyNames()
    {
        if (properties == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(properties.keySet());
        
        Collections.sort(result);
        
        return result;
    }

}