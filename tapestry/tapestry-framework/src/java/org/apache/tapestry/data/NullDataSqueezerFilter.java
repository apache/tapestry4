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

/**
 * Handles null data assumedly?
 * 
 * @author jcarman
 */
public class NullDataSqueezerFilter extends AbstractDataSqueezerFilter
{
    private static final String NULL_STRING = "X";
    
    public String squeeze(Object data, DataSqueezer dataSqueezer)
    {
        if (data == null)
            return NULL_STRING;
        
        return dataSqueezer.squeeze(data);
    }

    public Object unsqueeze(String string, DataSqueezer dataSqueezer)
    {
        if (NULL_STRING.equals(string))
            return null;
        
        return dataSqueezer.unsqueeze(string);
    }
}
