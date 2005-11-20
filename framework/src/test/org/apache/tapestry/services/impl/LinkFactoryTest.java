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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.engine.encoders.PageServiceEncoder;
import org.apache.tapestry.record.PropertyPersistenceStrategy;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.io.DataSqueezerUtil;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.LinkFactoryImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class LinkFactoryTest extends HiveMindTestCase
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

        public Collection getAllStoredChanges(String pageName)
        {
            return null;
        }

        public void discardAllStoredChanged(String pageName)
        {
        }

        public void addParametersForPersistentProperties(ServiceEncoding encoding, boolean post)
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

    private IEngine newEngine()
    {
        return (IEngine) newMock(IEngine.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private void trainGetOutputEncoding(IEngine engine, String outputEncoding)
    {
        engine.getOutputEncoding();
        setReturnValue(engine, outputEncoding);
    }

    private void trainGetEngine(IRequestCycle cycle, IEngine engine)
    {
        cycle.getEngine();
        setReturnValue(cycle, engine);
    }

    public void testNoEncoders()
    {
        ErrorLog log = newErrorLog();
        WebRequest request = newRequest();
        IEngine engine = newEngine();
        IRequestCycle cycle = newCycle();
        IEngineService service = newService("myservice");

        trainGetEngine(cycle, engine);
        trainGetOutputEncoding(engine, "utf-8");

        replayControls();

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(Collections.EMPTY_LIST);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);
        lf.setRequestCycle(cycle);

        lf.initializeService();

        Map parameters = new HashMap();

        ILink link = lf.constructLink(service, false, parameters, false);

        verifyControls();

        assertEquals("/context/app?service=myservice", link.getURL());
    }

    private IEngineService newService(String name)
    {
        IEngineService service = (IEngineService) newMock(IEngineService.class);

        service.getName();
        setReturnValue(service, name);

        return service;
    }

    public void testStatefulRequest()
    {
        ErrorLog log = newErrorLog();
        WebRequest request = newRequest();
        IEngine engine = newEngine();
        IEngineService service = newService("myservice");
        IRequestCycle cycle = newCycle();

        trainGetEngine(cycle, engine);
        trainGetOutputEncoding(engine, "utf-8");

        trainEncodeURL(cycle, "/context/app?foo=bar&service=myservice", "{encoded}");

        replayControls();

        LinkFactoryImpl lf = new LinkFactoryImpl();

        lf.setContributions(Collections.EMPTY_LIST);
        lf.setErrorLog(log);
        lf.setContextPath("/context");
        lf.setServletPath("/app");
        lf.setRequest(request);
        lf.setPersistenceStrategySource(new MockSource());
        lf.setRequestCycle(cycle);

        lf.initializeService();

        Map parameters = new HashMap();

        ILink link = lf.constructLink(service, false, parameters, true);

        assertEquals("{encoded}", link.getURL());

        verifyControls();
    }

    private void trainEncodeURL(IRequestCycle cycle, String inputURL, String encodeURL)
    {
        cycle.encodeURL(inputURL);
        setReturnValue(cycle, encodeURL);
    }

    public void testNoopEncoders()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();
        IEngine engine = newEngine();
        IEngineService service = newService("myservice");

        trainGetEngine(cycle, engine);
        trainGetOutputEncoding(engine, "utf-8");

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
        lf.setRequestCycle(cycle);

        lf.initializeService();

        Map parameters = new HashMap();

        ILink link = lf.constructLink(service, false, parameters, false);

        verifyControls();

        assertEquals("/context/app?service=myservice", link.getURL());
    }

    public void testActiveEncoder()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();
        IEngineService service = newService("page");
        IEngine engine = newEngine();

        trainGetEngine(cycle, engine);
        trainGetOutputEncoding(engine, "utf-8");

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
        lf.setRequestCycle(cycle);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.PAGE, "Barney");

        ILink link = lf.constructLink(service, false, parameters, false);

        verifyControls();

        assertEquals("/context/Barney.html", link.getURL());
    }

    public void testServiceNameIsNull()
    {
        IEngineService service = newService(null);

        Map parameters = new HashMap();

        replayControls();

        LinkFactory lf = new LinkFactoryImpl();

        try
        {
            lf.constructLink(service, false, parameters, true);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(ImplMessages.serviceNameIsNull(), ex.getMessage());
        }

        verifyControls();
    }

    public void testWithServiceParameters()
    {
        WebRequest request = newRequest();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newErrorLog();
        IEngineService service = newService("external");
        IEngine engine = newEngine();

        trainGetEngine(cycle, engine);
        trainGetOutputEncoding(engine, "utf-8");

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
        lf.setDataSqueezer(DataSqueezerUtil.createUnitTestSqueezer());
        lf.setRequest(request);
        lf.setRequestCycle(cycle);

        lf.initializeService();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.PAGE, "Barney");
        parameters.put(ServiceConstants.PARAMETER, new Object[]
        { Boolean.TRUE });

        ILink link = lf.constructLink(service, false, parameters, false);

        verifyControls();

        assertEquals("/context/Barney.ext?sp=T", link.getURL());
    }
}