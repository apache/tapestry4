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

package org.apache.tapestry.engine.encoders;

import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Encodes the service name, typical output is "/page.svc" where "page" is a service name. This is
 * useful for the home and restart services, for example. This encoder should be prioritized very
 * low so that it doesn't prevent other encoders from doing their work.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ServiceExtensionEncoder implements ServiceEncoder
{
    private String _extension;

    public void setExtension(String extension)
    {
        _extension = extension;
    }

    public void encode(ServiceEncoding encoding)
    {
        String service = encoding.getParameterValue(ServiceConstants.SERVICE);

        // Can assume this is never null.

        encoding.setServletPath("/" + service + "." + _extension);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
    }

    public void decode(ServiceEncoding encoding)
    {
        String servletPath = encoding.getServletPath();

        int dotx = servletPath.lastIndexOf('.');

        String extension = servletPath.substring(dotx + 1);

        if (!extension.equals(_extension))
            return;

        // The first character should be a slash, then the service name, then the dot.

        String service = servletPath.substring(1, dotx);

        encoding.setParameterValue(ServiceConstants.SERVICE, service);
    }

}