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

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.engine.ServiceEncodingImpl}.
 * 
 * @author Howard M. Lewis Ship
 */
public class TestServiceEncoding extends HiveMindTestCase
{
    public void testSetServletPath()
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl("/bar");

        assertEquals("/bar", se.getServletPath());
        assertEquals(false, se.isModified());

        se.setServletPath("/foo");

        assertEquals("/foo", se.getServletPath());
        assertEquals(true, se.isModified());
    }

    public void testCreateWithExistingMap()
    {
        Map parameters = new HashMap();

        ServiceEncodingImpl se = new ServiceEncodingImpl("/foo", parameters);

        se.setParameterValue("foo", "bar");

        assertEquals("bar", parameters.get("foo"));
    }

    public void testGetParameterValue()
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl("/foo");

        se.setParameterValue("foo", "bar");

        assertEquals(true, se.isModified());

        assertEquals("bar", se.getParameterValue("foo"));

        se.setParameterValues("flintstone", new String[]
        { "fred", "wilma", "dino" });

        assertEquals("fred", se.getParameterValue("flintstone"));

        assertNull(se.getParameterValue("unknown"));
    }

    public void testGetParameterValues()
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl("/foo");
        se.setParameterValues("flintstone", new String[]
        { "fred", "wilma", "dino" });

        assertListsEqual(new String[]
        { "fred", "wilma", "dino" }, se.getParameterValues("flintstone"));

        assertEquals(true, se.isModified());

        se.setParameterValue("foo", "bar");

        assertListsEqual(new String[]
        { "bar" }, se.getParameterValues("foo"));

        assertNull(se.getParameterValue("unknown"));
    }

    public void testResetModified()
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl("/bar");

        assertEquals(false, se.isModified());

        se.setServletPath("/foo");

        assertEquals(true, se.isModified());

        se.resetModified();

        assertEquals(false, se.isModified());
    }

    public void testGetParameterNames()
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl("/foo");

        se.setParameterValue("foo", "bar");
        se.setParameterValue("alpha", "beta");

        assertListsEqual(new String[]
        { "alpha", "foo" }, se.getParameterNames());
    }
}