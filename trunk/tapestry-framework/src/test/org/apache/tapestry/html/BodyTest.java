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

import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.testng.annotations.Test;

/**
 * A scattering of tests for the {@link org.apache.tapestry.html.Body} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class BodyTest extends BaseComponentTestCase
{
    public void testGetNoBodyReturnsNull()
    {
        IRequestCycle cycle = newCycle();

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, null);

        replay();

        assertNull(Body.get(cycle));

        verify();
    }

    public void testGetReturnsBody()
    {
        Body body = (Body) newInstance(Body.class);
        IRequestCycle cycle = newCycle();

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, body);

        replay();

        assertSame(body, Body.get(cycle));

        verify();
    }
}
