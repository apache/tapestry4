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
import java.util.Iterator;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects a property that will dynamically access a managed bean.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectBeanWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getBeanNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            IBeanSpecification bs = spec.getBeanSpecification(name);

            String propertyName = bs.getPropertyName();
            if (propertyName != null)
            {
                try
                {
                    injectBean(op, name, propertyName, bs.getLocation());
                }
                catch (Exception ex)
                {
                    _errorLog.error(EnhanceMessages.errorAddingProperty(propertyName, op
                            .getBaseClass(), ex), bs.getLocation(), ex);
                }
            }
        }
    }

    public void injectBean(EnhancementOperation op, String beanName, String propertyName,
            Location location)
    {
        Defense.notNull(op, "op");
        Defense.notNull(beanName, "beanName");
        Defense.notNull(propertyName, "propertyName");

        op.claimReadonlyProperty(propertyName);

        Class propertyType = EnhanceUtils.extractPropertyType(op, propertyName, null);

        String methodName = op.getAccessorMethodName(propertyName);

        MethodSignature sig = new MethodSignature(propertyType, methodName, null, null);

        // i.e.
        // return (foo.bar.Baz) getBeans().getBean("baz");

        op.addMethod(Modifier.PUBLIC, sig, "return ("
                + ClassFabUtils.getJavaClassName(propertyType) + ") getBeans().getBean(\""
                + beanName + "\");", location);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}