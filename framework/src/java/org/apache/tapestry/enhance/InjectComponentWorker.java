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
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Injects components for which the property attribute of the &lt;component&gt; element was
 * specified. This makes it easier to reference a particular component from Java code.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InjectComponentWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getComponentIds().iterator();

        while (i.hasNext())
        {
            String id = (String) i.next();

            IContainedComponent cc = spec.getComponent(id);

            String propertyName = cc.getPropertyName();

            if (propertyName != null)
            {
                try
                {
                    injectComponent(op, id, propertyName);
                }
                catch (Exception ex)
                {
                    _errorLog.error(EnhanceMessages.errorAddingProperty(propertyName, op
                            .getBaseClass(), ex), cc.getLocation(), ex);
                }
            }
        }
    }

    private void injectComponent(EnhancementOperation op, String componentId, String propertyName)
    {
        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, null);

        op.claimProperty(propertyName);

        String fieldName = "_$" + propertyName;

        op.addField(fieldName, propertyType);

        EnhanceUtils.createSimpleAccessor(op, fieldName, propertyName, propertyType);

        // I.e. _$fred = (IComponent) getComponent("fred");

        String code = fieldName + " = (" + ClassFabUtils.getJavaClassName(propertyType)
                + ") getComponent(\"" + componentId + "\");";

        op.extendMethodImplementation(IComponent.class, EnhanceUtils.FINISH_LOAD_SIGNATURE, code);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}