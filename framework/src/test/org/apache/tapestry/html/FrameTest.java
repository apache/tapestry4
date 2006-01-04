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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.spec.ComponentSpecification;

/**
 * Tests for the {@link org.apache.tapestry.html.Frame} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class FrameTest extends BaseComponentTestCase
{

    public void testRender()
    {
        IEngineService pageService = newEngineService();
        ILink link = newLink();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        trainGetLink(pageService, cycle, false, "FramePage", link);
        trainGetURL(link, "<LinkURL>");

        writer.beginEmpty("frame");
        writer.attribute("src", "<LinkURL>");

        writer.closeTag();

        replayControls();

        Frame frame = (Frame) newInstance(Frame.class, new Object[]
        { "pageService", pageService, "targetPage", "FramePage" });

        frame.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRenderWithInformal()
    {
        IBinding binding = newBinding("informal");
        IEngineService pageService = newEngineService();
        ILink link = newLink();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        trainGetLink(pageService, cycle, false, "FramePage", link);
        trainGetURL(link, "<LinkURL>");

        writer.beginEmpty("frame");
        writer.attribute("src", "<LinkURL>");
        writer.attribute("class", "informal");

        writer.closeTag();

        replayControls();

        Frame frame = (Frame) newInstance(Frame.class, new Object[]
        { "pageService", pageService, "targetPage", "FramePage", "specification",
                new ComponentSpecification() });
        frame.setBinding("class", binding);

        frame.renderComponent(writer, cycle);

        verifyControls();
    }
}
