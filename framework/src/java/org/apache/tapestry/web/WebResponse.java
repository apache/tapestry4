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

package org.apache.tapestry.web;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Controls the response to the client, and specifically allows for creating the output stream (or
 * print writer) to which content is sent.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface WebResponse
{
    /**
     * Returns a output stream to which output should be sent. This method should only be invoked
     * once on a response.
     * 
     * @return the output stream, configured for the given type.
     */

    public OutputStream getOutputStream() throws IOException;

    /**
     * Encodes a URL, which adds information to the URL needed to ensure that the request triggered
     * by the URL will be associated with the current session (if any). In most cases, the string is
     * returned unchanged.
     */

    public String encodeURL(String url);

    /**
     * Resets any buffered content. This may be used after an error to radically change what the
     * output will be.
     */

    public void reset();

    public void setContentType(String contentType);
}