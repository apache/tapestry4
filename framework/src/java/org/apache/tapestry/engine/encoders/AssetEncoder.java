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

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.asset.AssetService;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Encoder for the {@link org.apache.tapestry.asset.AssetService}&nbsp;that uses servlet path info
 * to store the resource digest and the path to the resource.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AssetEncoder implements ServiceEncoder
{
    private String _path;

    public void setPath(String path)
    {
        _path = path;
    }

    public void encode(ServiceEncoding encoding)
    {
        if (!encoding.getParameterValue(ServiceConstants.SERVICE).equals(Tapestry.ASSET_SERVICE))
            return;

        String path = encoding.getParameterValue(AssetService.PATH);
        String digest = encoding.getParameterValue(AssetService.DIGEST);

        // _path ends with a slash, path starts with one.

        String fullPath = _path + digest + path;

        encoding.setServletPath(fullPath);
        encoding.setParameterValue(AssetService.PATH, null);
        encoding.setParameterValue(AssetService.DIGEST, null);
        encoding.setParameterValue(ServiceConstants.SERVICE, null);
    }

    public void decode(ServiceEncoding encoding)
    {
        String fullPath = encoding.getServletPath();

        if (!fullPath.startsWith(_path))
            return;

        String pathInfo = fullPath.substring(_path.length());
        int slashx = pathInfo.indexOf('/');

        encoding.setParameterValue(ServiceConstants.SERVICE, Tapestry.ASSET_SERVICE);
        encoding.setParameterValue(AssetService.DIGEST, pathInfo.substring(0, slashx));
        encoding.setParameterValue(AssetService.PATH, pathInfo.substring(slashx));
    }

}