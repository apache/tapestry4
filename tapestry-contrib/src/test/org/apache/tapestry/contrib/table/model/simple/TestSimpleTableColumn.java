package org.apache.tapestry.contrib.table.model.simple;

import org.apache.tapestry.TestBase;
import org.testng.annotations.Test;

@Test
public class TestSimpleTableColumn extends TestBase
{
    public void test_display_column_names()
    {
        SimpleTableColumn column = new SimpleTableColumn("a.b_c");
        assertEquals(column.getDisplayName(), "a.b.c");
        assertEquals(column.getColumnName(), "a_b_c");

    }
}
