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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.components.Any} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAny extends BaseComponentTestCase
{

    public void testElementNull()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newMock(IRequestCycle.class);
        Location l = newLocation();
        
        replay();
        
        Any any = newInstance(Any.class, new Object[] { "location", l });

        try
        {
            any.render(writer, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(ComponentMessages.anyElementNotDefined(), ex
                    .getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testRender()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false, writer);
        
        writer.begin("span");
        
        IRender body = newRender();
        body.render(writer, cycle);

        writer.end("span");

        replay();

        Any any = newInstance(Any.class,
                new Object[] { "element", "span" });

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

        writer.begin("span");
        writer.attribute("class", "fred");

        body.render(writer, cycle);

        writer.end("span");

        replay();

        Any any = newInstance(Any.class, new Object[] { "element",
                "span", "specification", new ComponentSpecification() });

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

        body.render(writer, cycle);

        replay();

        Any any = newInstance(Any.class,
                new Object[] { "element", "span" });

        any.addBody(body);

        any.render(writer, cycle);

        verify();
    }
}
