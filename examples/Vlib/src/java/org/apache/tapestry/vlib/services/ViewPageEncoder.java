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

package org.apache.tapestry.vlib.services;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Specialized encdoder for the {@link org.apache.tapestry.vlib.pages.ViewBook} and
 * {@link org.apache.tapestry.vlib.pages.ViewPerson} pages.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ViewPageEncoder implements ServiceEncoder
{
    private String _pageName;

    private String _url;

    public void encode(ServiceEncoding encoding)
    {
        if (!isExternalService(encoding))
            return;

        String pageName = encoding.getParameterValue(ServiceConstants.PAGE);

        if (!pageName.equals(_pageName))
            return;

        StringBuilder builder = new StringBuilder(_url);

        String[] params = encoding.getParameterValues(ServiceConstants.PARAMETER);

        // params will not be null; in fact, pretty sure it will consist
        // of just one element (an integer).

        for (String param : params)
        {
            builder.append("/");
            builder.append(param);
        }

        encoding.setServletPath(builder.toString());

        encoding.setParameterValue(ServiceConstants.SERVICE, null);
        encoding.setParameterValue(ServiceConstants.PAGE, null);
        encoding.setParameterValue(ServiceConstants.PARAMETER, null);
    }

    private boolean isExternalService(ServiceEncoding encoding)
    {
        String service = encoding.getParameterValue(ServiceConstants.SERVICE);

        return service.equals(Tapestry.EXTERNAL_SERVICE);
    }

    public void decode(ServiceEncoding encoding)
    {
        String servletPath = encoding.getServletPath();

        if (!servletPath.equals(_url))
            return;

        // Skip the URL and the slash, end up with a "/" seperated series of
        // strings.

        String pathInfo = encoding.getPathInfo();

        // Skip the leading slash, then split the rest at each slash

        String[] params = TapestryUtils.split(pathInfo.substring(1), '/');

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.EXTERNAL_SERVICE);
        encoding.setParameterValue(ServiceConstants.PAGE, _pageName);
        encoding.setParameterValues(ServiceConstants.PARAMETER, params);
    }

    public void setPageName(String pageName)
    {
        _pageName = pageName;
    }

    public void setUrl(String url)
    {
        _url = url;
    }

}
