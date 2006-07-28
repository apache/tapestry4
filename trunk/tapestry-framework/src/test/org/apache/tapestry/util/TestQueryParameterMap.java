// Copyright 2004, 2005 The Apache Software Foundation
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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.util.QueryParameterMap}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestQueryParameterMap extends BaseComponentTestCase
{
    public void testUnknownKey()
    {
        QueryParameterMap m = new QueryParameterMap();

        assertNull(m.getParameterValue("unknown"));
        assertNull(m.getParameterValues("unknown"));
    }

    public void testGetSingleValue()
    {
        QueryParameterMap m = new QueryParameterMap();

        m.setParameterValue("fred", "flintstone");

        assertEquals("flintstone", m.getParameterValue("fred"));
        assertListEquals(new String[]
        { "flintstone" }, m.getParameterValues("fred"));
    }

    public void testGetValuesArray()
    {
        QueryParameterMap m = new QueryParameterMap();

        String[] values = new String[]
        { "fred", "wilma" };

        m.setParameterValues("flintstone", values);

        assertListEquals(values, m.getParameterValues("flintstone"));

        m.setParameterValue("rubble", "barney");

        assertListEquals(new String[]
        { "barney" }, m.getParameterValues("rubble"));
    }

    public void testGetParameterNames()
    {
        QueryParameterMap m = new QueryParameterMap();

        m.setParameterValue("fred", "flintstone");
        m.setParameterValue("barney", "rubble");

        assertListEquals(new String[]
        { "barney", "fred" }, m.getParameterNames());
    }

    public void testExistingMap()
    {
        Map map = new HashMap();
        QueryParameterMap m = new QueryParameterMap(map);

        m.setParameterValue("fred", "flintstone");

        assertEquals("flintstone", map.get("fred"));
    }
}