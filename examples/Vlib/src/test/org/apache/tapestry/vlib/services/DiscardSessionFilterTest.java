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

package org.apache.tapestry.vlib.services;

import java.io.IOException;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;

/**
 * Tests for {@link org.apache.tapestry.vlib.services.DiscardSessionFilter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DiscardSessionFilterTest extends HiveMindTestCase
{
    public void testNormal() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ApplicationLifecycle lifecycle = newLifecycle();

        servicer.service(request, response);

        trainGetDiscardSession(lifecycle, false);

        replayControls();

        DiscardSessionFilter filter = new DiscardSessionFilter();

        filter.setApplicationLifecycle(lifecycle);

        filter.service(request, response, servicer);

        verifyControls();
    }

    public void testDiscard() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ApplicationLifecycle lifecycle = newLifecycle();
        WebSession session = newSession();

        servicer.service(request, response);

        trainGetDiscardSession(lifecycle, true);

        trainGetSession(request, session);

        session.invalidate();

        replayControls();

        DiscardSessionFilter filter = new DiscardSessionFilter();

        filter.setApplicationLifecycle(lifecycle);

        filter.service(request, response, servicer);

        verifyControls();
    }

    public void testDiscardNoSession() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ApplicationLifecycle lifecycle = newLifecycle();

        servicer.service(request, response);

        trainGetDiscardSession(lifecycle, true);

        trainGetSession(request, null);

        replayControls();

        DiscardSessionFilter filter = new DiscardSessionFilter();

        filter.setApplicationLifecycle(lifecycle);

        filter.service(request, response, servicer);

        verifyControls();
    }

    public void testDiscardSessionIllegalState() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ApplicationLifecycle lifecycle = newLifecycle();
        WebSession session = newSession();

        servicer.service(request, response);

        trainGetDiscardSession(lifecycle, true);

        trainGetSession(request, session);

        session.invalidate();
        setThrowable(session, new IllegalStateException());

        replayControls();

        DiscardSessionFilter filter = new DiscardSessionFilter();

        filter.setApplicationLifecycle(lifecycle);

        filter.service(request, response, servicer);

        verifyControls();
    }

    public void testDiscardCheckAfterException() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ApplicationLifecycle lifecycle = newLifecycle();

        servicer.service(request, response);
        setThrowable(servicer, new IOException());

        trainGetDiscardSession(lifecycle, false);

        replayControls();

        DiscardSessionFilter filter = new DiscardSessionFilter();

        filter.setApplicationLifecycle(lifecycle);

        try
        {
            filter.service(request, response, servicer);
            unreachable();
        }
        catch (IOException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    private ApplicationLifecycle newLifecycle()
    {
        return (ApplicationLifecycle) newMock(ApplicationLifecycle.class);
    }

    private WebRequestServicer newServicer()
    {
        return (WebRequestServicer) newMock(WebRequestServicer.class);
    }

    private WebResponse newResponse()
    {
        return (WebResponse) newMock(WebResponse.class);
    }

    private WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    private WebSession newSession()
    {
        return (WebSession) newMock(WebSession.class);
    }

    private void trainGetSession(WebRequest request, WebSession session)
    {
        request.getSession(false);
        setReturnValue(request, session);
    }

    private void trainGetDiscardSession(ApplicationLifecycle lifecycle, boolean discard)
    {
        lifecycle.getDiscardSession();
        setReturnValue(lifecycle, discard);
    }
}
