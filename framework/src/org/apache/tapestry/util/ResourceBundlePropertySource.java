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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.tapestry.engine.IPropertySource;

/**
 *  A property source that is based on a {@link java.util.ResourceBundle}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ResourceBundlePropertySource implements IPropertySource
{
    private ResourceBundle _bundle;

    public ResourceBundlePropertySource(ResourceBundle bundle)
    {
        _bundle = bundle;
    }

    /**
     *  Gets the value from the bundle by invoking
     *  {@link ResourceBundle#getString(java.lang.String)}.  If
     *  the bundle does not contain the key (that is, it it
     *  throws {@link java.util.MissingResourceException}), then
     *  null is returned.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        try
        {
            return _bundle.getString(propertyName);
        }
        catch (MissingResourceException ex)
        {
            return null;
        }
    }

}
