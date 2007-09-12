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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.l10n.ResourceLocalizer;
import org.apache.tapestry.spec.IComponentSpecification;

import java.util.Locale;

/**
 * Creates instances of {@link org.apache.tapestry.asset.PrivateAsset}, which are the holders of
 * classpath: resources.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ClasspathAssetFactory implements AssetFactory
{
    private ClassResolver _classResolver;

    private IEngineService _assetService;

    private ResourceLocalizer _localizer;

    private Resource _rootClassPath;

    public boolean assetExists(IComponentSpecification spec, Resource baseResource, String path, Locale locale)
    {
        Resource assetResource = null;
        if (path.startsWith("/")) {

            if (_rootClassPath == null) {
                _rootClassPath = new ClasspathResource(_classResolver, "");
            }

            assetResource = _rootClassPath.getRelativeResource(path);
        } else {
            
            assetResource = baseResource.getRelativeResource(path);
        }

        Resource localized = _localizer.findLocalization(assetResource, locale);

        return localized != null;
    }

    public IAsset createAsset(Resource baseResource, IComponentSpecification spec, String path, Locale locale, Location location)
    {
        Resource asset = baseResource.getRelativeResource(path);
        Resource localized = _localizer.findLocalization(asset, locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, baseResource), location, null);

        return createAsset(localized, location);
    }

    public IAsset createAbsoluteAsset(String path, Locale locale, Location location)
    {
        Resource base = new ClasspathResource(_classResolver, path);
        Resource localized = _localizer.findLocalization(base, locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingClasspathResource(path), location, null);

        return createAsset(localized, location);
    }

    public IAsset createAsset(Resource resource, Location location)
    {
        return new PrivateAsset(resource, _assetService, location);
    }

    public void setAssetService(IEngineService assetService)
    {
        _assetService = assetService;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    public void setLocalizer(ResourceLocalizer localizer)
    {
        _localizer = localizer;
    }
}
