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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;

import java.lang.reflect.Modifier;
import java.util.Iterator;

/**
 * Responsible for creating properties for connected parameters.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ParameterPropertyWorker implements EnhancementWorker
{

    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getParameterNames().iterator();
        while(i.hasNext())
        {
            String name = (String) i.next();

            IParameterSpecification ps = spec.getParameter(name);

            try
            {
                performEnhancement(op, name, ps);
            }
            catch (RuntimeException ex)
            {
                _errorLog.error(EnhanceMessages.errorAddingProperty(ps
                        .getPropertyName(), op.getBaseClass(), ex), ps
                        .getLocation(), ex);
            }
        }
    }

    /**
     * Performs the enhancement for a single parameter; this is about to change
     * radically in release 4.0 but for the moment we're emulating 3.0 behavior.
     */

    private void performEnhancement(EnhancementOperation op, String parameterName, IParameterSpecification ps)
    {
        // If the parameter name doesn't match, its because this is an alias
        // for a true parameter; we ignore aliases.

        if (!parameterName.equals(ps.getParameterName())) 
            return;
        
        String propertyName = ps.getPropertyName();
        String specifiedType = ps.getType();
        boolean cache = ps.getCache();
        
        addParameter(op, parameterName, propertyName, specifiedType, cache, ps.getLocation());
    }

    /**
     * Adds a parameter as a (very smart) property.
     * 
     * @param op
     *            the enhancement operation
     * @param parameterName
     *            the name of the parameter (used to access the binding)
     * @param propertyName
     *            the name of the property to create (usually, but not always,
     *            matches the parameterName)
     * @param specifiedType
     *            the type declared in the DTD (only 3.0 DTD supports this), may
     *            be null (always null for 4.0 DTD)
     * @param cache
     *            if true, then the value should be cached while the component
     *            renders; false (a much less common case) means that every
     *            access will work through binding object.
     * @param location
     *            Used for reporting line-precise errors in binding resolution / setting / etc..
     */

    public void addParameter(EnhancementOperation op, String parameterName,
            String propertyName, String specifiedType, boolean cache,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(parameterName, "parameterName");
        Defense.notNull(propertyName, "propertyName");

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, specifiedType);
        
        // 3.0 would allow connected parameter properties to be fully
        // implemented
        // in the component class. This is not supported in 4.0 and an existing
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

        buildAccessor(op, parameterName, propertyName, propertyType, fieldName,
                defaultFieldName, cachedFieldName, cache, location);

        buildMutator(op, parameterName, propertyName, propertyType, fieldName,
                defaultFieldName, cachedFieldName, location);

        extendCleanupAfterRender(op, parameterName, propertyName, propertyType,
                fieldName, defaultFieldName, cachedFieldName);
    }

    private void extendCleanupAfterRender(EnhancementOperation op,
            String parameterName, String propertyName, Class propertyType,
            String fieldName, String defaultFieldName, String cachedFieldName)
    {
        BodyBuilder cleanupBody = new BodyBuilder();

        // Cached is only set when the field is updated in the accessor or
        // mutator.
        // After rendering, we want to clear the cached value and cached flag
        // unless the binding is invariant, in which case it can stick around
        // for some future render.

        String bindingName = propertyName + "Binding";

        addBindingReference(cleanupBody, bindingName, parameterName);

        cleanupBody.addln("if ({0} && ! {1}.isInvariant())", cachedFieldName,
                bindingName);
        cleanupBody.begin();
        cleanupBody.addln("{0} = false;", cachedFieldName);
        cleanupBody.addln("{0} = {1};", fieldName, defaultFieldName);
        cleanupBody.end();

        op.extendMethodImplementation(IComponent.class,
                EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE, cleanupBody.toString());
    }

    private void addBindingReference(BodyBuilder builder,
            String localVariableName, String parameterName)
    {
        builder.addln("{0} {1} = getBinding(\"{2}\");",
                      IBinding.class.getName(), localVariableName, parameterName);
    }

    private void buildMutator(EnhancementOperation op, String parameterName,
            String propertyName, Class propertyType, String fieldName,
            String defaultFieldName, String cachedFieldName, Location location)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();

        // The mutator method may be invoked from finishLoad(), in which
        // case it changes the default value for the parameter property, if the
        // parameter
        // is not bound.

        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("{0} = $1;", defaultFieldName);
        builder.addln("return;");
        builder.end();

        // In the normal state, we update the binding first - and it's an error
        // if the parameter is not bound.

        addBindingReference(builder, "binding", parameterName);

        builder.addln("if (binding == null)");
        builder.addln("  throw new {0}(\"Parameter ''{1}'' is not bound and can not be updated.\");",
                        ApplicationRuntimeException.class.getName(), parameterName);

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

        op.addMethod(Modifier.PUBLIC, 
                new MethodSignature(void.class, mutatorMethodName, new Class[] { propertyType }, null), 
                builder.toString(), location);
    }

    // Package private for testing

    void buildAccessor(EnhancementOperation op, String parameterName,
            String propertyName, Class propertyType, String fieldName,
            String defaultFieldName, String cachedFieldName, boolean cache,
            Location location)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();

        builder.addln("if ({0}) return {1};", cachedFieldName, fieldName);

        addBindingReference(builder, "binding", parameterName);

        builder.addln("if (binding == null) return {0};", defaultFieldName);

        String javaTypeName = ClassFabUtils.getJavaClassName(propertyType);

        builder.addln("{0} result = {1};", javaTypeName, EnhanceUtils.createUnwrapExpression(op, "binding", propertyType));

        // Values read via the binding are cached during the render of
        // the component (if the parameter defines cache to be true, which
        // is the default), or any time the binding is invariant
        // (such as most bindings besides ExpressionBinding.

        String expression = cache ? "isRendering() || binding.isInvariant()"
                : "binding.isInvariant()";

        builder.addln("if ({0})", expression);
        builder.begin();
        builder.addln("{0} = result;", fieldName);
        builder.addln("{0} = true;", cachedFieldName);
        builder.end();

        builder.addln("return result;");

        builder.end();

        String accessorMethodName = op.getAccessorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(propertyType,
                accessorMethodName, null, null), builder.toString(), location);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}
