// Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Defense;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * Responsible for creating properties for connected parameters.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ParameterPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op)
    {
        Defense.notNull(op, "op");

        IComponentSpecification spec = op.getSpecification();

        Iterator i = spec.getParameterNames().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            IParameterSpecification ps = spec.getParameter(name);

            try
            {
                performEnhancement(op, name, ps);
            }
            catch (RuntimeException ex)
            {
                _errorLog.error(EnhanceMessages.errorAddingProperty(ps.getPropertyName(), op
                        .getBaseClass(), ex), ps.getLocation(), ex);
            }
        }
    }

    /**
     * Performs the enhancement for a single parameter; this is about to change radically in release
     * 3.1 but for the moment we're emulating 3.0 behavior.
     */

    private void performEnhancement(EnhancementOperation op, String parameterName,
            IParameterSpecification ps)
    {
        if (ps.getDirection() == Direction.CUSTOM)
            return;

        String propertyName = ps.getPropertyName();

        Class propertyType = op.convertTypeName(ps.getType());

        op.validateProperty(propertyName, propertyType);

        // 3.0 would allow connected parameter properties to be fully implemented
        // in the component class. This is not supported in 3.1 and an existing
        // property will be overwritten in the subclass.

        op.claimProperty(propertyName);

        // 3.0 used to support a property for the binding itself. That's
        // no longer the case.

        if (ps.getDirection() == Direction.AUTO)
        {
            createAutoParameterProperty(op, parameterName, ps, propertyType);
            return;
        }

        createSimpleParameterProperty(op, propertyName, propertyType);
    }

    private void createSimpleParameterProperty(EnhancementOperation op, String propertyName,
            Class propertyType)
    {
        String fieldName = "_$" + propertyName;

        op.addField(fieldName, propertyType);

        createSimpleAccessor(op, fieldName, propertyName, propertyType);
        createSimpleMutator(op, fieldName, propertyName, propertyType);
    }

    private void createSimpleAccessor(EnhancementOperation op, String fieldName,
            String propertyName, Class propertyType)
    {
        String methodName = op.getAccessorMethodName(propertyName);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(propertyType, methodName, null, null),
                "return " + fieldName + ";");
    }

    private void createSimpleMutator(EnhancementOperation op, String fieldName,
            String propertyName, Class propertyType)
    {
        String methodName = EnhanceUtils.createMutatorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, methodName, new Class[]
        { propertyType }, null), fieldName + " = $1;");
    }

    private void createAutoParameterProperty(EnhancementOperation op, String parameterName,
            IParameterSpecification ps, Class propertyType)
    {
        String propertyName = ps.getPropertyName();

        // This restriction will go away shortly ...

        if (!ps.isRequired() && ps.getDefaultValue() == null)
            throw new ApplicationRuntimeException(EnhanceMessages.autoMustBeRequired(propertyName),
                    ps.getLocation(), null);

        createAutoAccessor(op, parameterName, propertyName, propertyType);
        createAutoMutator(op, parameterName, propertyName, propertyType);
    }

    private void createAutoAccessor(EnhancementOperation op, String parameterName,
            String propertyName, Class propertyType)
    {
        String methodName = op.getAccessorMethodName(propertyName);
        String classReference = op.getClassReference(propertyType);

        BodyBuilder b = new BodyBuilder();

        b.begin();
        b.addln("org.apache.tapestry.IBinding binding = getBinding(\"{0}\");", parameterName);
        b.addln("return ($r)binding.getObject(\"{0}\", {1});", parameterName, classReference);
        b.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(propertyType, methodName, null, null), b
                .toString());
    }

    private void createAutoMutator(EnhancementOperation op, String parameterName,
            String propertyName, Class propertyType)
    {
        String methodName = EnhanceUtils.createMutatorMethodName(propertyName);

        BodyBuilder b = new BodyBuilder();

        b.begin();
        b.addln("org.apache.tapestry.IBinding binding = getBinding(\"{0}\");", parameterName);
        b.addln("binding.setObject(($w) $1);");
        b.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, methodName, new Class[]
        { propertyType }, null), b.toString());
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}