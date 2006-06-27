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

import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Base class for common utilities when testing enhancement workers.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public abstract class BaseEnhancementTestCase extends BaseComponentTestCase
{
    public IComponentSpecification newSpec(Location location)
    {
        IComponentSpecification spec = newSpec();

        expect(spec.getLocation()).andReturn(location);

        return spec;
    }

    protected EnhancementOperation newOp()
    {
        return newMock(EnhancementOperation.class);
    }

    protected void trainAddInjectedField(EnhancementOperation op, String fieldName,
            Class fieldType, Object injectedValue, String injectedFieldName)
    {
        expect(op.addInjectedField(fieldName, fieldType, injectedValue))
        .andReturn(injectedFieldName);

    }

    protected ApplicationStateManager newApplicationStateManager()
    {
        return newMock(ApplicationStateManager.class);
    }

    protected EnhancementOperation newEnhancementOp()
    {
        return newMock(EnhancementOperation.class);
    }

    protected void trainGetAccessorMethodName(EnhancementOperation op, String propertyName,
            String methodName)
    {
        expect(op.getAccessorMethodName(propertyName)).andReturn(methodName);
    }

    protected void trainGetPropertyType(EnhancementOperation op, String propertyName,
            Class propertyType)
    {
        expect(op.getPropertyType(propertyName)).andReturn(propertyType);
    }

    protected void trainGetBaseClass(EnhancementOperation op, Class baseClass)
    {
        expect(op.getBaseClass()).andReturn(baseClass);
    }
}
