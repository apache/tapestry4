//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.resource.ContextResourceLocation;

/**
 *  An asset whose path is relative to the {@link javax.servlet.ServletContext} containing
 *  the application.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ContextAsset extends AbstractAsset implements IAsset
{
    private String _resolvedURL;

    public ContextAsset(ContextResourceLocation resourceLocation, ILocation location)
    {
        super(resourceLocation, location);
    }

    /**
     *  Generates a URL for the client to retrieve the asset.  The context path
     *  is prepended to the asset path, which means that assets deployed inside
     *  web applications will still work (if things are configured properly).
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        if (_resolvedURL == null)
        {
            IEngine engine = cycle.getEngine();
            String contextPath = engine.getContextPath();

            _resolvedURL = contextPath + getResourceLocation().getPath();
        }
        
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
            throw new ApplicationRuntimeException(
                Tapestry.format("ContextAsset.resource-missing", getResourceLocation()),
                ex);
        }
    }
}