// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.engine;

/**
 * Encapsulates the logic for encoding and decoding service requests.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ServiceEncoder
{
    /**
     * Invoked by the {@link org.apache.tapestry.services.LinkFactory}&nbsp;to encode the request.
     * Encoding is the process of modifying the encoding object to represent the same data in a
     * different format; the canoncial example is to replace the
     * {@link org.apache.tapestry.services.ServiceConstants#PAGE}and
     * {@link org.apache.tapestry.services.ServiceConstants#SERVICE}query parameters with a servlet
     * path (i.e., "/Home.html", if the ".html" extension is mapped to the page service).
     * <p>
     * The {@link org.apache.tapestry.services.LinkFactory}&nbsp;iterates over a collection of
     * encoders, stopping once the ServiceRequestEncoding is modified in any way.
     */

    public void encode(ServiceEncoding encoding);

    /**
     * Invoked by XXX to decode a request. The encoder is responsible for recognizing a request it
     * may have encoded, and for restoring any query parameters is may have removed.
     */

    public void decode(ServiceEncoding encoding);
}