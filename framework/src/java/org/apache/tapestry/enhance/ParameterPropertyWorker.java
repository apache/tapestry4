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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 * Responsible for creating properties for connected parameters.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ParameterPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    /**
     * Used to unwrap primitive types inside the accessor method. In each case, the binding is in a
     * variable named "binding", and {0} will be the actual type of the property. The Map is keyed
     * on the primtive type.
     */

    private Map _unwrappers = new HashMap();

    {
        _unwrappers.put(boolean.class, "((Boolean) binding.getObject({0})).booleanValue();");
        _unwrappers.put(byte.class, "((Byte) binding.getObject({0})).byteValue();");
        _unwrappers.put(char.class, "((Character) binding.getObject({0})).charValue();");
        _unwrappers.put(short.class, "((Short) binding.getObject({0})).shortValue();");
        _unwrappers.put(int.class, "((Integer) binding.getObject({0})).intValue();");
        _unwrappers.put(long.class, "((Long) binding.getObject({0})).longValue();");
        _unwrappers.put(float.class, "((Float) binding.getObject({0})).floatValue();");
        _unwrappers.put(double.class, "((Double) binding.getObject({0})).doubleValue();");
    }

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
        String propertyName = ps.getPropertyName();

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, ps.getType());

        // 3.0 would allow connected parameter properties to be fully implemented
        // in the component class. This is not supported in 3.1 and an existing
        // property will be overwritten in the subclass.

        op.claimProperty(propertyName);

        // 3.0 used to support a property for the binding itself. That's
        // no longer the case.

        String fieldName = "_$" + propertyName;
        String defaultFieldName = fieldName + "$Default";
        String cachedFieldName = fieldName + "$Cached";

        op.addField(fieldName, propertyType);
        op.addField(defaultFieldName, propertyType);
        op.addField(cachedFieldName, boolean.class);

        buildAccessor(
                op,
                parameterName,
                propertyName,
                propertyType,
                fieldName,
                defaultFieldName,
                cachedFieldName);

        buildMutator(
                op,
                parameterName,
                propertyName,
                propertyType,
                fieldName,
                defaultFieldName,
                cachedFieldName);

        extendCleanupAfterRender(
                op,
                parameterName,
                propertyName,
                propertyType,
                fieldName,
                defaultFieldName,
                cachedFieldName);
    }

    private void extendCleanupAfterRender(EnhancementOperation op, String parameterName,
            String propertyName, Class propertyType, String fieldName, String defaultFieldName,
            String cachedFieldName)
    {
        BodyBuilder cleanupBody = op.getBodyBuilderForMethod(
                IComponent.class,
                EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE);

        // Cached is only set when the field is updated in the accessor or mutator.
        // After rendering, we want to clear the cached value and cached flag
        // unless the binding is invariant, in which case it can stick around
        // for some future render.

        String bindingName = propertyName + "Binding";

        addBindingReference(cleanupBody, bindingName, parameterName);

        cleanupBody.addln("if ({0} && ! {1}.isInvariant())", cachedFieldName, bindingName);
        cleanupBody.begin();
        cleanupBody.addln("{0} = false;", cachedFieldName);
        cleanupBody.addln("{0} = {1};", fieldName, defaultFieldName);
        cleanupBody.end();
    }

    private void addBindingReference(BodyBuilder builder, String localVariableName,
            String parameterName)
    {
        builder.addln(
                "{0} {1} = getBinding(\"{2}\");",
                IBinding.class.getName(),
                localVariableName,
                parameterName);
    }

    private void buildMutator(EnhancementOperation op, String parameterName, String propertyName,
            Class propertyType, String fieldName, String defaultFieldName, String cachedFieldName)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();

        // The mutator method may be invoked from finishLoad(), in which
        // case it changes the default value for the parameter property, if the parameter
        // is not bound.

        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("{0} = $1;", defaultFieldName);
        builder.addln("return;");
        builder.end();

        // In the normal state, we update the binding firstm, and it's an error
        // if the parameter is not bound.

        addBindingReference(builder, "binding", parameterName);

        builder.addln("if (binding == null)");
        builder.addln(
                "  throw new {0}(\"Parameter ''{1}'' is not bound and can not be updated.\");",
                ApplicationRuntimeException.class.getName(),
                parameterName);

        // Always updated the binding first (which may fail with an exception).

        builder.addln("binding.setObject(($w) $1);");

        // While rendering, we store the updated value for fast
        // access again (while the component is still rendering).
        // The property value will be reset to default by cleanupAfterRender().

        builder.addln("if (isRendering())");
        builder.begin();
        builder.addln("{0} = $1;", fieldName);
        builder.addln("{0} = true;", cachedFieldName);
        builder.end();

        builder.end();

        String mutatorMethodName = EnhanceUtils.createMutatorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, mutatorMethodName,
                new Class[]
                { propertyType }, null), builder.toString());
    }

    // Package private for testing

    void buildAccessor(EnhancementOperation op, String parameterName, String propertyName,
            Class propertyType, String fieldName, String defaultFieldName, String cachedFieldName)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();

        builder.addln("if ({0}) return {1};", cachedFieldName, fieldName);

        addBindingReference(builder, "binding", parameterName);

        builder.addln("if (binding == null) return {0};", defaultFieldName);

        String propertyTypeRef = op.getClassReference(propertyType);

        String javaTypeName = ClassFabUtils.getJavaClassName(propertyType);

        builder.add("{0} result = ", javaTypeName);

        String unwrapper = (String) _unwrappers.get(propertyType);

        if (unwrapper == null)
            builder.addln("({0}) binding.getObject({1});", javaTypeName, propertyTypeRef);
        else
            builder.addln(unwrapper, propertyTypeRef);

        // Values read via the binding are cached during the render of
        // the component, or when the binding is invariant
        // (such as a StringBinding or MessageBinding).

        builder.addln("if (isRendering() || binding.isInvariant())");
        builder.begin();
        builder.addln("{0} = result;", fieldName);
        builder.addln("{0} = true;", cachedFieldName);
        builder.end();

        builder.addln("return result;");

        builder.end();

        String accessorMethodName = op.getAccessorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(propertyType, accessorMethodName, null,
                null), builder.toString());
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}