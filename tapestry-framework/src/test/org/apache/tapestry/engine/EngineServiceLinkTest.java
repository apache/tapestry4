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
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.EngineServiceLink}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
@Test
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

        EngineServiceLink l = new EngineServiceLink("/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        replay();

        assertEquals("/context/servlet?service=myservice", l.getURL());

        verify();

        assertListEquals(new String[]
        { ServiceConstants.SERVICE, ServiceConstants.PARAMETER }, l.getParameterNames());

    }

    public void testGetURLWithServiceParameters()
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("foo", new String[]
                { "godzilla", "frodo" }), false);
        
        replay();

        assertEquals("/ctx/app?service=foo&sp=godzilla&sp=frodo", l.getURL());

        verify();
    }

    /** @since 4.0 */

    public void testGetURLSansParameters()
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), true);

        replay();
        
        assertEquals("/context/servlet", l.getURL(null, false));

        verify();
    }

    /** @since 4.0 */

    public void testGetURLWithAnchor()
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), false);
        
        replay();

        assertEquals("/context/servlet#anchor", l.getURL("anchor", false));

        verify();
    }

    public void testGetURLWithAnchorAndParameters() throws Exception
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/context/servlet", ENCODING, _urlCodec,
                request, buildParameters("myservice", null), false);
        
        replay();

        assertEquals("/context/servlet?service=myservice#anchor", l.getURL("anchor", true));

        verify();
    }

    public void testGetAbsoluteURL() throws Exception
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        trainGetServerPort(request, 9187);
        
        trainGetScheme(request, "HTTP");
        
        trainGetServerName(request, "TESTSERVER.COM");
        
        replay();
        
        assertEquals("HTTP://TESTSERVER.COM:9187/ctx/app?service=myservice", l.getAbsoluteURL());

        verify();
    }

    public void testGetAbsoluteURLWithOverrides() throws Exception
    {
        WebRequest request = newRequest();

        EngineServiceLink l = new EngineServiceLink("/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);
        
        replay();

        assertEquals("https://myserver.net:9100/ctx/app?service=myservice", l.getAbsoluteURL(
                "https",
                "myserver.net",
                9100,
                null,
                true));

        verify();
    }

    public void testGetURLNotAbsolute()
    {
        WebRequest request = newRequest();

        trainGetScheme(request, "http");
        trainGetServerName(request, "myserver.net");
        trainGetServerPort(request, 80);
        
        replay();

        EngineServiceLink l = new EngineServiceLink("/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        assertEquals("/ctx/app?service=myservice#myanchor", l.getURL(
                "http",
                "myserver.net",
                80,
                "myanchor",
                true));

        verify();
    }

    public void testGetURLAbsolute()
    {
        WebRequest request = newRequest();

        trainGetScheme(request, "http");
        
        replay();

        EngineServiceLink l = new EngineServiceLink("/ctx/app", ENCODING, _urlCodec, request,
                buildParameters("myservice", null), false);

        assertEquals("https://override.net:8080/ctx/app?service=myservice#myanchor", l.getURL(
                "https",
                "override.net",
                8080,
                "myanchor",
                true));

        verify();
    }
}