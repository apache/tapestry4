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
package org.apache.tapestry.annotations;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.enhance.EnhanceUtils;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;


/**
 * Performs runtime checks on persistent properties to ensure that objects being
 * managed by competing bytecode enhancement libraries (such as Hibernate) aren't
 * proxied.
 */
public class ComponentPropertyProxyWorker implements EnhancementWorker {

    private List<String> _excludedPackages;

    /**
     * {@inheritDoc}
     */
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec) {
        for (Object o : spec.getPropertySpecificationNames()) {
            String name = (String) o;
            IPropertySpecification ps = spec.getPropertySpecification(name);

            checkProxy(op, ps);
        }
    }
    
    public Class extractPropertyType(Class type, String propertyName, IPropertySpecification ps) {
        
        try {
            BeanInfo info = Introspector.getBeanInfo(type);
            PropertyDescriptor[] props = info.getPropertyDescriptors();

            for (PropertyDescriptor prop : props) {

                if (!propertyName.equals(prop.getName())) {
                    continue;
                }
                
                Method m = prop.getReadMethod();
                if (m != null && !m.getGenericReturnType().getClass().getName().equals("java.lang.Class")
                        && TypeVariable.class.isAssignableFrom(m.getGenericReturnType().getClass())) {
                    
                    ps.setGeneric(true);
                    TypeVariable tvar = (TypeVariable)m.getGenericReturnType();
                    
                    // try to set the actual type
                    if (type.getGenericSuperclass() != null 
                            && ParameterizedType.class.isInstance(type.getGenericSuperclass())) {
                        
                        ParameterizedType ptype = (ParameterizedType)type.getGenericSuperclass();
                        if (ptype.getActualTypeArguments().length > 0) {
                            
                            ps.setCanProxy(canProxyType((Class)ptype.getActualTypeArguments()[0]));
                            ps.setType(((Class)tvar.getBounds()[0]).getName());
                            
                            return (Class)tvar.getBounds()[0];
                        }
                    }
                    
                    return null;
                } else if (m != null) {
                    
                    ps.setCanProxy(canProxyType(m.getReturnType()));
                    ps.setType(m.getReturnType().getName());
                    
                    return m.getReturnType();
                }
                
                // try write method instead
                
                if (m == null && prop.getWriteMethod() == null)
                    return null;
                
                m = prop.getWriteMethod();
                if (m.getParameterTypes().length != 1)
                    return null;
                
                Type genParam = m.getGenericParameterTypes()[0];
                Class param = m.getParameterTypes()[0];
                
                if (!genParam.getClass().getName().equals("java.lang.Class")
                        && TypeVariable.class.isAssignableFrom(genParam.getClass())) {
                    
                    TypeVariable tvar = (TypeVariable)genParam;
                    ps.setGeneric(true);
                    
                    if (type.getGenericSuperclass() != null) {
                        
                        ParameterizedType ptype = (ParameterizedType)type.getGenericSuperclass();
                        if (ptype.getActualTypeArguments().length > 0) {
                            
                            ps.setCanProxy(canProxyType((Class)ptype.getActualTypeArguments()[0]));
                            ps.setType(((Class)tvar.getBounds()[0]).getName());
                            
                            return (Class)tvar.getBounds()[0];
                        }
                    }
                }
                
                ps.setCanProxy(canProxyType(param));
                ps.setType(param.getName());
                return param;
            }

        } catch (Throwable t) {
            
            throw new ApplicationRuntimeException("Error reading property " + propertyName + " from base component class : " + type, t);
        }

        return null;
    }

    boolean canProxyType(Class type)
    {
        if (type == null)
            return false;
        
        if (!EnhanceUtils.canProxyPropertyType(type))
            return false;
        
        for (Annotation an : type.getAnnotations()) {
            if (isExcluded(an)) {
                return false;
            }
        }
        
        return true;
    }
    
    void checkProxy(EnhancementOperation op, IPropertySpecification ps) {
        ps.setProxyChecked(true);

        if (!ps.isPersistent()) {
            return;
        }
        
        extractPropertyType(op.getBaseClass(), ps.getName(), ps);
    }
    
    boolean isExcluded(Annotation annotation) {
        for (String match : _excludedPackages) {

            if (annotation.annotationType().getName().indexOf(match) > -1) {
                return true;
            }
        }

        return false;
    }

    public void setExcludedPackages(List<String> packages) {
        _excludedPackages = packages;
    }
}

