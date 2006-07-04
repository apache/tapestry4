// Copyright Apr 15, 2006 The Apache Software Foundation
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
package org.apache.tapestry.asset;

import java.util.Comparator;


/**
 * Used to guarantee that whenever present, the PATH key entry 
 * of an asset service link is always appended last in that path
 * string. This is required for certain relative url operations. (ie dojo baseRelativePath)
 * 
 * @author jkuhnert
 */
public class AssetComparator implements Comparator
{

    /** 
     * {@inheritDoc}
     */
    public int compare(Object o1, Object o2)
    {
        if (!String.class.isInstance(o1) || !String.class.isInstance(o2))
            return 1;
        
        String key1 = (String)o1;
        String key2 = (String)o2;
        
        if (key1.toUpperCase().equals("PATH") && !key2.toUpperCase().equals("PATH"))
            return 1;
        else if (key2.toUpperCase().equals("PATH") && !key1.toUpperCase().equals("PATH"))
            return -1;
        else return key1.compareToIgnoreCase(key2);
    }

}
