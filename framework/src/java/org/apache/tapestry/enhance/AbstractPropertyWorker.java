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

import java.util.Iterator;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;

/**
 * No, this class isn't abstract ... this worker locates abstract properties in the base component
 * class and provides a concrete implementation for them in the enhanced class.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class AbstractPropertyWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op)
    {
        Iterator i = op.findUnclaimedAbstractProperties().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            try
            {
                createProperty(op, name);
            }
            catch (Exception ex)
            {
                _errorLog.error(
                        EnhanceMessages.errorAddingProperty(name, op.getBaseClass(), ex),
                        op.getSpecification().getLocation(),
                        ex);
            }
        }
    }

    private void createProperty(EnhancementOperation op, String name)
    {
        // This won't be null because its always for existing properties.

        Class propertyType = op.getPropertyType(name);

        String fieldName = "_$" + name;
        String defaultFieldName = fieldName + "$defaultValue";

        op.addField(fieldName, propertyType);
        op.addField(defaultFieldName, propertyType);

        EnhanceUtils.createSimpleAccessor(op, fieldName, name, propertyType);
        EnhanceUtils.createSimpleMutator(op, fieldName, name, propertyType);

        BodyBuilder finishLoadBody = op.getBodyBuilderForMethod(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE);

        // Inside finish load, "snapshot" the value of the real field

        finishLoadBody.addln("{0} = {1};", defaultFieldName, fieldName);

        // When detaching the page, overwrite the field value with
        // the snapshot.

        BodyBuilder pageDetachedBody = op.getBodyBuilderForMethod(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE);

        pageDetachedBody.addln("{0} = {1};", fieldName, defaultFieldName);

        // This is not all that necessary, but is proper.

        op.claimProperty(name);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}