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

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;

/**
 * Tests for {@link org.apache.tapestry.engine.EngineServiceLink}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
public class EngineServiceLinkTest extends BaseComponentTestCase
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

    /** @since 4.0 */
    public void testGetURLWithParameters()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        trainEncodeURL(
                rc,
                "/context/servlet?service=myservice",
                "/context/servlet?service=myservice;encoded");

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        replayControls();

        assertEquals("/context/servlet?service=myservice;encoded", l.getURL());

        verifyControls();

        assertListsEqual(new String[]
        { ServiceConstants.SERVICE, ServiceConstants.PARAMETER }, l.getParameterNames());

    }

    public void testGetURLWithServiceParameters()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("foo", new String[]
                { "godzilla", "frodo" }), false);

        replayControls();

        assertEquals("/ctx/app?service=foo&sp=godzilla&sp=frodo", l.getURL());

        verifyControls();
    }

    /** @since 4.0 */

    public void testGetURLSansParameters()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        trainEncodeURL(rc, "/context/servlet", "/context/servlet;encoded");

        replayControls();

        assertEquals("/context/servlet;encoded", l.getURL(null, false));

        verifyControls();
    }

    /** @since 4.0 */

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
        IRequestCycle rc = newCycle();

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), false);

        replayControls();

        assertEquals("/context/servlet?service=myservice#anchor", l.getURL("anchor", true));

        verifyControls();
    }

    public void testGetAbsoluteURL() throws Exception
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        trainGetScheme(request, "HTTP");

        trainGetServerName(request, "TESTSERVER.COM");

        trainGetServerPort(request, 9187);

        replayControls();

        assertEquals("HTTP://TESTSERVER.COM:9187/ctx/app?service=myservice", l.getAbsoluteURL());

        verifyControls();
    }

    public void testGetAbsoluteURLWithOverrides() throws Exception
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

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

    public void testGetURLNotAbsolute()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        trainGetScheme(request, "http");
        trainGetServerName(request, "myserver.net");
        trainGetServerPort(request, 80);

        replayControls();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        assertEquals("/ctx/app?service=myservice#myanchor", l.getURL(
                "http",
                "myserver.net",
                80,
                "myanchor",
                true));

        verifyControls();
    }

    public void testGetURLAbsolute()
    {
        WebRequest request = newRequest();
        IRequestCycle rc = newCycle();

        trainGetScheme(request, "http");

        replayControls();

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        assertEquals("https://override.net:8080/ctx/app?service=myservice#myanchor", l.getURL(
                "https",
                "override.net",
                8080,
                "myanchor",
                true));

        verifyControls();
    }
}