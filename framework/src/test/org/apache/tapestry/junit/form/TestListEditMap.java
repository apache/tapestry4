// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.form;

import org.apache.tapestry.form.ListEditMap;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.AssetType;

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

        m.add("external", AssetType.EXTERNAL);
        m.add("private", AssetType.PRIVATE);
        m.add("context", AssetType.CONTEXT);

        return m;
    }

    public void testAdd()
    {
        ListEditMap m = create();

        assertEquals("context", m.getKey());

        checkList("keys", new Object[]
        { "external", "private", "context" }, m.getKeys());
        checkList("all values", new Object[]
        { AssetType.EXTERNAL, AssetType.PRIVATE, AssetType.CONTEXT }, m.getAllValues());
        checkList("all values", new Object[]
        { AssetType.EXTERNAL, AssetType.PRIVATE, AssetType.CONTEXT }, m.getValues());

        assertNull(m.getDeletedKeys());
    }

    public void testGet()
    {
        ListEditMap m = create();

        m.setKey("private");

        assertEquals("private", m.getKey());
        assertSame(AssetType.PRIVATE, m.getValue());
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

        m.setKey("private");

        assertEquals(false, m.isDeleted());

        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList("all values", new Object[]
        { AssetType.EXTERNAL, AssetType.PRIVATE, AssetType.CONTEXT }, m.getAllValues());

        checkList("undeleted values", new Object[]
        { AssetType.EXTERNAL, AssetType.CONTEXT }, m.getValues());

        checkList("deleted keys", new Object[]
        { "private" }, m.getDeletedKeys());
    }

    public void testMarkAlreadyDeleted()
    {

        ListEditMap m = create();

        m.setKey("private");

        assertEquals(false, m.isDeleted());

        m.setDeleted(false);

        assertEquals(false, m.isDeleted());
    }

    public void testMarkMultipleDeleted()
    {
        ListEditMap m = create();

        m.setKey("private");
        m.setDeleted(true);

        m.setKey("context");
        assertEquals(false, m.isDeleted());
        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList("undeleted values", new Object[]
        { AssetType.EXTERNAL }, m.getValues());
    }

    public void testDeleteUndelete()
    {
        ListEditMap m = create();

        m.setKey("private");
        m.setDeleted(true);
        m.setDeleted(false);

        m.setKey("context");
        m.setDeleted(true);

        checkList("undeleted values", new Object[]
        { AssetType.EXTERNAL, AssetType.PRIVATE }, m.getValues());
    }

}