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

/**
 * Tests for {@link org.apache.tapestry.describe.RenderableAdapterFactoryImpl},
 * {@link org.apache.tapestry.describe.RenderBridge}and
 * {@link org.apache.tapestry.describe.DefaultRenderableAdapter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestRenderableAdapter extends BaseDescribeTestCase
{
    private RenderableAdapter newAdapter()
    {
        return (RenderableAdapter) newMock(RenderableAdapter.class);
    }

    public void testRenderBridge()
    {
        IMarkupWriter writer = newWriter();
        RenderableAdapter adapter = newAdapter();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        adapter.renderObject(object, writer, cycle);

        replayControls();

        new RenderBridge(object, adapter).render(writer, cycle);

        verifyControls();
    }

    public void testRenderableAdapterFactory()
    {
        IMarkupWriter writer = newWriter();
        RenderableAdapter adapter = newAdapter();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        adapter.renderObject(object, writer, cycle);

        replayControls();

        RenderableAdapterFactoryImpl factory = new RenderableAdapterFactoryImpl();
        factory.setAdapter(adapter);

        IRender renderable = factory.getRenderableAdaptor(object);

        renderable.render(writer, cycle);

        verifyControls();
    }

    public void testDefaultRenderableAdapter()
    {
        IMarkupWriter writer = newWriter();
        HTMLDescriber describer = newDescriber();
        IRequestCycle cycle = newCycle();

        Object object = new Object();

        describer.describeObject(object, writer);

        replayControls();

        DefaultRenderableAdapter adapter = new DefaultRenderableAdapter();
        adapter.setDescriber(describer);

        adapter.renderObject(object, writer, cycle);

        verifyControls();
    }
}