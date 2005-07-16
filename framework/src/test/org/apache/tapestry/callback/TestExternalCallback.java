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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.BaseComponentTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.callback.ExternalCallback}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestExternalCallback extends BaseComponentTestCase
{
    private IExternalPage newExternalPage()
    {
        return (IExternalPage) newMock(IExternalPage.class);
    }

    private IExternalPage newExternalPage(String pageName)
    {
        MockControl control = newControl(IExternalPage.class);
        IExternalPage page = (IExternalPage) control.getMock();

        page.getPageName();
        control.setReturnValue(pageName);

        return page;
    }

    public void testByName()
    {
        Object[] parameters =
        { "param1", "param2" };

        IExternalPage page = newExternalPage();
        IRequestCycle cycle = newCycleGetPage("Fred", page);

        cycle.activate(page);

        page.activateExternalPage(parameters, cycle);

        replayControls();

        ExternalCallback callback = new ExternalCallback("Fred", parameters);

        assertEquals("ExternalCallback[Fred/param1, param2]", callback.toString());

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testByNameNoParameters()
    {
        IExternalPage page = newExternalPage();
        IRequestCycle cycle = newCycleGetPage("Fred", page);

        cycle.activate(page);

        page.activateExternalPage(null, cycle);

        replayControls();

        ExternalCallback callback = new ExternalCallback("Fred", null);

        assertEquals("ExternalCallback[Fred]", callback.toString());

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testByPage()
    {
        Object[] parameters =
        { "param1", "param2" };

        IExternalPage page = newExternalPage("Barney");
        IRequestCycle cycle = newCycleGetPage("Barney", page);

        cycle.activate(page);

        page.activateExternalPage(parameters, cycle);

        replayControls();

        ExternalCallback callback = new ExternalCallback(page, parameters);

        assertEquals("ExternalCallback[Barney/param1, param2]", callback.toString());

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testNotExternalPage()
    {
        IPage page = newPage();
        IRequestCycle cycle = newCycleGetPage("Barney", page);

        replayControls();

        ExternalCallback callback = new ExternalCallback("Barney", null);

        try
        {
            callback.performCallback(cycle);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Page Barney does not implement the IExternalPage interface, and may not be used with the external service.",
                    ex.getMessage());
        }

        verifyControls();
    }
}
