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
package org.apache.tapestry.pageload;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link PageSource} implementation, esp 
 * in how it handles threading semantics.
 */
public class PageSourceTest extends BaseComponentTestCase
{
    
    static final String PAGE_NAME = "TestPage";
    
    PageSource _pageSource;
    
    int _callCount = 0;
    
    @BeforeSuite
    public void create()
    {
        _pageSource = new PageSource();
    }
    
    /**
     * No matter how many threads are used the _callCount 
     * member should always equal the invocationCount after the test
     * is done running.
     * 
     * @throws InterruptedException
     */
    @Test(invocationCount = 80, threadPoolSize = 3)
    public void test_Page_Lock()
    throws InterruptedException
    {
        ReentrantLock lock = _pageSource.getPageLock(PAGE_NAME);
        
        try {
            lock.lockInterruptibly();
            
            _callCount++;
        } finally {
            lock.unlock();
        }
    }
    
    @AfterSuite
    public void check_Counts()
    {
        assertEquals(_callCount, 80);
    }
}
