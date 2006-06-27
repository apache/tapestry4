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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

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
        IExternalPage page = newMock(IExternalPage.class);

        expect(page.getPageName()).andReturn(pageName);

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

        replay();

        ExternalCallback callback = new ExternalCallback("Fred", parameters);

        assertEquals("ExternalCallback[Fred/param1, param2]", callback.toString());

        callback.performCallback(cycle);

        verify();
    }

    public void testByNameNoParameters()
    {
        IExternalPage page = newExternalPage();
        IRequestCycle cycle = newCycleGetPage("Fred", page);

        cycle.activate(page);

        page.activateExternalPage(null, cycle);

        replay();

        ExternalCallback callback = new ExternalCallback("Fred", null);

        assertEquals("ExternalCallback[Fred]", callback.toString());

        callback.performCallback(cycle);

        verify();
    }

    public void testByPage()
    {
        Object[] parameters =
        { "param1", "param2" };

        IExternalPage page = newExternalPage("Barney");
        IRequestCycle cycle = newCycleGetPage("Barney", page);

        cycle.activate(page);

        page.activateExternalPage(parameters, cycle);

        replay();

        ExternalCallback callback = new ExternalCallback(page, parameters);

        assertEquals("ExternalCallback[Barney/param1, param2]", callback.toString());

        callback.performCallback(cycle);

        verify();
    }

    public void testNotExternalPage()
    {
        IPage page = newPage();
        IRequestCycle cycle = newCycleGetPage("Barney", page);

        replay();

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

        verify();
    }
}
