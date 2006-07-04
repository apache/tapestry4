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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public abstract class BaseAnnotationTestCase extends HiveMindTestCase
{

    protected Method findMethod(Class clazz, String name)
    {
        for (Method m : clazz.getMethods())
        {
            if (m.getName().equals(name))
                return m;
        }

        throw new IllegalArgumentException("No method " + name + " in " + clazz);
    }

    protected IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    protected EnhancementOperation newOp()
    {
        return (EnhancementOperation) newMock(EnhancementOperation.class);
    }

    protected Resource newResource(Class clazz)
    {
        return new ClasspathResource(getClassResolver(), clazz.getName().replace('.', '/'));
    }

    protected ErrorLog newLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    protected Location newMethodLocation(Class baseClass, Method m, Class annotationClass)
    {
        Resource classResource = newResource(baseClass);
    
        return AnnotationUtils.buildLocationForAnnotation(
                m,
                m.getAnnotation(annotationClass),
                classResource);
    }

}
