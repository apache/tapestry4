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

package org.apache.tapestry.callback;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link org.apache.tapestry.callback.PageCallback}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPageCallback extends BaseComponentTestCase
{
    public void testByString()
    {
        IRequestCycle cycle = newCycle();

        cycle.activate("Fred");

        replayControls();

        PageCallback callback = new PageCallback("Fred");

        assertEquals("PageCallback[Fred]", callback.toString());

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testByPage()
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage("Barney");

        cycle.activate("Barney");

        replayControls();

        PageCallback callback = new PageCallback(page);

        assertEquals("PageCallback[Barney]", callback.toString());

        callback.performCallback(cycle);

        verifyControls();
    }
}
