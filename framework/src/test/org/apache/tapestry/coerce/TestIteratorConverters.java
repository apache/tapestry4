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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestIteratorConverters extends HiveMindTestCase
{
    public void testObjectToIterator()
    {
        Object o = new Object();
        TypeConverter c = new ObjectToIteratorConverter();

        Iterator i = (Iterator) c.convertValue(o);

        assertTrue(i.hasNext());
        assertSame(o, i.next());
        assertFalse(i.hasNext());
    }

    public void testNullToIterator()
    {
        TypeConverter c = new NullToIteratorConverter();

        Iterator i = (Iterator) c.convertValue("will be null");

        assertFalse(i.hasNext());
    }

    public void testCollectionToIterator()
    {
        Object o = new Object();
        List l = Collections.singletonList(o);
        TypeConverter c = new CollectionToIteratorConverter();

        Iterator i = (Iterator) c.convertValue(l);

        assertTrue(i.hasNext());
        assertSame(o, i.next());
        assertFalse(i.hasNext());
    }

    public void testObjectArrayToIterator()
    {
        Object o = new Object();
        Object[] a = new Object[]
        { o };

        TypeConverter c = new ObjectArrayToIteratorConverter();

        Iterator i = (Iterator) c.convertValue(a);

        assertTrue(i.hasNext());
        assertSame(o, i.next());
        assertFalse(i.hasNext());
    }
}