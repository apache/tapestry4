// Copyright 2007 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.MethodFab;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.service.impl.AbstractFab;
import org.apache.hivemind.service.impl.CtClassSource;

/**
 * Implementation replacement for hivemind {@link ClassFab} utiltity to get around some javassist
 * incompatibilties found with the latest 3.4 version of javassist.
 * 
 * @author jkuhnert
 */
public class ClassFabImpl extends AbstractFab implements ClassFab
{
    /**
     * Stores information about a constructor; used by toString().
     * 
     * @since 1.1
     */

    private class AddedConstructor
    {

        private Class[] _parameterTypes;

        private Class[] _exceptionTypes;

        private String _body;

        AddedConstructor(Class[] parameterTypes, Class[] exceptionTypes, String body)
        {
            _parameterTypes = parameterTypes;
            _exceptionTypes = exceptionTypes;
            _body = body;
        }

        public String toString()
        {
            StringBuffer buffer = new StringBuffer();

            buffer.append("public ");
            buffer.append(getCtClass().getName());

            buffer.append("(");

            int count = size(_parameterTypes);
            for(int i = 0; i < count; i++) {
                if (i > 0) buffer.append(", ");

                buffer.append(_parameterTypes[i].getName());

                buffer.append(" $");
                buffer.append(i + 1);
            }

            buffer.append(")");

            count = size(_exceptionTypes);
            for(int i = 0; i < count; i++) {
                if (i == 0)
                    buffer.append("\n  throws ");
                else buffer.append(", ");

                buffer.append(_exceptionTypes[i].getName());
            }

            buffer.append("\n");
            buffer.append(_body);

            buffer.append("\n");

            return buffer.toString();
        }

        private int size(Object[] array)
        {
            return array == null ? 0 : array.length;
        }
    }

    /**
     * Map of {@link MethodFab}keyed on {@link MethodSignature}.
     */
    private Map _methods = new HashMap();

    /**
     * List of {@link AddedConstructor}.
     * 
     * @since 1.1
     */

    private List _constructors = new ArrayList();

    public ClassFabImpl(CtClassSource source, CtClass ctClass)
    {
        super(source, ctClass);
    }

    /**
     * Returns a representation of the fabricated class, including inheritance, fields,
     * constructors, methods and method bodies.
     * 
     * @since 1.1
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ClassFab[\n");

        try {
            buildClassAndInheritance(buffer);

            buildFields(buffer);

            buildConstructors(buffer);

            buildMethods(buffer);

        } catch (Exception ex) {
            buffer.append(" *** ");
            buffer.append(ex);
        }

        buffer.append("\n]");

        return buffer.toString();
    }

    /** @since 1.1 */
    private void buildMethods(StringBuffer buffer)
    {
        Iterator i = _methods.values().iterator();
        while(i.hasNext()) {

            MethodFab mf = (MethodFab) i.next();

            buffer.append("\n");
            buffer.append(mf);
            buffer.append("\n");
        }
    }

    /** @since 1.1 */
    private void buildConstructors(StringBuffer buffer)
    {
        Iterator i = _constructors.iterator();

        while(i.hasNext()) {
            buffer.append("\n");
            buffer.append(i.next());
        }
    }

    /** @since 1.1 */
    private void buildFields(StringBuffer buffer)
        throws NotFoundException
    {
        CtField[] fields = getCtClass().getDeclaredFields();

        for(int i = 0; i < fields.length; i++) {
            buffer.append("\n");
            buffer.append(modifiers(fields[i].getModifiers()));
            buffer.append(" ");
            buffer.append(fields[i].getType().getName());
            buffer.append(" ");
            buffer.append(fields[i].getName());
            buffer.append(";\n");
        }
    }

    /** @since 1.1 */
    private void buildClassAndInheritance(StringBuffer buffer)
        throws NotFoundException
    {
        buffer.append(modifiers(getCtClass().getModifiers()));
        buffer.append(" class ");
        buffer.append(getCtClass().getName());
        buffer.append(" extends ");
        buffer.append(getCtClass().getSuperclass().getName());
        buffer.append("\n");

        CtClass[] interfaces = getCtClass().getInterfaces();

        if (interfaces.length > 0) {
            buffer.append("  implements ");

            for(int i = 0; i < interfaces.length; i++) {
                if (i > 0) buffer.append(", ");

                buffer.append(interfaces[i].getName());
            }

            buffer.append("\n");
        }
    }

    private String modifiers(int modifiers)
    {
        return Modifier.toString(modifiers);
    }

    /**
     * Returns the name of the class fabricated by this instance.
     */
    String getName()
    {
        return getCtClass().getName();
    }

    public void addField(String name, Class type)
    {
        CtClass ctType = convertClass(type);

        try {
            CtField field = new CtField(ctType, name, getCtClass());
            field.setModifiers(Modifier.PRIVATE);

            getCtClass().addField(field);
        } catch (CannotCompileException ex) {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToAddField(name, getCtClass(), ex), ex);
        }
    }

    public boolean containsMethod(MethodSignature ms)
    {
        return _methods.get(ms) != null;
    }

    public MethodFab addMethod(int modifiers, MethodSignature ms, String body)
    {
        if (_methods.get(ms) != null)
            throw new ApplicationRuntimeException(EnhanceMessages.duplicateMethodInClass(ms, this));
        
        if (body.indexOf("isWrapperFor") > 0 || body.indexOf("unwrap") > 0)
            return new MethodFabImpl(null, ms, null, "{ throw new UnsupportedOperationException(\"Method not implemented\"); }");
        
        CtClass ctReturnType = convertClass(ms.getReturnType());

        CtClass[] ctParameters = convertClasses(ms.getParameterTypes());
        CtClass[] ctExceptions = convertClasses(ms.getExceptionTypes());
        
        CtMethod method = new CtMethod(ctReturnType, ms.getName(), ctParameters, getCtClass());
        
        try {
            method.setModifiers(modifiers);
            method.setBody(body);
            method.setExceptionTypes(ctExceptions);
            
            getCtClass().addMethod(method);
        } catch (Exception ex) {
            
            throw new ApplicationRuntimeException(EnhanceMessages.unableToAddMethod(ms, getCtClass(), ex), ex);
        }

        // Return a MethodFab so the caller can add catches.

        MethodFab result = new MethodFabImpl(getSource(), ms, method, body);

        _methods.put(ms, result);

        return result;
    }
    
    public MethodFab getMethodFab(MethodSignature ms)
    {
        return (MethodFab) _methods.get(ms);
    }

    public void addConstructor(Class[] parameterTypes, Class[] exceptions, String body)
    {
        CtClass[] ctParameters = convertClasses(parameterTypes);
        CtClass[] ctExceptions = convertClasses(exceptions);

        try {
            CtConstructor constructor = new CtConstructor(ctParameters, getCtClass());
            constructor.setExceptionTypes(ctExceptions);
            constructor.setBody(body);

            getCtClass().addConstructor(constructor);

            _constructors.add(new AddedConstructor(parameterTypes, exceptions, body));
        } catch (Exception ex) {
            throw new ApplicationRuntimeException(EnhanceMessages.unableToAddConstructor(getCtClass(), ex), ex);
        }
    }
}
