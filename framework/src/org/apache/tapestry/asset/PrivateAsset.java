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
import org.apache.tapestry.IEngine;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.resource.ClasspathResourceLocation;

/**
 *  An implementation of {@link org.apache.tapestry.IAsset} for localizable assets within
 *  the JVM's classpath.
 *
 *  <p>The localization code here is largely cut-and-paste from
 *  {@link ContextAsset}.
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class PrivateAsset extends AbstractAsset
{

    private AssetExternalizer _externalizer;

    public PrivateAsset(ClasspathResourceLocation resourceLocation, ILocation location)
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

        IEngine engine = cycle.getEngine();
        
        URL resourceURL = engine.getResourceResolver().getResource(path);
        String checksum = engine.getResourceChecksumSource().getChecksum(resourceURL);
        
        String[] parameters = new String[] { path, checksum };

        AssetService service = (AssetService) engine.getService(Tapestry.ASSET_SERVICE);
        ILink link = service.getLink(cycle, null, parameters);

        return link.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        IResourceLocation location = getResourceLocation();

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