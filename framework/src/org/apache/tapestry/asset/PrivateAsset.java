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

import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 *  An implementation of {@link org.apache.tapestry.IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>The localization code here is largely cut-and-paste from
 *  {@link ContextAsset}.
 *
 *  @author Howard Ship
 * 
 **/

public class PrivateAsset extends AbstractAsset
{

    private AssetExternalizer _externalizer;

    public PrivateAsset(ClasspathResource resourceLocation, Location location)
    {
        super(resourceLocation, location);
    }

    /**
     *  Gets the localized version of the resource.  Build
     *  the URL for the resource.  If possible, the application's
     *  {@link AssetExternalizer} is located, to copy the resource to
     *  a directory visible to the web server.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        if (_externalizer == null)
            _externalizer = AssetExternalizer.get(cycle);

        String path = getResourceLocation().getPath();

        String externalURL = _externalizer.getURL(path);

        if (externalURL != null)
            return externalURL;

        // Otherwise, the service is responsible for dynamically retrieving the
        // resource.

        String[] parameters = new String[] { path };

        IEngineService service = cycle.getEngine().getService(Tapestry.ASSET_SERVICE);

        ILink link = service.getLink(cycle, null, parameters);

        return link.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        Resource location = getResourceLocation();

        try
        {
            URL url = location.getResourceURL();

            return url.openStream();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("PrivateAsset.resource-missing", location),
                ex);
        }
    }

}