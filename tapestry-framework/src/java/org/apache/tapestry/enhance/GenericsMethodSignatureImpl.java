//Copyright 2004, 2005 The Apache Software Foundation

//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at

//http://www.apache.org/licenses/LICENSE-2.0

//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package org.apache.tapestry.enhance;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;


/**
 * Implementation of {@link MethodSignature} that supports jdk 1.5 generic typing support.
 * 
 */
public class GenericsMethodSignatureImpl extends MethodSignatureImpl implements MethodSignature
{
    boolean _isGeneric;
    
    public GenericsMethodSignatureImpl(Class type, Method m)
    {
        super(findReturnType(type, m), m.getName(), findParameterTypes(type, m), m.getExceptionTypes());
        
        _isGeneric = type.getGenericSuperclass() != null && ParameterizedType.class.isInstance(type.getGenericSuperclass());
    }

    public boolean isGeneric()
    {
        return _isGeneric;
    }
    
    static Class findReturnType(Class type, Method m)
    {
        Type ret = m.getGenericReturnType();

        if (TypeVariable.class.isInstance(ret) 
                && type.getGenericSuperclass() != null
                && ParameterizedType.class.isInstance(type.getGenericSuperclass())) {

            TypeVariable tvar = (TypeVariable)ret;
            ParameterizedType param = (ParameterizedType)type.getGenericSuperclass();

            Class resolvedType = resolveType(param, tvar);
            if (resolvedType != null)
                return resolvedType;
            
        }

        return m.getReturnType();
    }

    static Class resolveType(ParameterizedType param, TypeVariable var)
    {
        if (param.getActualTypeArguments().length < 1)
            return null;

        for (int i=0; i < var.getBounds().length; i++) {

            Type t = var.getBounds()[i];
            Class resolvedType = null;
            if (ParameterizedType.class.isInstance(t)) {

                ParameterizedType pparam = (ParameterizedType)t;
                for (int e=0; e < pparam.getActualTypeArguments().length; e++) {
                    if (!TypeVariable.class.isInstance(pparam.getActualTypeArguments()[e]))
                        continue;
                    
                    resolvedType = resolveType(pparam, (TypeVariable)pparam.getActualTypeArguments()[e]);
                }
            } else {

                resolvedType = findType(param.getActualTypeArguments(), (Class)t);
            }
            
            if (resolvedType != null)
                return resolvedType;
        }

        return null;
    }

    static Class findType(Type[] types, Class type)
    {
        for (int i = 0; i < types.length; i++) {
            
            if (Class.class.isInstance(types[i]) && type.isAssignableFrom((Class)types[i]))
                return (Class)types[i];
        }

        return null;
    }

    static Class[] findParameterTypes(Class type, Method m)
    {
        if (type.getGenericSuperclass() == null
                || !ParameterizedType.class.isInstance(type.getGenericSuperclass()))
            return m.getParameterTypes();

        ParameterizedType param = (ParameterizedType)type.getGenericSuperclass();
        Type[] genTypes = m.getGenericParameterTypes();
        Class[] types = new Class[genTypes.length];

        typeSearch:
            for (int i=0; i < genTypes.length; i++) {

                if (TypeVariable.class.isInstance(genTypes[i])) {

                    Class resolved = resolveType(param, (TypeVariable)genTypes[i]);
                    
                    if (resolved != null) {
                        types[i] = resolved;
                        continue;
                    }
                }

                types[i] = m.getParameterTypes()[i];
            }

        return types;
    }


}
