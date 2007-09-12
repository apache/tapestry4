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
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.describe.RenderableAdapterFactoryImpl},
 * {@link org.apache.tapestry.describe.RenderBridge}and
 * {@link org.apache.tapestry.describe.DefaultRenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestRenderStrategy extends BaseDescribeTestCase
{
    private RenderStrategy newStrategy()
    {
        return newMock(RenderStrategy.class);
    }

    public void testRenderBridge()
    {
        IMarkupWriter writer = newWriter();
        RenderStrategy strategy = newStrategy();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        strategy.renderObject(object, writer, cycle);

        replay();

        new RenderBridge(object, strategy).render(writer, cycle);

        verify();
    }

    public void testRenderableAdapterFactory()
    {
        IMarkupWriter writer = newWriter();
        RenderStrategy strategy = newStrategy();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        strategy.renderObject(object, writer, cycle);

        replay();

        RenderableAdapterFactoryImpl factory = new RenderableAdapterFactoryImpl();
        factory.setStrategy(strategy);

        IRender renderable = factory.getRenderableAdaptor(object);

        renderable.render(writer, cycle);

        verify();
    }

    public void testDefaultRenderableAdapter()
    {
        IMarkupWriter writer = newWriter();
        HTMLDescriber describer = newDescriber();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        describer.describeObject(object, writer);

        replay();

        DefaultRenderStrategy strategy = new DefaultRenderStrategy();
        strategy.setDescriber(describer);

        strategy.renderObject(object, writer, cycle);

        verify();
    }
}