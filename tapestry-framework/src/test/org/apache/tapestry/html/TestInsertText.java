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

import static org.easymock.EasyMock.expect;

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
        IRequestCycle cycle = newCycle(true);
        
        InsertText component = (InsertText) newInstance(InsertText.class);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    public void testRenderNull()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        InsertText component = (InsertText) newInstance(InsertText.class);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    public void testRenderBreaks()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        InsertText component = (InsertText) newInstance(
                InsertText.class,
                "value",
        "Now is the time\nfor all good men\nto come to the aid of their Tapestry.");

        expect(cycle.renderStackPush(component)).andReturn(component);
        
        writer.print("Now is the time", false);
        writer.beginEmpty("br");
        writer.print("for all good men", false);
        writer.beginEmpty("br");
        writer.print("to come to the aid of their Tapestry.", false);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.finishLoad(cycle, null, null);
        component.render(writer, cycle);

        verify();
    }

    public void testRenderParas()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        InsertText component = newInstance(InsertText.class, 
                new Object[] { "mode", InsertTextMode.PARAGRAPH, "value",
        "Now is the time\nfor all good men\nto come to the aid of their Tapestry." });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        writer.begin("p");
        writer.print("Now is the time", false);
        writer.end();

        writer.begin("p");
        writer.print("for all good men", false);
        writer.end();

        writer.begin("p");
        writer.print("to come to the aid of their Tapestry.", false);
        writer.end();
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();
        
        component.render(writer, cycle);

        verify();
    }

    public void testRenderRaw()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        InsertText component = newInstance(InsertText.class, 
                new Object[] { "value", "output\n<b>raw</b>", "raw", Boolean.TRUE });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        writer.print("output", true);
        writer.beginEmpty("br");
        writer.print("<b>raw</b>", true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.finishLoad(cycle, null, null);
        component.render(writer, cycle);

        verify();
    }
    
    public void test_Render_Nested_Raw()
    {
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle(false);
        
        InsertText component = newInstance(InsertText.class, 
                new Object[] { "value", "output\n<b>raw</b>", "raw", Boolean.TRUE });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        IMarkupWriter nested = writer.getNestedWriter();
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.finishLoad(cycle, null, null);
        component.render(nested, cycle);
        
        verify();
        
        nested.close();
        
        assertBuffer("output<br/><b>raw</b>");
    }
}
