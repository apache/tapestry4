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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.AssetSpecification;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Uses the {@link org.apache.tapestry.annotations.Asset} annotation to create a new
 * {@link org.apache.tapestry.spec.IAssetSpecification} which is then added to the
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class AssetAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        Asset asset = method.getAnnotation(Asset.class);
        String propertyName = AnnotationUtils.getPropertyName(method);

        IAssetSpecification as = new AssetSpecification();
        as.setPath(asset.value());
        as.setPropertyName(propertyName);

        // Very important for assets, as they need a location (really, the Resource
        // of a location) to figure out what kind of asset they are.

        Resource specResource = spec.getSpecificationLocation();
        Location assetLocation = new AnnotationLocation(specResource, location.toString());

        as.setLocation(assetLocation);

        spec.addAsset(propertyName, as);
    }

}
