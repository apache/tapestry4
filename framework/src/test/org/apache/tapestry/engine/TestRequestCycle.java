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

import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.pageload.PageSource;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.util.QueryParameterMap;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.RequestCycle}. Mostly just tests changes for 3.1
 * (3.0 code is still mostly tested via the mock integration tests).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestRequestCycle extends HiveMindTestCase
{
    private IEngine newEngine()
    {
        return (IEngine) newMock(IEngine.class);
    }

    private IMonitor newMonitor()
    {
        return (IMonitor) newMock(IMonitor.class);
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
        MockControl control = newControl(Infrastructure.class);
        Infrastructure infrastructure = (Infrastructure) control.getMock();

        infrastructure.getPageSource();
        control.setReturnValue(source);

        return infrastructure;
    }

    private IEngineService newService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    private RequestCycleEnvironment newEnvironment(RequestContext context)
    {
        return new RequestCycleEnvironment(newErrorHandler(), newInfrastructure(), context,
                newStrategySource(), newBuilder());
    }

    public void testGetters()
    {
        RequestContext context = new RequestContext(null, null);
        Infrastructure infrastructure = newInfrastructure();
        RequestCycleEnvironment env = new RequestCycleEnvironment(newErrorHandler(),
                infrastructure, context, newStrategySource(), newBuilder());
        IEngine engine = newEngine();
        IEngineService service = newService();
        IMonitor monitor = newMonitor();

        replayControls();

        IRequestCycle cycle = new RequestCycle(engine, new QueryParameterMap(), service, monitor,
                env);

        assertSame(infrastructure, cycle.getInfrastructure());
        assertSame(context, cycle.getRequestContext());
        assertSame(service, cycle.getService());
        assertSame(engine, cycle.getEngine());
        assertSame(monitor, cycle.getMonitor());

        verifyControls();
    }

    public void testForgetPage()
    {
        RequestContext context = new RequestContext(null, null);
        Infrastructure infrastructure = newInfrastructure();
        PropertyPersistenceStrategySource source = newStrategySource();
        RequestCycleEnvironment env = new RequestCycleEnvironment(newErrorHandler(),
                infrastructure, context, source, newBuilder());
        IEngine engine = newEngine();
        IEngineService service = newService();
        IMonitor monitor = newMonitor();

        replayControls();

        IRequestCycle cycle = new RequestCycle(engine, new QueryParameterMap(), service, monitor,
                env);
        
        cycle.getEngine();
        
        verifyControls();

        source.discardAllStoredChanged("MyPage", cycle);

        replayControls();

        cycle.forgetPage("MyPage");

        verifyControls();
        
        source.discardAllStoredChanged("MyPage", cycle);

        replayControls();

        cycle.discardPage("MyPage");

        verifyControls();
    }
}