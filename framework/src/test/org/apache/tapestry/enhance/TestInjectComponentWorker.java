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
import static org.easymock.EasyMock.expectLastCall;

import java.lang.reflect.Modifier;
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectComponentWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectComponentWorker extends BaseComponentTestCase
{
    private IComponentSpecification newSpec(String componentId, String propertyName,
            Location location)
    {
        IContainedComponent cc = new ContainedComponent();
        cc.setPropertyName(propertyName);
        cc.setLocation(location);
        
        IComponentSpecification spec = newSpec();

        expect(spec.getComponentIds()).andReturn(Collections.singletonList(componentId));

        expect(spec.getComponent(componentId)).andReturn(cc);

        return spec;
    }

    public void testNoWork()
    {
        IComponentSpecification spec = newSpec("fred", null, null);
        EnhancementOperation op = newMock(EnhancementOperation.class);

        replay();

        new InjectComponentWorker().performEnhancement(op, spec);

        verify();
    }

    protected EnhancementOperation newEnhancementOperation()
    {
        return (EnhancementOperation) newMock(EnhancementOperation.class);
    }

    protected void trainGetPropertyType(EnhancementOperation op, String propertyName,
            Class propertyType)
    {
        expect(op.getPropertyType(propertyName)).andReturn(propertyType);
    }

    protected void trainGetAccessorMethodName(EnhancementOperation op, String propertyName,
            String methodName)
    {
        expect(op.getAccessorMethodName(propertyName)).andReturn(methodName);
    }

    protected void trainGetClassReference(EnhancementOperation op, Class clazz, String reference)
    {
        expect(op.getClassReference(clazz)).andReturn(reference);
    }

    protected void trainAddInjectedField(EnhancementOperation op, String fieldName,
            Class fieldType, Object fieldValue, String uniqueFieldName)
    {
        expect(op.addInjectedField(fieldName, fieldType, fieldValue)).andReturn(uniqueFieldName);
    }

    public void testSuccess()
    {
        Location l = newLocation();
        IComponentSpecification spec = newSpec("fred", "barney", l);
        EnhancementOperation op = newEnhancementOperation();

        trainGetPropertyType(op, "barney", IComponent.class);

        op.claimReadonlyProperty("barney");

        trainGetClassReference(op, IComponent.class, "_$IComponent$class");
        trainAddInjectedField(op, "_$barney$location", Location.class, l, "_$$location");

        op.addField("_$barney", IComponent.class);

        trainGetAccessorMethodName(op, "barney", "getBarney");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(IComponent.class, "getBarney", null, null),
                "return _$barney;",
                l);

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                "_$barney = (org.apache.tapestry.IComponent) "
                        + "org.apache.tapestry.TapestryUtils#getComponent"
                        + "(this, \"fred\", _$IComponent$class, _$$location);");

        replay();

        new InjectComponentWorker().performEnhancement(op, spec);

        verify();
    }

    public void testFailure()
    {
        Location l = newLocation();
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages.claimedProperty("barney"));
        
        EnhancementOperation op = newMock(EnhancementOperation.class);

        IComponentSpecification spec = newSpec("fred", "barney", l);

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        expect(op.getPropertyType("barney")).andReturn(IComponent.class);

        op.claimReadonlyProperty("barney");
        expectLastCall().andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);

        log.error(EnhanceMessages.errorAddingProperty("barney", BaseComponent.class, ex), l, ex);

        replay();

        InjectComponentWorker w = new InjectComponentWorker();

        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verify();
    }
}