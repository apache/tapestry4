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

package org.apache.tapestry.engine;

import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.web.WebRequest;

/**
 * Tests for code inside {@link org.apache.tapestry.engine.EngineUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EngineUtilsTest extends ServiceTestCase
{

    public void testNeedAbsoluteURLForScheme()
    {
        WebRequest request = newRequest();

        trainGetScheme(request, "http");

        replay();

        assertEquals(true, EngineUtils.needAbsoluteURL("https", null, 0, request));

        verify();
    }

    public void testNeedAbsoluteURLForServer()
    {
        WebRequest request = newRequest();

        trainGetServerName(request, "someserver.net");

        replay();

        assertEquals(true, EngineUtils.needAbsoluteURL(null, "myserver.com", 0, request));

        verify();
    }

    public void testNeedAbsoluteURLForServerPort()
    {
        WebRequest request = newRequest();

        trainGetServerPort(request, 80);

        replay();

        assertEquals(true, EngineUtils.needAbsoluteURL(null, null, 8197, request));

        verify();
    }

    public void testDontNeedAbsoluteURL()
    {
        WebRequest request = newRequest();

        replay();

        assertEquals(false, EngineUtils.needAbsoluteURL(null, null, 0, request));

        verify();

        trainGetScheme(request, "http");

        trainGetServerName(request, "myserver.com");

        trainGetServerPort(request, 80);

        replay();

        assertEquals(false, EngineUtils.needAbsoluteURL("http", "myserver.com", 80, request));

        verify();
    }
}
