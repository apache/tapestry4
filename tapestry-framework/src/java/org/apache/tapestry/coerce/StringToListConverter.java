// Copyright Oct 1, 2006 The Apache Software Foundation
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
package org.apache.tapestry.coerce;

import java.util.Arrays;

import org.apache.tapestry.TapestryUtils;


/**
 * Converts String values into {@link Collection} instances. String values can 
 * be a single "string" or multiple strings delimited by a "," comma.
 * 
 * @author jkuhnert
 */
public final class StringToListConverter implements TypeConverter
{
    /** 
     * {@inheritDoc}
     */
    public Object convertValue(Object value)
    {
        String input = (String)value;
        
        String[] values = TapestryUtils.split(input);
        
        return Arrays.asList(values);
    }

}