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

package org.apache.tapestry.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.request.DecodedRequestWrapper}and
 * {@link org.apache.tapestry.request.DecodedRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestDecodedRequestWrapper extends HiveMindTestCase
{
    private HttpServletRequest newRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    public void testInterceptedMethods()
    {
        DecodedRequest dr = new DecodedRequest();

        dr.setRequestURI("/foo/bar/baz");
        dr.setScheme("https");
        dr.setServerPort(2170);
        dr.setServerName("www.flintstone.com");

        HttpServletRequest request = newRequest();

        replayControls();

        DecodedRequestWrapper w = new DecodedRequestWrapper(request, dr);

        assertEquals("/foo/bar/baz", w.getRequestURI());
        assertEquals("https", w.getScheme());
        assertEquals(2170, w.getServerPort());
        assertEquals("www.flintstone.com", w.getServerName());

        verifyControls();
    }

    public void testRequestConstructor()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getScheme();
        control.setReturnValue("https");

        request.getServerName();
        control.setReturnValue("www.flintstone.com");

        request.getRequestURI();
        control.setReturnValue("/foo/bar/baz");

        request.getServerPort();
        control.setReturnValue(2170);

        replayControls();

        DecodedRequest dr = new DecodedRequest(request);

        assertEquals("/foo/bar/baz", dr.getRequestURI());
        assertEquals("https", dr.getScheme());
        assertEquals(2170, dr.getServerPort());
        assertEquals("www.flintstone.com", dr.getServerName());

        verifyControls();
    }
}