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

package org.apache.tapestry.describe;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link ObjectArrayRenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ObjectArrayRenderStrategyTest extends BaseDescribeTestCase
{
    public void testEmpty()
    {
        Object[] array = new Object[0];

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        writer.begin("em");
        writer.print("empty list");
        writer.end();

        replayControls();

        new ObjectArrayRenderStrategy().renderObject(array, writer, cycle);

        verifyControls();
    }

    public void testNonEmpty()
    {
        Object o1 = new Object();
        Object o2 = new Object();

        Object array = new Object[]
        { o1, o2 };

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        RenderStrategy strategy = (RenderStrategy) newMock(RenderStrategy.class);

        // Alas; this doesn't *prove* that the code executes
        // in the right order, just that the messages for
        // each mock execute in the right order. We'll
        // trust that and the code and hope for the best.

        writer.begin("ul");
        writer.begin("li");

        strategy.renderObject(o1, writer, cycle);

        writer.end();
        writer.begin("li");

        strategy.renderObject(o2, writer, cycle);

        writer.end();
        writer.end();

        replayControls();

        ObjectArrayRenderStrategy subject = new ObjectArrayRenderStrategy();

        subject.setRenderStrategy(strategy);

        subject.renderObject(array, writer, cycle);

        verifyControls();
    }

    public void testNullInArray()
    {
        Object[] array =
        { null };

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        writer.begin("ul");
        writer.begin("li");
        writer.begin("em");
        writer.print("<NULL>");
        writer.end();
        writer.end();
        writer.end();

        replayControls();

        new ObjectArrayRenderStrategy().renderObject(array, writer, cycle);

        verifyControls();
    }
}
