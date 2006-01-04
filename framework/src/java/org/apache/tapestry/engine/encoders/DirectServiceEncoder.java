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

import org.apache.tapestry.INamespace;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * A specialized encoder for the {@link org.apache.tapestry.engine.DirectService direct service}
 * &nbsp;that encodes the page name and component id path into the servlet path, and encodes the
 * stateful flag by choosing one of two extensions.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DirectServiceEncoder implements ServiceEncoder
{
    private String _statelessExtension;

    private String _statefulExtension;

    public void encode(ServiceEncoding encoding)
    {
        String service = encoding.getParameterValue(ServiceConstants.SERVICE);
        if (!service.equals(Tapestry.DIRECT_SERVICE))
            return;

        String pageName = encoding.getParameterValue(ServiceConstants.PAGE);

        // Only handle pages in the application namespace (not from a library).

        if (pageName.indexOf(INamespace.SEPARATOR) >= 0)
            return;

        String stateful = encoding.getParameterValue(ServiceConstants.SESSION);
        String componentIdPath = encoding.getParameterValue(ServiceConstants.COMPONENT);

        StringBuffer buffer = new StringBuffer("/");
        buffer.append(pageName);

        buffer.append(",");
        buffer.append(componentIdPath);

        buffer.append(".");
        buffer.append(stateful != null ? _statefulExtension : _statelessExtension);

        encoding.setServletPath(buffer.toString());

        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.SESSION, null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, null);
    }

    public void decode(ServiceEncoding encoding)
    {
        String servletPath = encoding.getServletPath();

        int dotx = servletPath.lastIndexOf('.');
        if (dotx < 0)
            return;

        String extension = servletPath.substring(dotx + 1);

        if (!(extension.equals(_statefulExtension) || extension.equals(_statelessExtension)))
            return;

        int commax = servletPath.lastIndexOf(',');

        String pageName = servletPath.substring(1, commax);
        String componentIdPath = servletPath.substring(commax + 1, dotx);

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, pageName);
        encoding.setParameterValue(
                ServiceConstants.SESSION,
                extension.equals(_statefulExtension) ? "T" : null);
        encoding.setParameterValue(ServiceConstants.COMPONENT, componentIdPath);
    }

    public void setStatefulExtension(String statefulExtension)
    {
        _statefulExtension = statefulExtension;
    }

    public void setStatelessExtension(String statelessExtension)
    {
        _statelessExtension = statelessExtension;
    }
}