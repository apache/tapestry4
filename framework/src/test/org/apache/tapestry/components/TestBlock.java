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
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link org.apache.tapestry.components.Block}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestBlock extends BaseComponentTestCase
{
    public void testRender()
    {
        final IComponent invoker = newComponent();

        final Block block = (Block) newInstance(Block.class);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(writer);

        replay();

        IRender body = new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                assertSame(block.getInvoker(), invoker);
                assertSame(block.getInserter(), invoker);
            }
        };

        assertNull(block.getInvoker());

        block.addBody(body);

        block.renderForComponent(writer, cycle, invoker);

        assertNull(block.getInvoker());

        verify();
    }

    public void testGetParameter()
    {
        Object parameterValue = new Object();

        IBinding binding = newBinding(parameterValue);
        IComponent component = newComponent();

        expect(component.getBinding("fred")).andReturn(binding);
        replay();

        Block block = (Block) newInstance(Block.class);

        block.setInvoker(component);

        assertSame(parameterValue, block.getParameter("fred"));

        verify();
    }

}
