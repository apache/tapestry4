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

package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertSame;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.ServiceMap;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineServiceObjectProvider}.
 * 
 * @author Howard M. Lewis Ship
 */
public class TestEngineServiceObjectProvider extends BaseComponentTestCase
{
    public void testProvideObject()
    {
        ServiceMap map = newMock(ServiceMap.class);

        IEngineService service = newMock(IEngineService.class);

        expect(map.getService("page")).andReturn(service);

        replay();

        EngineServiceObjectProvider p = new EngineServiceObjectProvider();

        p.setServiceMap(map);

        assertSame(service, p.provideObject(null, null, "page", null));

        verify();
    }
}