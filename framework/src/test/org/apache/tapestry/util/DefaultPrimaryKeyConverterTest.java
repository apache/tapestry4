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

package org.apache.tapestry.util;

import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.util.DefaultPrimaryKeyConverter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DefaultPrimaryKeyConverterTest extends HiveMindTestCase
{
    /**
     * Test the starting values of a number of properties.
     */
    public void testInitialValues()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        assertTrue(cv.getAllValues().isEmpty());
        assertTrue(cv.getValues().isEmpty());
        assertTrue(cv.getDeletedValues().isEmpty());
        assertNull(cv.getLastValue());
    }

    public void testAdd()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");

        assertEquals("flintstone", cv.getLastValue());

        cv.add("barney", "rubble");

        assertEquals("rubble", cv.getLastValue());

        List l = cv.getValues();

        assertEquals("flintstone", l.get(0));
        assertEquals("rubble", l.get(1));
    }

    public void testAddDuplicate()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        try
        {
            cv.add("fred", "macmurray");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Key 'fred' already exists in this primary key converter.", ex
                    .getMessage());
        }
    }

    public void testClear()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");
        cv.setDeleted(true);

        cv.clear();

        assertTrue(cv.getAllValues().isEmpty());
        assertTrue(cv.getValues().isEmpty());
        assertTrue(cv.getDeletedValues().isEmpty());
        assertNull(cv.getLastValue());
    }

    public void testDelete()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        assertEquals("flintstone", cv.getValue("fred"));

        assertEquals(false, cv.isDeleted());

        assertTrue(cv.getDeletedValues().isEmpty());

        assertEquals("flintstone", cv.getLastValue());

        cv.setDeleted(true);

        assertEquals(true, cv.isDeleted());

        assertTrue(cv.getDeletedValues().contains("flintstone"));
        assertFalse(cv.getValues().contains("flintstone"));
        assertTrue(cv.getAllValues().contains("flintstone"));

        cv.setDeleted(false);

        assertFalse(cv.isDeleted());

        assertFalse(cv.getDeletedValues().contains("flintstone"));
        assertTrue(cv.getValues().contains("flintstone"));
    }

    public void testGetPrimaryKey()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        assertEquals("fred", cv.getPrimaryKey("flintstone"));
        assertEquals("flintstone", cv.getLastValue());

        assertEquals("barney", cv.getPrimaryKey("rubble"));
        assertEquals("rubble", cv.getLastValue());
    }

    public void testGetPrimaryKeyNotFound()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        try
        {
            cv.getPrimaryKey("flintstone");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Value flintstone not found.", ex.getMessage());
        }

    }

    public void testGetValue()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        assertEquals("flintstone", cv.getValue("fred"));
        assertEquals("flintstone", cv.getLastValue());

        assertEquals("rubble", cv.getValue("barney"));
        assertEquals("rubble", cv.getLastValue());
    }

    public void testGetValueNotFound()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        assertEquals(null, cv.getValue("unknown"));
    }

    public void testGetValueSubclassOverride()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter()
        {
            public Object provideMissingValue(Object primaryKey)
            {
                assertEquals("fred", primaryKey);

                return "flintstone";
            }
        };

        assertEquals("flintstone", cv.getValue("fred"));
    }

    public void testGetValueSubclassThrowsException()
    {
        final RuntimeException re = new ApplicationRuntimeException("flintstone");

        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter()
        {
            public Object provideMissingValue(Object primaryKey)
            {
                assertEquals("fred", primaryKey);

                throw re;
            }
        };

        try
        {
            cv.getValue("fred");
            unreachable();

        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(re, ex);
        }
    }

    public void testGetDeletedValues()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        assertEquals("fred", cv.getPrimaryKey("flintstone"));

        cv.setDeleted(true);

        assertTrue(cv.getDeletedValues().contains("flintstone"));
    }

    public void testRemoveDeletedValues()
    {
        DefaultPrimaryKeyConverter cv = new DefaultPrimaryKeyConverter();

        cv.add("fred", "flintstone");
        cv.add("barney", "rubble");

        assertEquals("flintstone", cv.getValue("fred"));

        cv.setDeleted(true);

        cv.removeDeletedValues();

        Object[] values = cv.getValues().toArray();
        assertEquals(1, values.length);
        assertEquals("rubble", values[0]);

        assertEquals(cv.getAllValues(), cv.getValues());
    }
}
