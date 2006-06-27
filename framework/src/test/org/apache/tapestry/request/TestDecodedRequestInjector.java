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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Tests for {@link org.apache.tapestry.request.DecodedRequestInjector}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDecodedRequestInjector extends BaseComponentTestCase
{
    private static class ServicerFixture implements ServletRequestServicer
    {
        HttpServletRequest _request;

        public void service(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException
        {
            _request = request;
        }
    }

    private HttpServletRequest newHttpRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    private ILibrarySpecification newSpec(boolean exists, IRequestDecoder decoder)
    {
        ILibrarySpecification spec = newMock(ILibrarySpecification.class);

        expect(spec.checkExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME)).andReturn(exists);

        if (exists)
        {
            expect(spec.getExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME, IRequestDecoder.class))
            .andReturn(decoder);
        }

        return spec;
    }

    public void testNoExtension() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        ILibrarySpecification spec = newSpec(false, null);

        ServletRequestServicer servicer = (ServletRequestServicer) newMock(ServletRequestServicer.class);

        servicer.service(request, response);

        replay();

        DecodedRequestInjector dri = new DecodedRequestInjector();

        dri.setApplicationSpecification(spec);
        dri.initializeService();

        dri.service(request, response, servicer);

        verify();
    }

    public void testWithExtension() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        
        IRequestDecoder decoder = newMock(IRequestDecoder.class);
        ILibrarySpecification spec = newSpec(true, decoder);

        ServicerFixture servicer = new ServicerFixture();

        DecodedRequest decoded = new DecodedRequest();
        decoded.setRequestURI("/foo/bar/baz");

        expect(decoder.decodeRequest(request)).andReturn(decoded);

        replay();

        DecodedRequestInjector dri = new DecodedRequestInjector();

        dri.setApplicationSpecification(spec);
        dri.initializeService();

        dri.service(request, response, servicer);

        // Prove that the request passed down the pipeline is a wrapper

        assertEquals("/foo/bar/baz", servicer._request.getRequestURI());

        verify();
    }
}