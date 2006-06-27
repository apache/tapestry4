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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.pageload.PageSource;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Tests for {@link org.apache.tapestry.engine.RequestCycle}. Mostly just tests changes for 4.0
 * (3.0 code is still mostly tested via the mock integration tests).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RequestCycleTest extends HiveMindTestCase
{
    private IEngine newEngine()
    {
        return (IEngine) newMock(IEngine.class);
    }

    private PropertyPersistenceStrategySource newStrategySource()
    {
        return (PropertyPersistenceStrategySource) newMock(PropertyPersistenceStrategySource.class);
    }

    private PageSource newPageSource()
    {
        return (PageSource) newMock(PageSource.class);
    }

    private Infrastructure newInfrastructure()
    {
        return newInfrastructure(newPageSource());
    }

    private ErrorHandler newErrorHandler()
    {
        return (ErrorHandler) newMock(ErrorHandler.class);
    }

    private AbsoluteURLBuilder newBuilder()
    {
        return (AbsoluteURLBuilder) newMock(AbsoluteURLBuilder.class);
    }

    private Infrastructure newInfrastructure(PageSource source)
    {
        Infrastructure infrastructure = (Infrastructure) newMock(Infrastructure.class);

        expect(infrastructure.getPageSource()).andReturn(source);
        
        return infrastructure;
    }

    private IEngineService newService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    public void testGetters()
    {
        IEngineService service = newService();
        ServiceMap map = newServiceMap("fred", service);

        Infrastructure infrastructure = newInfrastructure(newPageSource());

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
        ServiceMap map = (ServiceMap) newMock(ServiceMap.class);

        expect(map.getService(serviceName)).andReturn(service);

        return map;
    }

    public void testForgetPage()
    {
        Infrastructure infrastructure = newInfrastructure();
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