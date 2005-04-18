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

package org.apache.tapestry.coerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests several implementations of {@link org.apache.tapestry.coerce.TypeConverter}that return
 * Boolean values.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestBooleanConverters extends HiveMindTestCase
{
    public void testStringToBoolean()
    {
        StringToBooleanConverter c = new StringToBooleanConverter();

        assertSame(Boolean.TRUE, c.convertValue("fred"));

        // The special case
        assertSame(Boolean.FALSE, c.convertValue("false"));

        assertSame(Boolean.FALSE, c.convertValue(" "));
        assertSame(Boolean.FALSE, c.convertValue(""));

        // Actually, null will never be passed in, but ..

        assertSame(Boolean.FALSE, c.convertValue(null));
    }

    public void testMapToBoolean()
    {
        Map m = new HashMap();

        TypeConverter c = new MapToBooleanConverter();

        assertSame(Boolean.FALSE, c.convertValue(m));

        m.put("foo", "bar");

        assertSame(Boolean.TRUE, c.convertValue(m));
    }

    public void testNullToBoolean()
    {
        assertSame(Boolean.FALSE, new NullToBooleanConverter().convertValue("doesn't matter"));
    }

    public void testCollectionToBoolean()
    {
        List l = new ArrayList();
        TypeConverter c = new CollectionToBooleanConverter();

        assertSame(Boolean.FALSE, c.convertValue(l));

        l.add("foo");

        assertSame(Boolean.TRUE, c.convertValue(l));
    }

    public void testNumberToBoolean()
    {
        TypeConverter c = new NumberToBooleanConverter();

        assertSame(Boolean.FALSE, c.convertValue(new Integer(0)));
        assertSame(Boolean.TRUE, c.convertValue(new Integer(7)));
    }

    public void testObjectToBoolean()
    {
        TypeConverter c = new ObjectToBooleanConverter();

        assertSame(Boolean.TRUE, c.convertValue("foo"));
    }

}