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

package org.apache.tapestry.components;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.text.DateFormat;
import java.text.Format;
import java.util.Date;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.components.Insert}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInsert extends BaseComponentTestCase
{
    /**
     * Returns a new page instance (not a mock page).
     */

    private IPage newBasePage(String name)
    {
        BasePage page = (BasePage) newInstance(BasePage.class);

        page.setPageName(name);

        return page;
    }

    public void testRewinding()
    {
        IRequestCycle cycle = newCycle(true, false);

        replay();

        Insert insert = (Insert) newInstance(Insert.class);

        insert.render(null, cycle);

        verify();
    }

    public void testNullValue()
    {
        IRequestCycle cycle = newCycle(false, false);

        replay();

        Insert insert = newInstance(Insert.class, new Object[]
        { "value", null });

        insert.render(null, cycle);

        verify();
    }

    public void testNoFormat()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);
        
        writer.print("42", false);
        
        replay();
        
        Insert insert = newInstance(Insert.class, new Object[]
        { "value", new Integer(42) });
        
        insert.render(writer, cycle);

        verify();
    }

    public void testFormat()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);

        Date date = new Date();
        DateFormat format = DateFormat.getDateInstance();
        String expected = format.format(date);
        
        writer.print(expected, false);
        
        replay();
        
        Insert insert = newInstance(Insert.class, new Object[]
        { "value", date, "format", format });

        insert.render(writer, cycle);

        verify();
    }

    public void testUnableToFormat()
    {
        Object value = "xyzzyx";
        Location l = fabricateLocation(87);
        IBinding binding = newBinding(l);
        
        Format format = DateFormat.getInstance();
        
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        
        IPage page = newBasePage("Flintstone");
        
        replay();
        
        Insert insert = newInstance(Insert.class, new Object[]
        { "value", value, "format", format });
        insert.setBinding("format", binding);
        insert.setId("fred");
        insert.setPage(page);
        insert.setContainer(page);

        try
        {
            insert.render(writer, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Flintstone/fred unable to format value 'xyzzyx': Cannot format given Object as a Date",
                    ex.getMessage());
            assertSame(insert, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testRaw()
    {
        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();

        writer.print("42", true);

        replay();

        Insert insert = newInstance(Insert.class, new Object[]
        { "value", new Integer(42), "raw", Boolean.TRUE });

        insert.render(writer, cycle);

        verify();
    }

    public void testStyleClass()
    {
        IBinding informal = newBinding("informal-value");

        IRequestCycle cycle = newCycle(false, false);
        IMarkupWriter writer = newWriter();
        IComponentSpecification spec = newSpec("informal", null);

        writer.begin("span");
        writer.attribute("class", "paisley");
        writer.attribute("informal", "informal-value");
        writer.print("42", false);
        writer.end();

        replay();

        Insert insert = newInstance(Insert.class, new Object[]
        { "value", "42", "specification", spec, "styleClass", "paisley" });

        insert.setBinding("informal", informal);

        insert.render(writer, cycle);

        verify();
    }
}