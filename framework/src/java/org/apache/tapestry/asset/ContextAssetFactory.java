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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.web.WebContext;
import org.apache.tapestry.web.WebContextResource;

/**
 * For the moment, all "context:" prefixed asset paths are interpreted relative to the web context
 * (the web application's root folder).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ContextAssetFactory implements AssetFactory
{
    private WebContext _context;

    private String _contextPath;

    private Resource _servletRoot;

    private ClassResolver _classResolver;

    private IEngineService _assetService;

    public void initializeService()
    {
        _servletRoot = new WebContextResource(_context, "/");
    }

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        Resource assetResource = _servletRoot.getRelativeResource(path);

        // Here's the thing; In Tapestry 3.0 and earlier, you could specify
        // library path like /org/apache/tapestry/contrib/Contrib.library. In the new scheme
        // of things, that should be "classpath:/org/apache/tapestry/contrib/Contrib.library".
        // But to keep a lot of things from breaking, we'll kludgely allow that here.

        if (assetResource.getResourceURL() == null && path.startsWith("/"))
        {
            ClasspathResource resource = new ClasspathResource(_classResolver, path);
            return new PrivateAsset(resource, _assetService, location);
        }

        Resource localized = assetResource.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, _servletRoot),
                    location, null);

        return new ContextAsset(_contextPath, localized, location);
    }

    public IAsset createAsset(Resource resource, Location location)
    {
        return new ContextAsset(_contextPath, resource, location);
    }

    public void setContext(WebContext context)
    {
        _context = context;
    }

    public void setContextPath(String contextPath)
    {
        _contextPath = contextPath;
    }

    public void setAssetService(IEngineService assetService)
    {
        _assetService = assetService;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}