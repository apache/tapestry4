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

package org.apache.tapestry.enhance;

import java.util.Iterator;

import org.apache.hivemind.ErrorLog;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects assets as component properties.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
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
                    injectAsset(op, name, propertyName);
                }
                catch (Exception ex)
                {
                    _errorLog.error(EnhanceMessages.errorAddingProperty(propertyName, op
                            .getBaseClass(), ex), as.getLocation(), ex);
                }
            }
        }
    }

    private void injectAsset(EnhancementOperation op, String assetName, String propertyName)
    {
        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, null);

        // TODO: Should be compatible with IAsset.

        op.claimProperty(propertyName);

        String fieldName = "_$" + propertyName;

        op.addField(fieldName, propertyType);

        EnhanceUtils.createSimpleAccessor(op, fieldName, propertyName, propertyType);

        // i.e. _$fred = getAsset("barney");
        
        String code = fieldName + " = getAsset(\"" + assetName + "\");";

        op.extendMethodImplementation(IComponent.class, EnhanceUtils.FINISH_LOAD_SIGNATURE, code);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}