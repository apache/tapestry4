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

package org.apache.tapestry.engine.encoders;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * The canonical implementation of {@link org.apache.tapestry.engine.ServiceEncoder}, it
 * recognizes a service and a page. The page name becomes the servlet path, prefixed with "/" and
 * suffixed with a dot and a particular extension. In this way, "/app?service=page&amp;page=Home"
 * becomes simply "Home.html".
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServicePathEncoder implements ServiceEncoder
{
    private String _extension;

    private String _serviceName;

    public void encode(ServiceEncoding encoding)
    {
        String service = encoding.getParameterValue(ServiceConstants.SERVICE);

        if (!service.equals(_serviceName))
            return;

        String pageName = encoding.getParameterValue(ServiceConstants.PAGE);

        // Only handle pages in the application namespace (not from a library).

        if (pageName.indexOf(INamespace.SEPARATOR) >= 0)
            return;

        StringBuffer buffer = new StringBuffer("/");
        buffer.append(pageName);
        buffer.append('.');
        buffer.append(_extension);

        encoding.setServletPath(buffer.toString());

        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
    }

    public void decode(ServiceEncoding encoding)
    {
        String servletPath = encoding.getServletPath();

        int dotx = servletPath.lastIndexOf('.');
        if (dotx < 0)
            return;

        String extension = servletPath.substring(dotx + 1);

        if (!extension.equals(_extension))
            return;

        // Skip the slash and the dot.

        String page = servletPath.substring(1, dotx);

        encoding.setParameterValue(ServiceConstants.SERVICE, _serviceName);
        encoding.setParameterValue(ServiceConstants.PAGE, page);
    }

    public void setExtension(String extension)
    {
        _extension = extension;
    }

    public void setServiceName(String serviceName)
    {
        _serviceName = serviceName;
    }
}