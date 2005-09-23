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

    public IAsset createAsset(Resource baseResource, String path, Locale locale, Location location)
    {
        Resource asset = baseResource.getRelativeResource(path);
        Resource localized = asset.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingAsset(path, baseResource),
                    location, null);

        return createAsset(localized, location);
    }

    public IAsset createAbsoluteAsset(String path, Locale locale, Location location)
    {
        Resource base = new ClasspathResource(_classResolver, path);
        Resource localized = base.getLocalization(locale);

        if (localized == null)
            throw new ApplicationRuntimeException(AssetMessages.missingClasspathResource(path),
                    location, null);

        return createAsset(localized, location);
    }

    public IAsset createAsset(Resource resource, Location location)
    {
        ClasspathResource cr = (ClasspathResource) resource;

        return new PrivateAsset(cr, _assetService, location);
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