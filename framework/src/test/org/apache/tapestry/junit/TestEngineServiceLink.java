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

package org.apache.tapestry.junit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.net.URLCodec;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.EngineServiceLink}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
public class TestEngineServiceLink extends TapestryTestCase
{
    private URLCodec _urlCodec = new URLCodec();

    private static final String ENCODING = "utf-8";

    private Map buildParameters(String serviceName, String[] serviceParameters)
    {
        Map result = new HashMap();

        result.put(ServiceConstants.SERVICE, serviceName);
        result.put(ServiceConstants.PARAMETER, serviceParameters);

        return result;
    }

    /** @since 3.1 */
    private WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    /** @since 3.1 */
    public void testGetURLWithParameters()
    {
        WebRequest request = newRequest();
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        rc.encodeURL("/context/servlet?service=myservice");
        control.setReturnValue("/context/servlet?service=myservice;encoded");

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        replayControls();

        assertEquals("/context/servlet?service=myservice;encoded", l.getURL());

        verifyControls();

        checkList("parameterNames", new String[]
        { Tapestry.SERVICE_QUERY_PARAMETER_NAME, Tapestry.PARAMETERS_QUERY_PARAMETER_NAME }, l
                .getParameterNames());

    }

    public void testGetURLWithServiceParameters()
    {
        WebRequest request = newRequest();

        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("foo", new String[]
                { "godzilla", "frodo" }), false);

        replayControls();

        assertEquals("/ctx/app?service=foo&sp=godzilla&sp=frodo", l.getURL());

        verifyControls();
    }

    /** @since 3.1 */

    public void testGetURLSansParameters()
    {
        WebRequest request = newRequest();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        rc.encodeURL("/context/servlet");
        control.setReturnValue("/context/servlet;encoded");

        replayControls();

        assertEquals("/context/servlet;encoded", l.getURL(null, false));

        verifyControls();
    }

    /** @since 3.1 */

    public void testGetURLWithAnchor()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), false);

        replayControls();

        assertEquals("/context/servlet#anchor", l.getURL("anchor", false));

        verifyControls();
    }

    public void testGetURLWithAnchorAndParameters() throws Exception
    {
        WebRequest request = newRequest();
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), false);

        replayControls();

        assertEquals("/context/servlet?service=myservice#anchor", l.getURL("anchor", true));

        verifyControls();
    }

    public void testGetAbsoluteURL() throws Exception
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        MockControl requestc = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestc.getMock();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        request.getScheme();
        requestc.setReturnValue("HTTP");

        request.getServerName();
        requestc.setReturnValue("TESTSERVER.COM");

        request.getServerPort();
        requestc.setReturnValue(9187);

        replayControls();

        assertEquals("HTTP://TESTSERVER.COM:9187/ctx/app?service=myservice", l.getAbsoluteURL());

        verifyControls();
    }

    public void testGetAbsoluteURLWithOverrides() throws Exception
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        MockControl requestc = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestc.getMock();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        replayControls();

        assertEquals("https://myserver.net:9100/ctx/app?service=myservice", l.getAbsoluteURL(
                "https",
                "myserver.net",
                9100,
                null,
                true));

        verifyControls();
    }
}