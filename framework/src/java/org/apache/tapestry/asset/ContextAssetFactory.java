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

package org.apache.tapestry.asset;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.IAsset;

/**
 * For the moment, all "context:" prefixed asset paths are interpreted relative to the servlet
 * context (the web application's root folder).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ContextAssetFactory implements AssetFactory
{
    private ServletContext _servletContext;

    private ContextResource _servletRoot;

    public void initializeService()
    {
        _servletRoot = new ContextResource(_servletContext, "/");
    }

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        Resource assetResource = _servletRoot.getRelativeResource(path);
        Resource localized = assetResource.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, _servletRoot),
                    location, null);

        return new ContextAsset((ContextResource) localized, location);
    }

    public void setServletContext(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }
}