// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.junit.utils;

import java.util.Random;

/**
 * Bean used by {@link org.apache.tapestry.junit.utils.TestPublicBean}.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class PublicBean
{
    private static Random r = new Random();

    private static long random()
    {
        return r.nextLong();
    }

    public String stringProperty = Long.toHexString(random());

    public Object objectProperty = new Long(random());

    public long longProperty = random();

    public double getSyntheticProperty()
    {
        return 3.14;
    }
}
