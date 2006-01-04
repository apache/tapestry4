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

import java.util.Iterator;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * No, this class isn't abstract ... this worker locates abstract properties in the base component
 * class and provides a concrete implementation for them in the enhanced class.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AbstractPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Location location = spec.getLocation();

        Iterator i = op.findUnclaimedAbstractProperties().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            try
            {
                createProperty(op, name, location);
            }
            catch (Exception ex)
            {
                _errorLog.error(
                        EnhanceMessages.errorAddingProperty(name, op.getBaseClass(), ex),
                        location,
                        ex);
            }
        }
    }

    private void createProperty(EnhancementOperation op, String name, Location location)
    {
        // This won't be null because its always for existing properties.

        Class propertyType = op.getPropertyType(name);

        String fieldName = "_$" + name;
        String defaultFieldName = fieldName + "$defaultValue";

        op.addField(fieldName, propertyType);
        op.addField(defaultFieldName, propertyType);

        EnhanceUtils.createSimpleAccessor(op, fieldName, name, propertyType, location);
        EnhanceUtils.createSimpleMutator(op, fieldName, name, propertyType, location);

        // Copy the real attribute into the default attribute inside finish load
        // (allowing a default value to be set inside finishLoad()).

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                defaultFieldName + " = " + fieldName + ";");

        // On page detach, restore the attribute to its default value.

        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                fieldName + " = " + defaultFieldName + ";");

        // This is not all that necessary, but is proper.

        op.claimProperty(name);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}