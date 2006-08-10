// Copyright Aug 9, 2006 The Apache Software Foundation
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
package org.apache.tapestry.listener;

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link ListenerMethodInvokerImpl}.
 * 
 * @author jkuhnert
 */
@Test
public class ListenerMethodInvokerTest extends BaseComponentTestCase
{

    public void test_BrowserEvent_Parameter()
    {
        IRequestCycle cycle = newCycle();
        
        ListenerMethodHolder target = new ListenerMethodHolder();
        
        ListenerMethodInvoker invoker = 
            new ListenerMethodInvokerImpl("bangbangClicked", target.getClass().getMethods());
        
        BrowserEvent event = new BrowserEvent("onClick", null);
        Object[] parms = { 12, event };
        
        expect(cycle.getListenerParameters()).andReturn(parms);
        
        replay();
        
        invoker.invokeListenerMethod(target, cycle);
        
        verify();
    }
    
    public void test_Type_Conversion_Parameter()
    {
        IRequestCycle cycle = newCycle();
        
        ListenerMethodHolder target = new ListenerMethodHolder();
        
        ListenerMethodInvoker invoker = 
            new ListenerMethodInvokerImpl("bangbangClicked", target.getClass().getDeclaredMethods());
        
        BrowserEvent event = new BrowserEvent("onClick", null);
        Object[] parms = { new Integer(12), event };
        
        expect(cycle.getListenerParameters()).andReturn(parms);
        
        replay();
        
        invoker.invokeListenerMethod(target, cycle);
        
        verify();
    }
}
