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

package org.apache.tapestry;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.TapestryUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestTapestryUtils extends HiveMindTestCase
{
    private IRequestCycle newCycle(String key, Object attribute)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getAttribute(key);
        control.setReturnValue(attribute);

        return cycle;
    }

    public void testStoreUniqueAttributeSuccess()
    {
        Object newInstance = new Object();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        String key = "foo.bar.Baz";

        cycle.getAttribute(key);
        control.setReturnValue(null);

        cycle.setAttribute(key, newInstance);

        replayControls();

        TapestryUtils.storeUniqueAttribute(cycle, key, newInstance);

        verifyControls();
    }

    public void testStoreUniqueAttributeFailure()
    {
        Object existing = "*EXISTING*";
        Object newInstance = "*NEW*";

        String key = "foo.bar.Baz";

        IRequestCycle cycle = newCycle(key, existing);

        replayControls();

        try
        {
            TapestryUtils.storeUniqueAttribute(cycle, key, newInstance);
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(TapestryMessages.nonUniqueAttribute(newInstance, key, existing), ex
                    .getMessage());
        }

        verifyControls();
    }

    public void testGetPageRenderSupportSuccess()
    {
        PageRenderSupport support = (PageRenderSupport) newMock(PageRenderSupport.class);
        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, support);

        replayControls();

        PageRenderSupport actual = TapestryUtils.getPageRenderSupport(cycle, null);

        assertSame(support, actual);

        verifyControls();
    }

    public void testGetPageRenderSupportFailure()
    {
        Location l = fabricateLocation(22);
        IRequestCycle cycle = newCycle(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, null);

        replayControls();

        try
        {
            TapestryUtils.getPageRenderSupport(cycle, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(TapestryMessages.noPageRenderSupport(), ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

}