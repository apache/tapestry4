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

import java.util.Locale;

/**
 *  Contains the path to a localized resource and the locale for which it has been localized.
 *  This object is immutable.
 * 
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class LocalizedResource
{
    private String _resourcePath;
    private Locale _resourceLocale;


    public LocalizedResource(String resourcePath, Locale resourceLocale)
    {
        _resourcePath = resourcePath;
        _resourceLocale = resourceLocale;
    }
    
    /**
     * @return the locale for which this resource has been localized or null if it has not been localized at all
     */
    public Locale getResourceLocale()
    {
        return _resourceLocale;
    }

    /**
     * @return the path to the localized resource
     */
    public String getResourcePath()
    {
        return _resourcePath;
    }

}
