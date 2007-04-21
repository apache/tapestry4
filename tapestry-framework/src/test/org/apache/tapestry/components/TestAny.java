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

import org.apache.tapestry.*;
import org.apache.tapestry.spec.ComponentSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.components.Any} component.
 * 
 */
@Test
public class TestAny extends BaseComponentTestCase
{

    public void testRender()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, writer);
        
        Any any = newInstance(Any.class, new Object[] { "templateTagName", "span" });
        
        expect(cycle.renderStackPush(any)).andReturn(any);
        
        writer.begin("span");
        
        IRender body = newRender();
        body.render(writer, cycle);

        writer.end();
        
        expect(cycle.renderStackPop()).andReturn(any);
        
        replay();
        
        any.addBody(body);

        any.render(writer, cycle);

        verify();
    }

    public void testRenderWithInformalParameters()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, writer);
        IRender body = newRender();
        IBinding binding = newBinding("fred");
        
        Any any = newInstance(Any.class, "templateTagName", "span", "specification", new ComponentSpecification());
        
        expect(cycle.renderStackPush(any)).andReturn(any);
        
        writer.begin("span");
        writer.attribute("class", "fred");

        body.render(writer, cycle);

        writer.end();
        
        expect(cycle.renderStackPop()).andReturn(any);
        
        replay();

        any.addBody(body);
        any.setBinding("class", binding);

        any.render(writer, cycle);

        verify();
    }

    public void testRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true, writer);
        IRender body = newRender();
        
        Any any = newInstance(Any.class, new Object[] { "templateTagName", "span" });
        
        expect(cycle.renderStackPush(any)).andReturn(any);
        
        body.render(writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(any);
        
        replay();

        any.addBody(body);

        any.render(writer, cycle);

        verify();
    }
}
