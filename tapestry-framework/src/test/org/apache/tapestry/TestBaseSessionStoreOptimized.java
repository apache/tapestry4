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

import static org.testng.AssertJUnit.assertEquals;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.BaseSessionStoreOptimized}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestBaseSessionStoreOptimized extends BaseComponentTestCase
{
    @Test
    public void testMarkDirty()
    {
        BaseSessionStoreOptimized object = new BaseSessionStoreOptimized();

        assertEquals(false, object.isStoreToSessionNeeded());

        object.markSessionStoreNeeded();

        assertEquals(true, object.isStoreToSessionNeeded());
    }

    @Test
    public void testMarkClean()
    {
        HttpSession session = newMock(HttpSession.class);

        BaseSessionStoreOptimized object = new BaseSessionStoreOptimized();

        object.markSessionStoreNeeded();

        replay();

        object.valueBound(new HttpSessionBindingEvent(session, "sessionid", object));

        assertEquals(false, object.isStoreToSessionNeeded());

        verify();
    }

    @Test
    public void testUnboundDoesNothing()
    {
        HttpSession session = newMock(HttpSession.class);

        BaseSessionStoreOptimized object = new BaseSessionStoreOptimized();

        object.markSessionStoreNeeded();

        replay();

        object.valueUnbound(new HttpSessionBindingEvent(session, "sessionid", object));

        assertEquals(true, object.isStoreToSessionNeeded());

        verify();

    }

}
