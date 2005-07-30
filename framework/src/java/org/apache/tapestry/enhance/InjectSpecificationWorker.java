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

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects the component's specification as the
 * {@link org.apache.tapestry.IComponent#getSpecification() specification}property.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectSpecificationWorker implements EnhancementWorker
{
    public static final String SPECIFICATION_PROPERTY_NAME = "specification";

    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        try
        {
            injectSpecification(op, spec);
        }
        catch (Exception ex)
        {
            _errorLog.error(EnhanceMessages.errorAddingProperty(SPECIFICATION_PROPERTY_NAME, op
                    .getBaseClass(), ex), spec.getLocation(), ex);
        }
    }

    public void injectSpecification(EnhancementOperation op, IComponentSpecification spec)
    {
        Defense.notNull(op, "op");
        Defense.notNull(spec, "spec");

        op.claimReadonlyProperty(SPECIFICATION_PROPERTY_NAME);

        String fieldName = op.addInjectedField(
                "_$" + SPECIFICATION_PROPERTY_NAME,
                IComponentSpecification.class,
                spec);

        EnhanceUtils.createSimpleAccessor(
                op,
                fieldName,
                SPECIFICATION_PROPERTY_NAME,
                IComponentSpecification.class,
                spec.getLocation());
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}