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
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectStateSpecification;

/**
 * Worker for injecting application state objects as properties of the component. These properties
 * are read/write and must be "live" (changes are propogated back into the
 * {@link org.apache.tapestry.engine.state.ApplicationStateManager}). They should also cache in a
 * local variable for efficiency, and clear out that variable at the end of the request.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InjectStateWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    private ApplicationStateManager _applicationStateManager;

    public void performEnhancement(EnhancementOperation op)
    {
        IComponentSpecification spec = op.getSpecification();
        List injects = spec.getInjectStateSpecifications();

        if (injects.isEmpty())
            return;

        // TODO: The EnhancementOperation should have a way of assigning non-conflicting
        // attribute names. What if someone injects a property named "applicationStateManager"
        // as well?

        op.addField(
                "_$applicationStateManager",
                ApplicationStateManager.class,
                _applicationStateManager);

        Iterator i = injects.iterator();
        while (i.hasNext())
        {
            InjectStateSpecification iss = (InjectStateSpecification) i.next();

            try
            {
                injectState(op, iss.getProperty(), iss.getObjectName());
            }
            catch (Exception ex)
            {
                _errorLog.error(EnhanceMessages.errorAddingProperty(iss.getProperty(), op
                        .getBaseClass(), ex), iss.getLocation(), ex);
            }
        }
    }

    private void injectState(EnhancementOperation op, String propertyName, String objectName)
    {
        Class propertyType =
            EnhanceUtils.extractPropertyType(op, propertyName, null);
        String fieldName = "_$" + propertyName;

        op.claimProperty(propertyName);

        op.addField(fieldName, propertyType);

        BodyBuilder builder = new BodyBuilder();

        // Accessor

        builder.begin();
        builder.addln("if ({0} == null)", fieldName);
        builder.addln(
                "  {0} = ({1}) _$applicationStateManager.get(\"{2}\");",
                fieldName,
                ClassFabUtils.getJavaClassName(propertyType),
                objectName);
        builder.addln("return {0};", fieldName);
        builder.end();

        String methodName = op.getAccessorMethodName(propertyName);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        // Mutator

        builder.clear();
        builder.begin();
        builder.addln("_$applicationStateManager.store(\"{0}\", $1);", objectName);
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

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}