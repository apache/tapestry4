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
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link PreformattedRenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 */
public class PreformattedRenderableStrategyTest extends BaseDescribeTestCase
{
    public void testRenderObject()
    {
        String object = "some verbose object";

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        writer.begin("pre");
        writer.print(object);
        writer.end();

        replayControls();

        new PreformattedRenderStrategy().renderObject(object, writer, cycle);

        verifyControls();
    }
}
