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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.BaseComponentTestCase;

/**
 * Tests for {@link org.apache.tapestry.request.DecodedRequestWrapper}and
 * {@link org.apache.tapestry.request.DecodedRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDecodedRequestWrapper extends BaseComponentTestCase
{
    private HttpServletRequest newHttpRequest()
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

        HttpServletRequest request = newHttpRequest();

        replay();

        DecodedRequestWrapper w = new DecodedRequestWrapper(request, dr);

        assertEquals("/foo/bar/baz", w.getRequestURI());
        assertEquals("https", w.getScheme());
        assertEquals(2170, w.getServerPort());
        assertEquals("www.flintstone.com", w.getServerName());

        verify();
    }

    public void testRequestConstructor()
    {
        HttpServletRequest request = newHttpRequest();

        expect(request.getScheme()).andReturn("https");

        expect(request.getServerName()).andReturn("www.flintstone.com");

        expect(request.getRequestURI()).andReturn("/foo/bar/baz");

        expect(request.getServerPort()).andReturn(2170);

        replay();

        DecodedRequest dr = new DecodedRequest(request);

        assertEquals("/foo/bar/baz", dr.getRequestURI());
        assertEquals("https", dr.getScheme());
        assertEquals(2170, dr.getServerPort());
        assertEquals("www.flintstone.com", dr.getServerName());

        verify();
    }
}