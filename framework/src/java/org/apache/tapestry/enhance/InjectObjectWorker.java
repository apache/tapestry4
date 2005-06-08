// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Implementation for injection type "object" (the default). Adds read-only properties to the
 * enhanced class that contain objects injected from HiveMind.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectObjectWorker implements InjectEnhancementWorker
{
    private InjectedValueProvider _provider;

    public void performEnhancement(EnhancementOperation op, InjectSpecification is)
    {
        String name = is.getProperty();
        String objectReference = is.getObject();
        Location location = is.getLocation();

        injectObject(op, name, objectReference, location);
    }

    public void injectObject(EnhancementOperation op, String name, String objectReference,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(name, "name");
        Defense.notNull(objectReference, "objectReference");

        Class propertyType = op.getPropertyType(name);
        if (propertyType == null)
            propertyType = Object.class;

        String fieldName = "_$" + name;

        op.claimProperty(name);

        Object injectedValue = _provider.obtainValue(objectReference, location);

        if (injectedValue == null)
            throw new ApplicationRuntimeException(EnhanceMessages
                    .locatedValueIsNull(objectReference), location, null);

        if (!propertyType.isAssignableFrom(injectedValue.getClass()))
            throw new ApplicationRuntimeException(EnhanceMessages.incompatibleInjectType(
                    objectReference,
                    injectedValue,
                    propertyType), location, null);

        op.addInjectedField(fieldName, propertyType, injectedValue);

        String methodName = EnhanceUtils.createAccessorMethodName(name);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(propertyType, methodName, null, null),
                "return " + fieldName + ";");
    }

    public void setProvider(InjectedValueProvider provider)
    {
        _provider = provider;
    }
}