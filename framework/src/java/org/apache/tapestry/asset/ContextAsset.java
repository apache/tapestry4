// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.asset;

import java.io.InputStream;
import java.net.URL;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.ContextResource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 * An asset whose path is relative to the {@link javax.servlet.ServletContext}containing the
 * application.
 * 
 * @author Howard Lewis Ship
 */

public class ContextAsset extends AbstractAsset implements IAsset
{
    private String _contextPath;

    private String _resolvedURL;

    public ContextAsset(String contextPath, ContextResource resourceLocation, Location location)
    {
        super(resourceLocation, location);

        Defense.notNull(contextPath, "contextPath");

        _contextPath = contextPath;
    }

    /**
     * Generates a URL for the client to retrieve the asset. The context path is prepended to the
     * asset path, which means that assets deployed inside web applications will still work (if
     * things are configured properly).
     */

    public String buildURL(IRequestCycle cycle)
    {
        if (_resolvedURL == null)
            _resolvedURL = _contextPath + getResourceLocation().getPath();

        return _resolvedURL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        try
        {
            URL url = getResourceLocation().getResourceURL();

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "ContextAsset.resource-missing",
                    getResourceLocation()), ex);
        }
    }
}