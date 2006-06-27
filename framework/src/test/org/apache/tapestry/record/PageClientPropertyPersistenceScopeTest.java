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

package org.apache.tapestry.record;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link org.apache.tapestry.record.PageClientPropertyPersistenceScope}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PageClientPropertyPersistenceScopeTest extends BaseComponentTestCase
{
    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected IPage newPage()
    {
        return (IPage) newMock(IPage.class);
    }

    public void testConstructParameterName()
    {
        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        assertEquals("state:MyPage", scope.constructParameterName("MyPage"));
    }

    public void testIsParameterForScope()
    {
        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        assertEquals(true, scope.isParameterForScope("state:MyPage"));
        assertEquals(false, scope.isParameterForScope("foo"));
        assertEquals(false, scope.isParameterForScope("appstate:Foo"));
    }

    public void testExtractPageName()
    {
        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        assertEquals("MyPage", scope.extractPageName("state:MyPage"));
    }

    public void testShouldEncodeState()
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();

        trainGetPage(cycle, page);
        trainGetPageName(page, "MyPage");

        replay();

        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        scope.setRequestCycle(cycle);

        assertEquals(true, scope.shouldEncodeState(null, "MyPage", null));

        verify();
    }

    public void testShouldEncodeStateDifferentPage()
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();

        trainGetPage(cycle, page);
        trainGetPageName(page, "MyPage");

        replay();

        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        scope.setRequestCycle(cycle);

        assertEquals(false, scope.shouldEncodeState(null, "OtherPage", null));

        verify();
    }

    public void testShouldEncodeStateNoActivePage()
    {
        IRequestCycle cycle = newCycle();

        trainGetPage(cycle, null);

        replay();

        PageClientPropertyPersistenceScope scope = new PageClientPropertyPersistenceScope();

        scope.setRequestCycle(cycle);

        assertEquals(true, scope.shouldEncodeState(null, "MyPage", null));

        verify();
    }

    private void trainGetPage(IRequestCycle cycle, IPage page)
    {
        expect(cycle.getPage()).andReturn(page);
    }
}
