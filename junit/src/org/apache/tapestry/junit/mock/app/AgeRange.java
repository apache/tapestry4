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

package org.apache.tapestry.junit.mock.app;

import org.apache.commons.lang.enum.Enum;


/**
 *  
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class AgeRange extends Enum
{
    
    public static final AgeRange CHILD = new AgeRange("CHILD");
    public static final AgeRange TEEN = new AgeRange("TEEN");
    public static final AgeRange ADULT = new AgeRange("ADULT");
    public static final AgeRange RETIREE = new AgeRange("RETIREE");
    public static final AgeRange ELDERLY = new AgeRange("ELDERLY");

    public static AgeRange[] getAllValues()
    {
        return new AgeRange[]
        {
            CHILD, TEEN, ADULT, RETIREE, ELDERLY
        };
    }

    private AgeRange(String name)
    {
        super(name);
    }

}
