// Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.codec.net.URLCodec;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.request.RequestContext;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

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

    /** @since 3.1 */
    public void testGetURLWithParameters()
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        rc.encodeURL("/context/servlet?service=myservice");
        control.setReturnValue("/context/servlet?service=myservice;encoded");

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                "myservice", null, null, true);

        replayControls();

        assertEquals("/context/servlet?service=myservice;encoded", l.getURL());

        verifyControls();

        checkList("parameterNames", new String[]
        { Tapestry.SERVICE_QUERY_PARAMETER_NAME, Tapestry.PARAMETERS_QUERY_PARAMETER_NAME }, l
                .getParameterNames());

    }

    public void testGetURLWithServiceContext()
    {
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                "myservice", new String[]
                { "alpha", "bravo" }, null, false);

        replayControls();

        assertEquals("/context/servlet?service=myservice%2Falpha%2Fbravo", l.getURL());

        verifyControls();

        assertEquals("myservice/alpha/bravo", l
                .getParameterValues(Tapestry.SERVICE_QUERY_PARAMETER_NAME)[0]);
    }

    public void testGetURLWithServiceParameters()
    {
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec, "foo",
                null, new String[]
                { "godzilla", "frodo" }, false);

        replayControls();

        assertEquals("/ctx/app?service=foo&sp=godzilla&sp=frodo", l.getURL());

        verifyControls();
    }

    /** @since 3.1 */

    public void testGetURLSansParameters()
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                "myservice", null, null, true);

        rc.encodeURL("/context/servlet");
        control.setReturnValue("/context/servlet;encoded");

        replayControls();

        assertEquals("/context/servlet;encoded", l.getURL(null, false));

        verifyControls();
    }

    /** @since 3.1 */

    public void testGetURLWithAnchor()
    {
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                "myservice", null, null, false);

        replayControls();

        assertEquals("/context/servlet#anchor", l.getURL("anchor", false));

        verifyControls();
    }

    public void testGetURLWithAnchorAndParameters() throws Exception
    {
        IRequestCycle rc = (IRequestCycle) newMock(IRequestCycle.class);

        EngineServiceLink l = new EngineServiceLink(rc, "/context/servlet", ENCODING, _urlCodec,
                "myservice", null, null, false);

        replayControls();

        assertEquals("/context/servlet?service=myservice#anchor", l.getURL("anchor", true));

        verifyControls();
    }

    public void testGetAbsoluteURL() throws Exception
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        MockControl contextc = MockClassControl.createStrictControl(RequestContext.class);
        addControl(contextc);

        RequestContext context = (RequestContext) contextc.getMock();

        rc.getRequestContext();
        control.setReturnValue(context);

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec,
                "myservice", null, null, false);

        context.getScheme();
        contextc.setReturnValue("HTTP");

        context.getServerName();
        contextc.setReturnValue("TESTSERVER.COM");

        context.getServerPort();
        contextc.setReturnValue(9187);

        replayControls();

        assertEquals("HTTP://TESTSERVER.COM:9187/ctx/app?service=myservice", l.getAbsoluteURL());

        verifyControls();
    }

    public void testGetAbsoluteURLWithOverrides() throws Exception
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle rc = (IRequestCycle) control.getMock();

        MockControl contextc = MockClassControl.createStrictControl(RequestContext.class);
        addControl(contextc);

        RequestContext context = (RequestContext) contextc.getMock();

        rc.getRequestContext();
        control.setReturnValue(context);

        EngineServiceLink l = new EngineServiceLink(rc, "/ctx/app", ENCODING, _urlCodec,
                "myservice", null, null, false);

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