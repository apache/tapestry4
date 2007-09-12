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
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.callback.PageCallback}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPageCallback extends BaseComponentTestCase
{
    public void testByString()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);

        cycle.activate("Fred");

        replay();

        PageCallback callback = new PageCallback("Fred");

        assertEquals("PageCallback[Fred]", callback.toString());

        callback.performCallback(cycle);

        verify();
    }

    public void testByPage()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IPage page = newPage("Barney");

        cycle.activate("Barney");

        replay();

        PageCallback callback = new PageCallback(page);

        assertEquals("PageCallback[Barney]", callback.toString());

        callback.performCallback(cycle);

        verify();
    }
}
