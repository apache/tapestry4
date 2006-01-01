// Copyright 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects assets as component properties.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectAssetWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getAssetNames().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            IAssetSpecification as = spec.getAsset(name);

            String propertyName = as.getPropertyName();

            if (propertyName != null)
            {
                try
                {
                    injectAsset(op, name, propertyName, as.getLocation());
                }
                catch (Exception ex)
                {
                    _errorLog.error(EnhanceMessages.errorAddingProperty(propertyName, op
                            .getBaseClass(), ex), as.getLocation(), ex);
                }
            }
        }
    }

    public void injectAsset(EnhancementOperation op, String assetName, String propertyName,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(assetName, "assetName");
        Defense.notNull(propertyName, "propertyName");

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, null);

        op.claimReadonlyProperty(propertyName);

        if (!propertyType.isAssignableFrom(IAsset.class))
            throw new ApplicationRuntimeException(EnhanceMessages.incompatiblePropertyType(
                    propertyName,
                    propertyType,
                    IAsset.class));

        String methodName = op.getAccessorMethodName(propertyName);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        String code = "return getAsset(\"" + assetName + "\");";

        op.addMethod(Modifier.PUBLIC, sig, code, location);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}