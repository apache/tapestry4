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

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.components.Conditional}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestConditional extends BaseComponentTestCase
{
    private IRender newRender(IMarkupWriter writer, IRequestCycle cycle)
    {
        IRender render = (IRender) newMock(IRender.class);

        render.render(writer, cycle);

        return render;
    }

    public void testFalseAndFalse()
    {
        Conditional conditional = (Conditional) newInstance(Conditional.class, new Object[]
        { "condition", Boolean.FALSE, "invert", Boolean.FALSE });

        conditional.renderComponent(null, null);
    }

    public void testTrueAndTrue()
    {
        Conditional conditional = (Conditional) newInstance(Conditional.class, new Object[]
        { "condition", Boolean.TRUE, "invert", Boolean.TRUE });

        conditional.renderComponent(null, null);
    }

    public void testRenderSimple()
    {
        IRequestCycle cycle = newRequestCycle(false);
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);

        replayControls();

        Conditional conditional = (Conditional) newInstance(Conditional.class, new Object[]
        { "condition", Boolean.TRUE });
        conditional.addBody(body);

        conditional.render(writer, cycle);

        verifyControls();
    }

    public void testIgnoreElementWhenRewinding()
    {
        IRequestCycle cycle = newRequestCycle(true);
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);

        replayControls();

        Conditional conditional = (Conditional) newInstance(Conditional.class, new Object[]
        { "condition", Boolean.TRUE, "element", "div" });
        conditional.addBody(body);

        conditional.render(writer, cycle);

        verifyControls();
    }

    public void testElement()
    {
        IBinding informal = newBinding("informal-value");
        IComponentSpecification spec = newSpec("informal", null);

        IRequestCycle cycle = newRequestCycle(false);
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);

        writer.begin("div");
        writer.attribute("informal", "informal-value");

        // We've trained body, but there's no way to ensure,
        // using EasyMock, that methods are invoked in the correct
        // order. But sometimes you have to trust the code (
        // and trust that future developers won't break something
        // that obvious!).

        writer.end("div");

        replayControls();

        Conditional conditional = (Conditional) newInstance(Conditional.class, new Object[]
        { "condition", Boolean.TRUE, "element", "div", "specification", spec });
        conditional.addBody(body);
        conditional.setBinding("informal", informal);

        conditional.render(writer, cycle);

        verifyControls();
    }
}