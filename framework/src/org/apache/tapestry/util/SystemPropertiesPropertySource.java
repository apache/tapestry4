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

import org.apache.tapestry.engine.IPropertySource;

/**
 *  Obtain properties from JVM system properties.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class SystemPropertiesPropertySource implements IPropertySource
{
    private static IPropertySource _shared;
    
    public static synchronized IPropertySource getInstance()
    {
        if (_shared == null)
            _shared = new SystemPropertiesPropertySource();
            
        return _shared; 
    }

    /**
     *  Delegates to {@link System#getProperty(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return System.getProperty(propertyName);
    }

}
