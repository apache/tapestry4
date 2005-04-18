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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.services.ResetEventCoordinator;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.DisableCachingFilter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDisableCachingFilter extends HiveMindTestCase
{
    private WebRequest newRequest()
    {
        return (WebRequest) newMock(WebRequest.class);
    }

    private WebResponse newResponse()
    {
        return (WebResponse) newMock(WebResponse.class);
    }

    private WebRequestServicer newServicer()
    {
        return (WebRequestServicer) newMock(WebRequestServicer.class);
    }

    private ResetEventCoordinator newREC()
    {
        return (ResetEventCoordinator) newMock(ResetEventCoordinator.class);
    }

    public void testNormal() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ResetEventCoordinator rec = newREC();

        servicer.service(request, response);
        rec.fireResetEvent();

        replayControls();

        DisableCachingFilter f = new DisableCachingFilter();
        f.setResetEventCoordinator(rec);

        f.service(request, response, servicer);

        verifyControls();
    }

    public void testResetFailure() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        MockControl control = newControl(ResetEventCoordinator.class);
        ResetEventCoordinator rec = (ResetEventCoordinator) control.getMock();
        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        Location l = fabricateLocation(99);

        Throwable t = new ApplicationRuntimeException("Mock failure.", l, null);

        servicer.service(request, response);

        rec.fireResetEvent();
        control.setThrowable(t);

        log.error(ImplMessages.errorResetting(t), l, t);

        replayControls();

        DisableCachingFilter f = new DisableCachingFilter();
        f.setResetEventCoordinator(rec);
        f.setErrorLog(log);

        f.service(request, response, servicer);

        verifyControls();
    }

}