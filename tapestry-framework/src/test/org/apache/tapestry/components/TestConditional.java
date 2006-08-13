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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.components.Conditional}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestConditional extends BaseComponentTestCase
{
    private IRender newRender(IMarkupWriter writer, IRequestCycle cycle)
    {
        IRender render = newMock(IRender.class);
        
        render.render(writer, cycle);

        return render;
    }

    public void testFalseAndFalse()
    {
        Conditional conditional = newInstance(Conditional.class, new Object[]
        { "condition", Boolean.FALSE, "invert", Boolean.FALSE });

        conditional.renderComponent(null, null);
    }

    public void testTrueAndTrue()
    {
        Conditional conditional = newInstance(Conditional.class, new Object[]
        { "condition", Boolean.TRUE, "invert", Boolean.TRUE });

        conditional.renderComponent(null, null);
    }

    public void testRenderSimple()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, writer);
        IRender body = newRender(writer, cycle);
        
        Conditional conditional = newInstance(Conditional.class, 
                new Object[] { "condition", Boolean.TRUE });
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        conditional.addBody(body);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        replay();

        conditional.render(writer, cycle);

        verify();
    }

    public void testIgnoreElementWhenRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IRender body = newMock(IRender.class);
        
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        
        Conditional conditional = newInstance(Conditional.class, 
                new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(true);
        
        expect(cycle.getResponseBuilder()).andReturn(builder);
        
        builder.render(writer, body, cycle);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        replay();
        
        conditional.addBody(body);
        
        conditional.render(writer, cycle);
        
        verify();
    }

    public void testElement()
    {   
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, null);
        
        IComponentSpecification spec = newSpec("informal", null);
        IBinding informal = newBinding("informal-value");
        
        IRender body = newRender(writer, cycle);
        
        Conditional conditional = newInstance(Conditional.class, 
                new Object[] { 
            "condition", Boolean.TRUE, 
            "element", "div", 
            "specification", spec 
        });
        
        conditional.addBody(body);
        conditional.setBinding("informal", informal);
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        writer.begin("div");
        
        writer.attribute("informal", "informal-value");

        // We've trained body, but there's no way to ensure,
        // using EasyMock, that methods are invoked in the correct
        // order. But sometimes you have to trust the code (
        // and trust that future developers won't break something
        // that obvious!).
        
        trainResponseBuilder(cycle, writer);
        
        writer.end("div");
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        replay();

        conditional.render(writer, cycle);

        verify();
    }
}