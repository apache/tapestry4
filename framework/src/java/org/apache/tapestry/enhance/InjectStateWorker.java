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

import java.lang.reflect.Modifier;

import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Worker for injecting application state objects as properties of the component. These properties
 * are read/write and must be "live" (changes are propogated back into the
 * {@link org.apache.tapestry.engine.state.ApplicationStateManager}). They should also cache in a
 * local variable for efficiency, and clear out that variable at the end of the request.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectStateWorker implements InjectEnhancementWorker
{
    private ApplicationStateManager _applicationStateManager;

    public void performEnhancement(EnhancementOperation op, InjectSpecification spec)
    {
        injectState(op, spec.getObject(), spec.getProperty());
    }

    public void injectState(EnhancementOperation op, String objectName, String propertyName)
    {
        Defense.notNull(op, "op");
        Defense.notNull(objectName, "objectName");
        Defense.notNull(propertyName, "propertyName");

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, null);
        String fieldName = "_$" + propertyName;

        op.claimProperty(propertyName);

        op.addField(fieldName, propertyType);

        String managerField = op.addInjectedField(
                "_$applicationStateManager",
                ApplicationStateManager.class,
                _applicationStateManager);

        BodyBuilder builder = new BodyBuilder();

        // Accessor

        builder.begin();
        builder.addln("if ({0} == null)", fieldName);
        builder.addln("  {0} = ({1}) {2}.get(\"{3}\");", new Object[]
        { fieldName, ClassFabUtils.getJavaClassName(propertyType), managerField, objectName });
        builder.addln("return {0};", fieldName);
        builder.end();

        String methodName = op.getAccessorMethodName(propertyName);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        // Mutator

        builder.clear();
        builder.begin();
        builder.addln("{0}.store(\"{1}\", $1);", managerField, objectName);
        builder.addln("{0} = $1;", fieldName);
        builder.end();

        sig = new MethodSignature(void.class, EnhanceUtils.createMutatorMethodName(propertyName),
                new Class[]
                { propertyType }, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        // Extend pageDetached() to clean out the cached field value.

        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                fieldName + " = null;");
    }

    public void setApplicationStateManager(ApplicationStateManager applicationStateManager)
    {
        _applicationStateManager = applicationStateManager;
    }
}