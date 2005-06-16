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

package org.apache.tapestry.form;

import org.apache.tapestry.form.ListEditMap;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.BeanLifecycle;

/**
 * Suite of tests for {@link org.apache.tapestry.form.ListEditMap}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class TestListEditMap extends TapestryTestCase
{

    private ListEditMap create()
    {
        ListEditMap m = new ListEditMap();

        m.add("request", BeanLifecycle.REQUEST);
        m.add("page", BeanLifecycle.PAGE);
        m.add("render", BeanLifecycle.RENDER);

        return m;
    }

    public void testAdd()
    {
        ListEditMap m = create();

        assertEquals("render", m.getKey());

        checkList("keys", new Object[]
        { "request", "page", "render" }, m.getKeys());
        checkList("all values", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE, BeanLifecycle.RENDER }, m.getAllValues());
        checkList("all values", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE, BeanLifecycle.RENDER }, m.getValues());

        assertTrue(m.getDeletedKeys().isEmpty());
    }

    public void testGet()
    {
        ListEditMap m = create();

        m.setKey("page");

        assertEquals("page", m.getKey());
        assertSame(BeanLifecycle.PAGE, m.getValue());
    }

    public void testGetUnknown()
    {
        ListEditMap m = create();

        m.setKey("unknown");

        assertNull(m.getValue());
    }

    public void testMarkDeleted()
    {
        ListEditMap m = create();

        m.setKey("page");

        assertEquals(false, m.isDeleted());

        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList("all values", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE, BeanLifecycle.RENDER }, m.getAllValues());

        checkList("undeleted values", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.RENDER }, m.getValues());

        checkList("deleted keys", new Object[]
        { "page" }, m.getDeletedKeys());
    }

    public void testMarkAlreadyDeleted()
    {
        ListEditMap m = create();

        m.setKey("page");

        assertEquals(false, m.isDeleted());

        m.setDeleted(false);

        assertEquals(false, m.isDeleted());
    }

    public void testMarkMultipleDeleted()
    {
        ListEditMap m = create();

        m.setKey("page");
        m.setDeleted(true);

        m.setKey("render");
        assertEquals(false, m.isDeleted());
        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList("undeleted values", new Object[]
        { BeanLifecycle.REQUEST }, m.getValues());
    }

    public void testDeleteUndelete()
    {
        ListEditMap m = create();

        m.setKey("page");
        m.setDeleted(true);
        m.setDeleted(false);

        m.setKey("render");
        m.setDeleted(true);

        checkList("undeleted values", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE }, m.getValues());
    }

    /** @since 4.0 */

    public void testPurgeDeletedKeys()
    {
        ListEditMap m = create();

        m.setKey("render");
        m.setDeleted(true);

        checkList("deleted keys before purge", new Object[]
        { "render" }, m.getDeletedKeys());

        m.purgeDeletedKeys();

        checkList("all values after purge", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE }, m.getAllValues());
        checkList("keys after purge", new Object[]
        { "request", "page" }, m.getKeys());

        assertTrue(m.getDeletedKeys().isEmpty());

        m.purgeDeletedKeys();

        checkList("all values after second purge", new Object[]
        { BeanLifecycle.REQUEST, BeanLifecycle.PAGE }, m.getAllValues());
        checkList("keys after second purge", new Object[]
        { "request", "page" }, m.getKeys());
    }

}