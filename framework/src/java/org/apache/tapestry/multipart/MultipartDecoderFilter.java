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

import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.services.ServletRequestServicerFilter;

/**
 * Checks to see if the request is a file upload and, if so, uses the
 * {@link org.apache.tapestry.multipart.MultipartDecoder}&nbsp;to obtain form parameters.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class MultipartDecoderFilter implements ServletRequestServicerFilter
{
    private MultipartDecoder _decoder;

    public void service(HttpServletRequest request, HttpServletResponse response,
            ServletRequestServicer servicer) throws IOException, ServletException
    {
        String contentType = request.getContentType();

        // contentType is occasionally null in testing. The browser tacks on additional
        // information onto the contentType to indicate where the boundaries are in
        // the stream.

        boolean encoded = contentType != null && contentType.startsWith("multipart/form-data");

        try
        {
            HttpServletRequest newRequest = encoded ? _decoder.decode(request) : request;

            servicer.service(newRequest, response);
        }
        finally
        {
            if (encoded)
                _decoder.cleanup();
        }
    }

    public void setDecoder(MultipartDecoder decoder)
    {
        _decoder = decoder;
    }
}