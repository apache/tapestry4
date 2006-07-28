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

package org.apache.tapestry.junit;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertTrue;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.impl.DefaultResponseBuilder;
import org.apache.tapestry.test.Creator;
import org.testng.annotations.Test;

/**
 * Test a few random things in {@link org.apache.tapestry.AbstractComponent}and
 * {@link  org.apache.tapestry.BaseComponent}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
@Test
public class TestComponent extends BaseComponentTestCase
{
    private static class TestRender implements IRender
    {
        private boolean rendered = false;

        public void render(IMarkupWriter writer, IRequestCycle cycle)
        {
            rendered = true;
        }

    }

    public abstract static class FakeComponent extends BaseComponent
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
     * Test the ability of {@link org.apache.tapestry.BaseComponent#addOuter(IRender)}to add a
     * large number of objects.
     */

    public void testOuter() throws Exception
    {
        IMarkupWriter writer = new NullWriter();
        IRequestCycle cycle = newMock(IRequestCycle.class);
        
        Creator creator = new Creator();
        
        FakeComponent c = (FakeComponent) creator.newInstance(FakeComponent.class);
        
        TestRender[] list = new TestRender[50];
        
        ResponseBuilder builder = 
            new DefaultResponseBuilder(writer);
        
        for (int i = 0; i < list.length; i++)
        {
            list[i] = new TestRender();
            c.addOuterTest(list[i]);
            
            expect(cycle.getResponseBuilder()).andReturn(builder);
        }
        
        replay();
        
        c.testRenderComponent(writer, cycle);
        
        verify();
        
        for (int i = 0; i < list.length; i++)
            assertTrue("Outer object #" + i + " did render.", list[i].rendered);
    }
}