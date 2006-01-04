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
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Injects a boolean property that indicates if a particular application state object already
 * exists. This is useful in situations where you are trying to prevent the creation of the ASO (and
 * thus, prevent the creation of the HttpSession).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectStateFlagWorker implements InjectEnhancementWorker
{
    private ApplicationStateManager _applicationStateManager;

    public void performEnhancement(EnhancementOperation op, InjectSpecification spec)
    {
        injectStateFlag(op, spec.getObject(), spec.getProperty(), spec.getLocation());
    }

    void injectStateFlag(EnhancementOperation op, String objectName, String propertyName,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(objectName, "objectName");
        Defense.notNull(propertyName, "propertyName");

        Class propertyType = op.getPropertyType(propertyName);

        // null means no property at all; it's just in the XML
        // which is ok. Otherwise, make sure it is exactly boolean.

        if (propertyType != null && propertyType != boolean.class)
            throw new ApplicationRuntimeException(EnhanceMessages.mustBeBoolean(propertyName),
                    location, null);

        op.claimReadonlyProperty(propertyName);

        String managerField = op.addInjectedField(
                "_$applicationStateManager",
                ApplicationStateManager.class,
                _applicationStateManager);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.add("return {0}.exists(", managerField);
        builder.addQuoted(objectName);
        builder.addln(");");
        builder.end();

        String methodName = op.getAccessorMethodName(propertyName);

        MethodSignature sig = new MethodSignature(boolean.class, methodName, null, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), location);
    }

    public void setApplicationStateManager(ApplicationStateManager applicationStateManager)
    {
        _applicationStateManager = applicationStateManager;
    }

}
