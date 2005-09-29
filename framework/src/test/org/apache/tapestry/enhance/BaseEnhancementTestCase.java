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

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Base class for common utilities when testing enhancement workers.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class BaseEnhancementTestCase extends HiveMindTestCase
{
    public IComponentSpecification newSpec(Location location)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getLocation();
        control.setReturnValue(location);

        return spec;
    }

    protected IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    protected EnhancementOperation newOp()
    {
        return (EnhancementOperation) newMock(EnhancementOperation.class);
    }

    protected void trainGetAccessorMethodName(EnhancementOperation op, String propertyName, String methodName)
    {
        op.getAccessorMethodName(propertyName);
        setReturnValue(op, methodName);
    
    }

    protected void trainAddInjectedField(EnhancementOperation op, String fieldName, Class fieldType, Object injectedValue, String injectedFieldName)
    {
        op.addInjectedField(fieldName, fieldType, injectedValue);
        setReturnValue(op, injectedFieldName);
    
    }

    protected ApplicationStateManager newApplicationStateManager()
    {
        return (ApplicationStateManager) newMock(ApplicationStateManager.class);
    }

    protected void trainGetPropertyType(EnhancementOperation op, String propertyName, Class propertyType)
    {
        op.getPropertyType(propertyName);
        setReturnValue(op, propertyType);
    }
}
