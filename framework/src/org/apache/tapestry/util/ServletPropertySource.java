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

import javax.servlet.ServletConfig;

import org.apache.tapestry.engine.IPropertySource;

/**
 *  Implementation of {@link IPropertySource}
 *  that returns values defined as Servlet initialization parameters
 *  (defined as <code>&lt;init-param&gt;</code> in the
 *  <code>web.xml</code> deployment descriptor.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/ 

public class ServletPropertySource implements IPropertySource
{
    private ServletConfig _config;
    
    public ServletPropertySource(ServletConfig config)
    {
        _config = config;
    }   
    
    /**
     *  Invokes {@link ServletConfig#getInitParameter(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return _config.getInitParameter(propertyName);
    }

}
