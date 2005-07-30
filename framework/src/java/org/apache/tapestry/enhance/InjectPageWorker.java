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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.IPage;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Injects code to access a named page within the application.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class InjectPageWorker implements InjectEnhancementWorker
{
    public void performEnhancement(EnhancementOperation op, InjectSpecification spec)
    {
        performEnhancement(op, spec.getObject(), spec.getProperty(), spec.getLocation());
    }

    public void performEnhancement(EnhancementOperation op, String pageName, String propertyName,
            Location location)
    {
        Class propertyType = op.getPropertyType(propertyName);

        if (propertyType == null)
            propertyType = Object.class;
        else if (propertyType.isPrimitive())
            throw new ApplicationRuntimeException(EnhanceMessages.wrongTypeForPageInjection(
                    propertyName,
                    propertyType), null, location, null);

        op.claimReadonlyProperty(propertyName);

        MethodSignature sig = new MethodSignature(propertyType, op
                .getAccessorMethodName(propertyName), null, null);

        BodyBuilder builder = new BodyBuilder();

        builder.add("return ");

        // If the property type is not IPage or a superclass of IPage then a cast
        // is needed.

        if (!propertyType.isAssignableFrom(IPage.class))
            builder.add("({0})", propertyType.getName());

        builder.add("getPage().getRequestCycle().getPage(\"{0}\");", pageName);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), location);
    }
}
