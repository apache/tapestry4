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

import org.apache.hivemind.Defense;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * Responsible for adding properties to a class corresponding to specified properties in the
 * component's specification.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SpecifiedPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    /**
     * Iterates over the specified properties, creating an enhanced property for each (a field, an
     * accessor, a mutator). Persistent properties will invoke
     * {@link org.apache.tapestry.Tapestry#fireObservedChange(IComponent, String, Object)}in thier
     * mutator.
     */

    public void performEnhancement(EnhancementOperation op)
    {
        Defense.notNull(op, "op");

        IComponentSpecification spec = op.getSpecification();

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
        Class propertyType = op.convertTypeName(ps.getType());

        op.validateProperty(propertyName, propertyType);

        op.claimProperty(propertyName);

        String field = "_$" + propertyName;

        op.addField(field, propertyType);

        // Release 3.0 would squack a bit about overriding non-abstract methods
        // if they exist. 3.1 is less picky ... it blindly adds new methods, possibly
        // overwriting methods in the base component class.

        addAccessor(op, propertyName, propertyType, field);
        addMutator(op, propertyName, propertyType, field, ps.isPersistent());
    }

    private void addAccessor(EnhancementOperation op, String name, Class type, String field)
    {
        String methodName = op.getAccessorMethodName(name);

        MethodSignature sig = new MethodSignature(type, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, "return " + field + ";");
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
}