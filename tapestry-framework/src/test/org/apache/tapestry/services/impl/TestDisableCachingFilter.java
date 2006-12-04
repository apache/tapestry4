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

import static org.easymock.EasyMock.expectLastCall;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.ResetEventHub;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.DisableCachingFilter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential=true)
public class TestDisableCachingFilter extends BaseComponentTestCase
{
    private WebResponse newResponse()
    {
        return newMock(WebResponse.class);
    }

    private WebRequestServicer newServicer()
    {
        return newMock(WebRequestServicer.class);
    }

    private ResetEventHub newREC()
    {
        return newMock(ResetEventHub.class);
    }

    public void test_Normal() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        ResetEventHub rec = newREC();

        servicer.service(request, response);
        rec.fireResetEvent();

        replay();

        DisableCachingFilter f = new DisableCachingFilter();
        f.setResetEventHub(rec);

        f.service(request, response, servicer);

        verify();
    }

    public void test_Reset_Failure() throws Exception
    {
        WebRequest request = newRequest();
        WebResponse response = newResponse();
        WebRequestServicer servicer = newServicer();
        
        ResetEventHub rec = newMock(ResetEventHub.class);
        ErrorLog log = newMock(ErrorLog.class);

        Location l = fabricateLocation(99);

        Throwable t = new ApplicationRuntimeException("Mock failure.", l, null);

        servicer.service(request, response);

        rec.fireResetEvent();
        expectLastCall().andThrow(t);

        log.error(ImplMessages.errorResetting(t), l, t);

        replay();

        DisableCachingFilter f = new DisableCachingFilter();
        f.setResetEventHub(rec);
        f.setErrorLog(log);

        f.service(request, response, servicer);

        verify();
    }
}
