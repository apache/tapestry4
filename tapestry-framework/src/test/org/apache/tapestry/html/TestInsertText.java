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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.html.InsertText}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInsertText extends BaseComponentTestCase
{
    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true, false);

        replay();

        InsertText component = (InsertText) newInstance(InsertText.class);

        component.render(writer, cycle);

        verify();
    }

    public void testRenderNull()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);

        replay();

        InsertText component = (InsertText) newInstance(InsertText.class);

        component.render(writer, cycle);

        verify();
    }

    public void testRenderBreaks()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);

        writer.print("Now is the time", false);
        writer.beginEmpty("br");
        writer.print("for all good men", false);
        writer.beginEmpty("br");
        writer.print("to come to the aid of their Tapestry.", false);

        replay();

        InsertText component = (InsertText) newInstance(
                InsertText.class,
                "value",
                "Now is the time\nfor all good men\nto come to the aid of their Tapestry.");

        component.finishLoad(cycle, null, null);
        component.render(writer, cycle);

        verify();
    }

    public void testRenderParas()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);

        writer.begin("p");
        writer.print("Now is the time", false);
        writer.end();

        writer.begin("p");
        writer.print("for all good men", false);
        writer.end();

        writer.begin("p");
        writer.print("to come to the aid of their Tapestry.", false);
        writer.end();

        replay();

        InsertText component = newInstance(InsertText.class, new Object[]
        { "mode", InsertTextMode.PARAGRAPH, "value",
                "Now is the time\nfor all good men\nto come to the aid of their Tapestry." });

        component.render(writer, cycle);

        verify();
    }

    public void testRenderRaw()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, false);

        writer.print("output", true);
        writer.beginEmpty("br");
        writer.print("<b>raw</b>", true);

        replay();

        InsertText component = newInstance(InsertText.class, new Object[]
        { "value", "output\n<b>raw</b>", "raw", Boolean.TRUE });

        component.finishLoad(cycle, null, null);
        component.render(writer, cycle);

        verify();
    }
}
