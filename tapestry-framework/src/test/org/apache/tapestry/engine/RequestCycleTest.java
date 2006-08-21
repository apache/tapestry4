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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import org.apache.hivemind.ErrorHandler;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.pageload.PageSource;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.util.QueryParameterMap;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.RequestCycle}. Mostly just tests changes for 4.0
 * (3.0 code is still mostly tested via the mock integration tests).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class RequestCycleTest extends BaseComponentTestCase
{
    private IEngine newEngine()
    {
        return newMock(IEngine.class);
    }

    private PropertyPersistenceStrategySource newStrategySource()
    {
        return newMock(PropertyPersistenceStrategySource.class);
    }

    private ErrorHandler newErrorHandler()
    {
        return newMock(ErrorHandler.class);
    }

    private AbsoluteURLBuilder newBuilder()
    {
        return newMock(AbsoluteURLBuilder.class);
    }

    private IEngineService newService()
    {
        return newMock(IEngineService.class);
    }

    public void testGetters()
    {
        Infrastructure infrastructure = newMock(Infrastructure.class);
        PageSource pageSource = new PageSource();
        
        expect(infrastructure.getPageSource()).andReturn(pageSource);
        
        IEngineService service = newService();
        ServiceMap map = newServiceMap("fred", service);
        
        expect(infrastructure.getServiceMap()).andReturn(map);
        
        RequestCycleEnvironment env = new RequestCycleEnvironment(newErrorHandler(),
                infrastructure, newStrategySource(), newBuilder());
        
        IEngine engine = newEngine();
        
        replay();
        
        IRequestCycle cycle = new RequestCycle(engine, new QueryParameterMap(), "fred", env);

        assertSame(infrastructure, cycle.getInfrastructure());
        assertSame(service, cycle.getService());
        assertSame(engine, cycle.getEngine());

        verify();
    }

    private ServiceMap newServiceMap(String serviceName, IEngineService service)
    {
        ServiceMap map = newMock(ServiceMap.class);
        checkOrder(map, false);
        
        expect(map.getService(serviceName)).andReturn(service);

        return map;
    }

    public void testForgetPage()
    {
        Infrastructure infrastructure = newMock(Infrastructure.class);
        PageSource pageSource = new PageSource();
        
        expect(infrastructure.getPageSource()).andReturn(pageSource);
        
        PropertyPersistenceStrategySource source = newStrategySource();
        RequestCycleEnvironment env = new RequestCycleEnvironment(newErrorHandler(),
                infrastructure, source, newBuilder());
        IEngine engine = newEngine();

        replay();

        IRequestCycle cycle = new RequestCycle(engine, new QueryParameterMap(), null, env);

        cycle.getEngine();

        verify();
        
        source.discardAllStoredChanged("MyPage");
        
        replay();
        
        cycle.forgetPage("MyPage");
        
        verify();
    }

    public void testSendRedirect()
    {
        IRequestCycle cycle = new RequestCycle();
        
        try
        {
            cycle.sendRedirect("http://foo/bar");
            unreachable();
        }
        catch (RedirectException ex)
        {
            assertEquals("http://foo/bar", ex.getRedirectLocation());
        }
    }
}