// Copyright 2004, 2005 The Apache Software Foundation
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
package org.apache.tapestry.event;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests how {@link BrowserEvent} extracts itself from the {@link IRequestCycle}.
 */
@Test
public class BrowserEventTest extends BaseComponentTestCase
{
    public void test_Construct_And_Read_Method_Arguments()
    {
        IRequestCycle cycle = newCycle();
        trainCycleForStandardBrowserEvent(cycle);
        
        expect(cycle.getParameter(BrowserEvent.METHOD_ARGUMENTS)).andReturn("[1,2]");

        replay();

        BrowserEvent event = new BrowserEvent(cycle);

        verify();

        assertEquals(event.getMethodArguments().getInt(0), 1);
        assertEquals(event.getMethodArguments().getInt(1), 2);
    }

    @Test(expectedExceptions = ApplicationRuntimeException.class)
    public void test_Unparseable_JSON_Method_Arguments()
    {
        IRequestCycle cycle = newCycle();
        trainCycleForStandardBrowserEvent(cycle);
        
        expect(cycle.getParameter(BrowserEvent.METHOD_ARGUMENTS)).andReturn("*/utterRubb�sh");

        replay();

        BrowserEvent event = new BrowserEvent(cycle);

        verify();

        event.getMethodArguments();
    }

    private void trainCycleForStandardBrowserEvent(IRequestCycle cycle)
    {
        expect(cycle.getParameter(BrowserEvent.NAME)).andReturn("onClick").anyTimes();

        expect(cycle.getParameter(BrowserEvent.TYPE)).andReturn("click");
        expect(cycle.getParameters(BrowserEvent.KEYS)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.CHAR_CODE)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.PAGE_X)).andReturn("123");
        expect(cycle.getParameter(BrowserEvent.PAGE_Y)).andReturn("1243");
        expect(cycle.getParameter(BrowserEvent.LAYER_X)).andReturn(null);
        expect(cycle.getParameter(BrowserEvent.LAYER_Y)).andReturn(null);

        expect(cycle.getParameter(BrowserEvent.TARGET + "." + BrowserEvent.TARGET_ATTR_ID)).andReturn("element1");
    }
}
