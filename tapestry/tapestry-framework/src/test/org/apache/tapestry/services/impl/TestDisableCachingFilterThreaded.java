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
package org.apache.tapestry.services.impl;

import java.io.IOException;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ResetEventHub;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Tests threaded calls on {@link DisableCachingFilter}.
 *
 * @author jkuhnert
 */
@Test(threadPoolSize=4)
public class TestDisableCachingFilterThreaded extends BaseComponentTestCase
{
    class MockServicer implements WebRequestServicer 
    {
        CountingResetListener _listener;
        
        public MockServicer(CountingResetListener listener)
        {
            _listener = listener;
        }
        
        /**
         * {@inheritDoc}
         */
        public void service(WebRequest request, WebResponse response)
            throws IOException
        {
            _listener.performOperation();
        }
        
    }
    
    class CountingResetListener implements ResetEventListener
    {
        private int _counter=0;
        
        public void performOperation()
        {
            _counter++;

            if (_counter != 1)
                throw new AssertionError("Counter should be 1 but is " + _counter);
        }
        
        public void resetEventDidOccur()
        {
            _counter--;
        }
        
        public int getCount()
        {
            return _counter;
        }
    }
    
    private ResetEventHub _resetHub;
    private CountingResetListener _listener;
    
    DisableCachingFilter _filter;
    WebRequestServicer _servicer;
    
    @BeforeClass
    public void setup_Event_Hub()
    {
        _resetHub = new ResetEventHubImpl();
        
        _filter = new DisableCachingFilter();
        _filter.setResetEventHub(_resetHub);
        
        _listener = new CountingResetListener();
        
        _resetHub.addResetEventListener(_listener);
        
        _servicer = new MockServicer(_listener);
    }
    
    @Test(invocationCount = 100, threadPoolSize=4)
    public void invoke_Listener() throws Exception
    {
        _filter.service(null, null, _servicer);
    }
    
    @AfterSuite(alwaysRun=true)
    public void verify_Reset_Counters()
    {
        assertEquals(_listener.getCount(), 0);
    }
}
