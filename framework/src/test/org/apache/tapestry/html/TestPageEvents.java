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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPageEvents extends HiveMindTestCase
{
    private IPage newPage()
    {
        Creator creator = new Creator();

        return (IPage) creator.newInstance(BasePage.class);
    }

    public void testPageDetached()
    {
        IPage page = newPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.detach();

        page.addPageDetachListener(l);

        page.detach();

        assertEquals("pageDetached", l.getMethod());

        l.reset();

        page.removePageDetachListener(l);

        page.detach();

        assertNull(l.getMethod());
    }

    // Cookie-cutter for the remaining listener interfaces

    public void testPageAttached()
    {
        IPage page = newPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.attach(null, null);

        page.addPageAttachListener(l);

        page.attach(null, null);

        assertEquals("pageAttached", l.getMethod());

        l.reset();

        page.removePageAttachListener(l);

        page.attach(null, null);

        assertNull(l.getMethod());
    }

    public void testPageBeginRender()
    {
        IPage page = newPage();
        ListenerFixture l = new ListenerFixture();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        // Code path: no listener list

        page.renderPage(null, cycle);

        verifyControls();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        page.addPageBeginRenderListener(l);

        page.renderPage(null, cycle);

        assertEquals("pageBeginRender", l.getMethod());

        l.reset();

        page.removePageBeginRenderListener(l);

        verifyControls();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        page.renderPage(null, cycle);

        assertNull(l.getMethod());

        verifyControls();
    }

    public void testPageEndRender()
    {
        IPage page = newPage();
        ListenerFixture l = new ListenerFixture();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        // Code path: no listener list

        page.renderPage(null, cycle);

        verifyControls();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        page.addPageEndRenderListener(l);

        page.renderPage(null, cycle);

        assertEquals("pageEndRender", l.getMethod());

        l.reset();

        page.removePageEndRenderListener(l);

        verifyControls();

        cycle.isRewinding();
        control.setReturnValue(true);

        replayControls();

        page.renderPage(null, cycle);

        assertNull(l.getMethod());

        verifyControls();
    }

    public void testPageValidate()
    {
        IPage page = newPage();
        ListenerFixture l = new ListenerFixture();

        // Code path: no listener list

        page.validate(null);

        page.addPageValidateListener(l);

        page.validate(null);

        assertEquals("pageValidate", l.getMethod());

        l.reset();

        page.removePageValidateListener(l);

        page.validate(null);

        assertNull(l.getMethod());
    }

}