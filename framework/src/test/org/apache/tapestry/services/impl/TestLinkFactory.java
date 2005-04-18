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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.engine.encoders.PageServiceEncoder;
import org.apache.tapestry.record.PropertyPersistenceStrategy;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.io.DataSqueezerImpl;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.LinkFactoryImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestLinkFactory extends HiveMindTestCase
{
    private ErrorLog newErrorLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    private WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    private static class NoopEncoder implements ServiceEncoder
    {
        public void decode(ServiceEncoding encoding)
        {
            //
        }

        public void encode(ServiceEncoding encoding)
        {
            //
        }
    }

    private static class MockSource implements PropertyPersistenceStrategySource
    {

        public PropertyPersistenceStrategy getStrategy(String name)
        {
            return null;
        }

        public Collection getAllStoredChanges(String pageName, IRequestCycle cycle)
        {
            return null;
        }

        public void discardAllStoredChanged(String pageName, IRequestCycle cycle)
        {
        }

        public void addParametersForPersistentProperties(ServiceEncoding encoding,
                IRequestCycle cycle)
        {
            encoding.setParameterValue("foo", "bar");
        }

    }

    private ServiceEncoderContribution newContribution(String id, ServiceEncoder encoder)
    {
        ServiceEncoderContribution result = new ServiceEncoderContribution();

        result.setId(id);
        result.setEncoder(encoder);

        return result;
    }

    private IRequestCycle newCycle()
    {
        MockControl enginec = newControl(IEngine.class);
        IEngine engine = (IEngine) enginec.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getEngine();
        cyclec.setReturnValue(engine);

        engine.getOutputEncoding();
        enginec.setReturnValue("utf-8");

        return cycle;
    }

    public void testNoEncoders()
    {
        ErrorLog log = newErrorLog();
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();

        replayControls();

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(Collections.EMPTY_LIST);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, "myservice");

        ILink link = lf.constructLink(cycle, parameters, false);

        verifyControls();

        assertEquals("/context/app?service=myservice", link.getURL());
    }

    public void testStatefulRequest()
    {
        ErrorLog log = newErrorLog();
        WebRequest request = newRequest();
        MockControl enginec = newControl(IEngine.class);
        IEngine engine = (IEngine) enginec.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getEngine();
        cyclec.setReturnValue(engine);

        engine.getOutputEncoding();
        enginec.setReturnValue("utf-8");
        
        cycle.encodeURL("/context/app?foo=bar&service=myservice");
        cyclec.setReturnValue("{encoded}");

        replayControls();

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(Collections.EMPTY_LIST);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);
        lf.setPersistenceStrategySource(new MockSource());

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, "myservice");

        ILink link = lf.constructLink(cycle, parameters, true);

        assertEquals("{encoded}", link.getURL());

        verifyControls();
    }

    public void testNoopEncoders()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();

        replayControls();

        List l = new ArrayList();
        l.add(newContribution("fred", new NoopEncoder()));
        l.add(newContribution("barney", new NoopEncoder()));

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(l);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, "myservice");

        ILink link = lf.constructLink(cycle, parameters, false);

        verifyControls();

        assertEquals("/context/app?service=myservice", link.getURL());
    }

    public void testActiveEncoder()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();

        replayControls();
        PageServiceEncoder e = new PageServiceEncoder();
        e.setServiceName("page");
        e.setExtension("html");

        List l = Collections.singletonList(newContribution("encoder", e));

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(l);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, "page");
        parameters.put(ServiceConstants.PAGE, "Barney");

        ILink link = lf.constructLink(cycle, parameters, false);

        verifyControls();

        assertEquals("/context/Barney.html", link.getURL());
    }

    public void testWithServiceParameters()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();

        replayControls();

        PageServiceEncoder e = new PageServiceEncoder();
        e.setServiceName("external");
        e.setExtension("ext");

        List l = Collections.singletonList(newContribution("encoder", e));

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(l);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setDataSqueezer(new DataSqueezerImpl(new DefaultClassResolver()));
        lf.setRequest(request);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, "external");
        parameters.put(ServiceConstants.PAGE, "Barney");
        parameters.put(ServiceConstants.PARAMETER, new Object[]
        { Boolean.TRUE });

        ILink link = lf.constructLink(cycle, parameters, false);

        verifyControls();

        assertEquals("/context/Barney.ext?sp=T", link.getURL());
    }
}