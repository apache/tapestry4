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

package org.apache.tapestry.record;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 *  Represents a change to a component on a page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PageChange implements IPageChange
{
    private String _componentPath;
    private String _propertyName;
    private Object _newValue;

    public PageChange(String componentPath, String propertyName, Object newValue)
    {
        _componentPath = componentPath;
        _propertyName = propertyName;
        _newValue = newValue;
    }

    /**
     *  The path to the component on the page, or null if the property
     *  is a property of the page.
     *
     **/

    public String getComponentPath()
    {
        return _componentPath;
    }

    /**
     *  The new value for the property, which may be null.
     *
     **/

    public Object getNewValue()
    {
        return _newValue;
    }

    /**
     *  The name of the property that changed.
     *
     **/

    public String getPropertyName()
    {
        return _propertyName;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("componentPath", _componentPath);
        builder.append("propertyName", _propertyName);
        builder.append("newValue", _newValue);
        
        return builder.toString();
    }
}