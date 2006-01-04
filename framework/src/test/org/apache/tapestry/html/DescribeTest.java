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
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.describe.HTMLDescriber;

/**
 * Tests for {@link org.apache.tapestry.html.Describe}.
 * 
 * @author Howard M. Lewis Ship
 */
public class DescribeTest extends BaseComponentTestCase
{
    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true);

        replayControls();

        Describe component = (Describe) newInstance(Describe.class);

        component.renderComponent(writer, cycle);

        verifyControls();
    }

    public void testRender()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);

        Object object = new Object();

        HTMLDescriber describer = (HTMLDescriber) newMock(HTMLDescriber.class);

        describer.describeObject(object, writer);

        replayControls();

        Describe component = (Describe) newInstance(Describe.class, new Object[]
        { "object", object, "describer", describer });

        component.renderComponent(writer, cycle);

        verifyControls();
    }
}
