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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Base class for test cases of engine service proxies.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class AbstractEngineServiceProxyTestCase extends HiveMindTestCase
{

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected IEngineService newEngineService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    protected void trainGetLink(IEngineService service, boolean post, Object parameter, ILink link)
    {
        service.getLink(post, parameter);
        setReturnValue(service, link);
    }

    protected ILink newLink()
    {
        return (ILink) newMock(ILink.class);
    }

}
