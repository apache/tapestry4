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
import java.util.List;

import org.apache.tapestry.engine.IPropertySource;

/**
 *  An implementation of {@link IPropertySource}
 *  that delegates to a list of other implementations.  This makes
 *  it possible to create a search path for property values.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class DelegatingPropertySource implements IPropertySource
{
    private List _sources = new ArrayList();
    
    public DelegatingPropertySource()
    {
    }
    
    public DelegatingPropertySource(IPropertySource delegate)
    {
        addSource(delegate);
    }
    
    /**
     *  Adds another source to the list of delegate property sources.
     *  This is typically only done during initialization
     *  of this DelegatingPropertySource.
     * 
     **/
    
    public void addSource(IPropertySource source)
    {
        _sources.add(source);
    }
    
    /**
     *  Re-invokes the method on each delegate property source, 
     *  in order, return the first non-null value found.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        String result = null;
        int count = _sources.size();
        
        for (int i = 0; i < count; i++)
        {
            IPropertySource source = (IPropertySource)_sources.get(i);
            
            result = source.getPropertyValue(propertyName);
            
            if (result != null)
                break;
        }
        
        return result;        
    }

}
