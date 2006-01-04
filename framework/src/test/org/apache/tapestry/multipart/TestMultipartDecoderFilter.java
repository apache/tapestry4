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

package org.apache.tapestry.multipart;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.services.ServletRequestServicer;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.multipart.MultipartDecoderFilter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestMultipartDecoderFilter extends HiveMindTestCase
{
    private static class MockServicer implements ServletRequestServicer
    {
        HttpServletRequest _request;

        public void service(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException
        {
            _request = request;
        }
    }

    private HttpServletRequest newRequest(String contentType)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getContentType();
        control.setReturnValue(contentType);

        return request;
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    public void testNormalRequest() throws Exception
    {
        HttpServletRequest request = newRequest("application/x-www-form-urlencoded");
        HttpServletResponse response = newResponse();

        MockServicer servicer = new MockServicer();

        replayControls();

        MultipartDecoderFilter f = new MultipartDecoderFilter();

        f.service(request, response, servicer);

        assertSame(request, servicer._request);

        verifyControls();
    }

    public void testUploadRequest() throws Exception
    {
        HttpServletRequest request = newRequest("multipart/form-data");
        HttpServletResponse response = newResponse();
        HttpServletRequest decoded = (HttpServletRequest) newMock(HttpServletRequest.class);

        MockControl control = newControl(MultipartDecoder.class);
        MultipartDecoder decoder = (MultipartDecoder) control.getMock();

        decoder.decode(request);
        control.setReturnValue(decoded);

        decoder.cleanup();

        MockServicer servicer = new MockServicer();

        replayControls();

        MultipartDecoderFilter f = new MultipartDecoderFilter();
        f.setDecoder(decoder);

        f.service(request, response, servicer);

        assertSame(decoded, servicer._request);

        verifyControls();
    }
}