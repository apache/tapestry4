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

package org.apache.tapestry.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.test.TypeMatcher;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.easymock.MockControl;

/**
 * Tests for {@link TestActionPortletToWebRequestServicerPipelineBridge}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestActionPortletToWebRequestServicerPipelineBridge extends HiveMindTestCase
{
    private class WebRequestServicerFixture implements WebRequestServicer
    {
        WebRequest _request;

        WebResponse _response;

        public void service(WebRequest request, WebResponse response) throws IOException
        {
            _request = request;
            _response = response;
        }

    }

    public void testSuccess() throws Exception
    {
        ActionRequest request = (ActionRequest) newMock(ActionRequest.class);

        MockControl responsec = newControl(ActionResponse.class);
        ActionResponse response = (ActionResponse) responsec.getMock();

        PortletRequestGlobals prg = (PortletRequestGlobals) newMock(PortletRequestGlobals.class);
        WebRequestServicerFixture wrs = new WebRequestServicerFixture();

        prg.store(request, response);

        request.removeAttribute("FOO");
        response.encodeURL("FOO");
        responsec.setReturnValue(null);

        replayControls();

        ActionPortletToWebRequestServicerPipelineBridge bridge = new ActionPortletToWebRequestServicerPipelineBridge();
        bridge.setPortletRequestGlobals(prg);
        bridge.setWebRequestServicer(wrs);

        bridge.service(request, response);

        // Test that the WebXXX wrappers createde by the bridge and passed to the WebRequestServicer
        // encapsulate the ActionRequest and ActionResponse

        wrs._request.setAttribute("FOO", null);
        wrs._response.encodeURL("FOO");

        verifyControls();
    }

    public void testFailure() throws Exception
    {
        ActionRequest request = (ActionRequest) newMock(ActionRequest.class);
        ActionResponse response = (ActionResponse) newMock(ActionResponse.class);
        PortletRequestGlobals prg = (PortletRequestGlobals) newMock(PortletRequestGlobals.class);

        MockControl control = newControl(WebRequestServicer.class);
        WebRequestServicer servicer = (WebRequestServicer) control.getMock();

        Throwable t = new RuntimeException("Failure.");

        prg.store(request, response);
        servicer.service(new PortletWebRequest(request), new PortletWebResponse(response));
        control.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { new TypeMatcher(), new TypeMatcher() }));
        control.setThrowable(t);

        replayControls();

        ActionPortletToWebRequestServicerPipelineBridge bridge = new ActionPortletToWebRequestServicerPipelineBridge();
        bridge.setPortletRequestGlobals(prg);
        bridge.setWebRequestServicer(servicer);

        try
        {

            bridge.service(request, response);
            unreachable();
        }
        catch (PortletException ex)
        {
            // PortletException doesn't seem to copy the
            // message?
            // assertEquals("Failure.", ex.getMessage());
            // Note: implemented by PortletException, not tied
            // to JDK 1.4
            assertSame(t, ex.getCause());
        }

        verifyControls();
    }
}