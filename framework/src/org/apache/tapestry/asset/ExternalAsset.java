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
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  A reference to an external URL.  {@link ExternalAsset}s are not
 *  localizable.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExternalAsset extends AbstractAsset
{
    private String _URL;

    public ExternalAsset(String URL, ILocation location)
    {
    	super(null, location);
    	
        _URL = URL;
    }

    /**
     *  Simply returns the URL of the external asset.
     *
     **/

    public String buildURL(IRequestCycle cycle)
    {
        return _URL;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle)
    {
        URL url;

        try
        {
            url = new URL(_URL);

            return url.openStream();
        }
        catch (Exception ex)
        {
            // MalrformedURLException or IOException

            throw new ApplicationRuntimeException(Tapestry.format("ExternalAsset.resource-missing", _URL), ex);
        }

    }

    public String toString()
    {
        return "ExternalAsset[" + _URL + "]";
    }
}