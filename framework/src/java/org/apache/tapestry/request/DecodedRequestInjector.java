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

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.services.ServletRequestServicerFilter;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Checks to see if a {@link org.apache.tapestry.request.IRequestDecoder}has been provided as an
 * application extension, and (if so), creates a new HttpServletRequest wrapper around the decoded
 * request.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DecodedRequestInjector implements ServletRequestServicerFilter
{
    private ILibrarySpecification _applicationSpecification;

    private IRequestDecoder _decoder;

    public void initializeService()
    {
        if (_applicationSpecification.checkExtension(Tapestry.REQUEST_DECODER_EXTENSION_NAME))
            _decoder = (IRequestDecoder) _applicationSpecification.getExtension(
                    Tapestry.REQUEST_DECODER_EXTENSION_NAME,
                    IRequestDecoder.class);
    }

    public void service(HttpServletRequest request, HttpServletResponse response,
            ServletRequestServicer servicer) throws IOException, ServletException
    {
        HttpServletRequest decodedRequest = _decoder == null ? request : wrapRequest(request);

        servicer.service(decodedRequest, response);
    }

    public HttpServletRequest wrapRequest(HttpServletRequest request)
    {
        DecodedRequest decodedRequest = _decoder.decodeRequest(request);

        return new DecodedRequestWrapper(request, decodedRequest);
    }

    public void setApplicationSpecification(ILibrarySpecification applicationSpecification)
    {
        _applicationSpecification = applicationSpecification;
    }

}