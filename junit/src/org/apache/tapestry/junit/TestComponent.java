//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;

/**
 *  Test a few random things in {@link org.apache.tapestry.AbstractComponent}
 *  and {@link  org.apache.tapestry.BaseComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestComponent extends TapestryTestCase
{
    private static class TestRender implements IRender
    {
        private boolean rendered = false;

        public void render(IMarkupWriter writer, IRequestCycle cycle)
        {
            rendered = true;
        }

    }

    private static class FakeComponent extends BaseComponent
    {
        void addOuterTest(IRender render)
        {
            addOuter(render);
        }
        
        void testRenderComponent(IMarkupWriter write, IRequestCycle cycle)
        {
            renderComponent(write, cycle);
        }
    }

    /** 
     *  Test the ability of {@link org.apache.tapestry.BaseComponent#addOuter(IRender)}
     *  to add a large number of objects.
     * 
     **/

    public void testOuter() throws Exception
    {
        FakeComponent c = new FakeComponent();

        TestRender[] list = new TestRender[50];

        for (int i = 0; i < list.length; i++)
        {
            list[i] = new TestRender();
            c.addOuterTest(list[i]);
        }

        IMarkupWriter writer = new NullWriter();

        c.testRenderComponent(writer, null);

        for (int i = 0; i < list.length; i++)
            assertTrue("Outer object #" + i + " did render.", list[i].rendered);
    }
}
