// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Common code used when testing the various container adapters.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BaseWebTestCase extends HiveMindTestCase
{

    protected Enumeration newEnumeration()
    {
        List l = new ArrayList();
        l.add("fred");
        l.add("barney");

        return Collections.enumeration(l);
    }

    protected void checkList(List l)
    {
        assertEquals("barney", l.get(0));
        assertEquals("fred", l.get(1));
    }

}