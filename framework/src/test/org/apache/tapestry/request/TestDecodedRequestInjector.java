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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.request.DecodedRequestInjector}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestDecodedRequestInjector extends HiveMindTestCase
{
    private static class ServicerFixture implements RequestServicer
    {
        HttpServletRequest _request;

        public void service(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException
        {
            _request = request;
        }
    }

    private HttpServletRequest newRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    private ILibrarySpecification newSpec(boolean exists, IRequestDecoder decoder)
    {
        MockControl control = newControl(ILibrarySpecification.class);
        ILibrarySpecification spec = (ILibrarySpecification) control.getMock();

        spec.checkExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME);
        control.setReturnValue(exists);

        if (exists)
        {
            spec.getExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME, IRequestDecoder.class);
            control.setReturnValue(decoder);
        }

        return spec;
    }

    public void testNoExtension() throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();
        ILibrarySpecification spec = newSpec(false, null);

        RequestServicer servicer = (RequestServicer) newMock(RequestServicer.class);

        servicer.service(request, response);

        replayControls();

        DecodedRequestInjector dri = new DecodedRequestInjector();

        dri.setApplicationSpecification(spec);
        dri.initializeService();

        dri.service(request, response, servicer);

        verifyControls();
    }

    public void testWithExtension() throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        MockControl decoderc = newControl(IRequestDecoder.class);
        IRequestDecoder decoder = (IRequestDecoder) decoderc.getMock();
        ILibrarySpecification spec = newSpec(true, decoder);

        ServicerFixture servicer = new ServicerFixture();

        DecodedRequest decoded = new DecodedRequest();
        decoded.setRequestURI("/foo/bar/baz");

        decoder.decodeRequest(request);
        decoderc.setReturnValue(decoded);

        replayControls();

        DecodedRequestInjector dri = new DecodedRequestInjector();

        dri.setApplicationSpecification(spec);
        dri.initializeService();

        dri.service(request, response, servicer);

        // Prove that the request passed down the pipeline is a wrapper

        assertEquals("/foo/bar/baz", servicer._request.getRequestURI());

        verifyControls();
    }
}