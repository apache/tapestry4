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

import static org.easymock.EasyMock.expect;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * Tests how BrowserEvent extracts itself from the RequestCycle
 * 
 */
@Test
public class BrowserEventTest extends BaseComponentTestCase
{
    public void testConstructAndReadMethodArguments() {
        IRequestCycle cycle = newCycle();
        
        trainCycleForStandardBrowserEvent(cycle);

        expect(cycle.getParameter(BrowserEvent.METHOD_ARGUMENTS))
                .andReturn("[1,2]");
        
        replay();
        
        BrowserEvent event = new BrowserEvent(cycle);
        
        verify();
        
        assertEquals(1, event.getMethodArguments().getInt(0));
        assertEquals(2, event.getMethodArguments().getInt(1));
    }
    
    
    
    public void testUnparseableJSON() {
        
        IRequestCycle cycle = newCycle();
        
        trainCycleForStandardBrowserEvent(cycle);
        
        expect(cycle.getParameter(BrowserEvent.METHOD_ARGUMENTS))
                .andReturn("*/utterRubbüsh");
        replay();
        
        BrowserEvent event = new BrowserEvent(cycle);
        
        verify();
        
        try {
            event.getMethodArguments();
            unreachable();
        } catch( ApplicationRuntimeException e) {
            //success.
        }
    }

    /**
     * @param cycle
     */
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
        
        expect(cycle.getParameter(BrowserEvent.TARGET + "." + BrowserEvent.TARGET_ATTR_ID))
        .andReturn("element1");
    }
    
    

}
