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
import java.util.Iterator;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * Responsible for adding properties to a class corresponding to specified properties in the
 * component's specification.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class SpecifiedPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    private BindingSource _bindingSource;

    /**
     * Iterates over the specified properties, creating an enhanced property for each (a field, an
     * accessor, a mutator). Persistent properties will invoke
     * {@link org.apache.tapestry.Tapestry#fireObservedChange(IComponent, String, Object)}in thier
     * mutator.
     */

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getPropertySpecificationNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            IPropertySpecification ps = spec.getPropertySpecification(name);

            try
            {
                performEnhancement(op, ps);
            }
            catch (RuntimeException ex)
            {
                _errorLog.error(
                        EnhanceMessages.errorAddingProperty(name, op.getBaseClass(), ex),
                        ps.getLocation(),
                        ex);
            }
        }
    }

    private void performEnhancement(EnhancementOperation op, IPropertySpecification ps)
    {
        Defense.notNull(ps, "ps");

        String propertyName = ps.getName();

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, ps.getType());

        op.claimProperty(propertyName);

        String field = "_$" + propertyName;

        op.addField(field, propertyType);

        // Release 3.0 would squack a bit about overriding non-abstract methods
        // if they exist. 4.0 is less picky ... it blindly adds new methods, possibly
        // overwriting methods in the base component class.

        EnhanceUtils.createSimpleAccessor(op, field, propertyName, propertyType);

        addMutator(op, propertyName, propertyType, field, ps.isPersistent());

        String initialValue = ps.getInitialValue();

        if (initialValue == null)
            addReinitializer(op, propertyType, field);
        else
            addInitialValue(op, propertyName, propertyType, field, initialValue, ps.getLocation());
    }

    private void addReinitializer(EnhancementOperation op, Class propertyType, String fieldName)
    {
        String defaultFieldName = fieldName + "$default";

        op.addField(defaultFieldName, propertyType);

        // On finishLoad(), store the current value into the default field.

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                defaultFieldName + " = " + fieldName + ";");

        // On pageDetach(), restore the attribute to its default value.

        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                fieldName + " = " + defaultFieldName + ";");
    }

    private void addInitialValue(EnhancementOperation op, String propertyName, Class propertyType,
            String fieldName, String initialValue, Location location)
    {
        String description = EnhanceMessages.initialValueForProperty(propertyName);

        InitialValueBindingCreator creator = new InitialValueBindingCreator(_bindingSource,
                description, initialValue, location);

        String creatorField = op.addInjectedField(fieldName + "$initialValueBindingCreator", creator);

        String bindingField = fieldName + "$initialValueBinding";
        op.addField(bindingField, IBinding.class);

        BodyBuilder builder = new BodyBuilder();

        builder.addln("{0} = {1}.createBinding(this);", bindingField, creatorField);

        op.extendMethodImplementation(IComponent.class, EnhanceUtils.FINISH_LOAD_SIGNATURE, builder
                .toString());

        builder.clear();

        builder.addln("{0} = {1};", fieldName, EnhanceUtils.createUnwrapExpression(
                op,
                bindingField,
                propertyType));

        String code = builder.toString();

        // In finishLoad() and pageDetach(), de-reference the binding to get the value
        // for the property.

        op.extendMethodImplementation(IComponent.class, EnhanceUtils.FINISH_LOAD_SIGNATURE, code);
        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                code);

    }

    private void addMutator(EnhancementOperation op, String propertyName, Class propertyType,
            String fieldName, boolean persistent)
    {
        String methodName = EnhanceUtils.createMutatorMethodName(propertyName);

        BodyBuilder body = new BodyBuilder();

        body.begin();

        if (persistent)
        {
            body.add("org.apache.tapestry.Tapestry#fireObservedChange(this, ");
            body.addQuoted(propertyName);
            body.addln(", ($w) $1);");
        }

        body.addln(fieldName + " = $1;");

        body.end();

        MethodSignature sig = new MethodSignature(void.class, methodName, new Class[]
        { propertyType }, null);

        op.addMethod(Modifier.PUBLIC, sig, body.toString());
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setBindingSource(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }
}