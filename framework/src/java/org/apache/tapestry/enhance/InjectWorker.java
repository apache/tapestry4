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
import java.util.Iterator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Adds read-only properties to the enhanced class that contain data injected from HiveMind.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InjectWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    private InjectedValueProvider _provider;

    public void performEnhancement(EnhancementOperation op)
    {
        IComponentSpecification spec = op.getSpecification();

        Iterator i = spec.getInjectSpecifications().iterator();

        while (i.hasNext())
        {
            InjectSpecification is = (InjectSpecification) i.next();

            try
            {
                performEnhancement(op, is);
            }
            catch (Exception ex)
            {
                _errorLog.error(EnhanceMessages.errorAddingProperty(
                        is.getProperty(),
                        op.getBaseClass(),
                        ex), is.getLocation(), ex);
            }
        }
    }

    private void performEnhancement(EnhancementOperation op, InjectSpecification is)
    {
        String name = is.getProperty();
        String objectReference = is.getObjectReference();

        Class propertyType = op.getPropertyType(name);
        if (propertyType == null)
            propertyType = Object.class;

        String fieldName = "_$" + name;

        op.claimProperty(name);

        Object injectedValue = _provider.obtainValue(objectReference, is.getLocation());

        if (injectedValue == null)
            throw new ApplicationRuntimeException(EnhanceMessages
                    .locatedValueIsNull(objectReference));

        if (!propertyType.isAssignableFrom(injectedValue.getClass()))
            throw new ApplicationRuntimeException(EnhanceMessages.incompatibleInjectType(
                    objectReference,
                    injectedValue,
                    propertyType));

        op.addField(fieldName, propertyType, injectedValue);

        String methodName = EnhanceUtils.createAccessorMethodName(name);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(propertyType, methodName, null, null),
                "return " + fieldName + ";");
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setProvider(InjectedValueProvider provider)
    {
        _provider = provider;
    }
}