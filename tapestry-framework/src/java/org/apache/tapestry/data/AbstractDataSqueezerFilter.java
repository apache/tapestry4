// Copyright May 31, 2006 The Apache Software Foundation
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
package org.apache.tapestry.data;

import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.DataSqueezerFilter;


/**
 * A useful superclass for data squeezer filters.  Subclasses only have
 * to implement the single object versions of the squeeze/unsqueeze methods.
 * 
 * @author jcarman
 */
public abstract class AbstractDataSqueezerFilter implements DataSqueezerFilter
{

    /**
     * Merely calls squeeze(Object,DataSqueezer) on each object in 
     * the array. 
     */
    public String[] squeeze( Object[] objects, DataSqueezer next )
    {
        final String[] squeezed = new String[objects.length];
        
        for( int i = 0; i < squeezed.length; i++ ) {
            
            squeezed[i] = squeeze( objects[i], next );
        }
        
        return squeezed;
    }

    /**
     * Merely calls unsqueeze(String,DataSqueezer) on each object in
     * the array. 
     */
    public Object[] unsqueeze( String[] strings, DataSqueezer next )
    {
        final Object[] unsqueezed = new Object[strings.length];
        
        for( int i = 0; i < unsqueezed.length; i++ ) {
            
            unsqueezed[i] = unsqueeze( strings[i], next );
        }
        
        return unsqueezed;
    }
}
