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

package org.apache.tapestry.components;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.components.RenderBlock} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestRenderBlock extends BaseComponentTestCase
{
    public void testNullBlock()
    {
        RenderBlock rb = (RenderBlock) newInstance(RenderBlock.class);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        replayControls();

        rb.render(writer, cycle);

        verifyControls();
    }

    public void testNonNullBlock()
    {
        MockControl bc = newControl(Block.class);
        Block b = (Block) bc.getMock();

        RenderBlock rb = (RenderBlock) newInstance(RenderBlock.class, new Object[]
        { "block", b });

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        b.renderForComponent(writer, cycle, rb);

        replayControls();

        rb.render(writer, cycle);

        verifyControls();
    }
}