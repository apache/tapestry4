/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.enhance.javassist;

import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.tapestry.enhance.CodeGenerationException;

/**
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class ClassMapping
{

    /**
     *  Map of type (as Type), keyed on type name.
     * 
     *  This should be kept in synch with ParameterManager, which maintains
     *  a similar list.
     * 
     **/

    private Map _objectTypeMap = new HashMap();
    private ClassPool _classPool;

    public ClassMapping(ClassPool classPool)
    {
        _classPool = classPool;
        initialize();
    }

    protected void initialize()
    {
        recordType("boolean", CtClass.booleanType);
        recordType("short", CtClass.shortType);
        recordType("int", CtClass.intType);
        recordType("long", CtClass.longType);
        recordType("float", CtClass.floatType);
        recordType("double", CtClass.doubleType);
        recordType("char", CtClass.charType);
        recordType("byte", CtClass.byteType);
        
        try
        {
            loadType("boolean[]");
            loadType("short[]");
            loadType("int[]");
            loadType("long[]");
            loadType("float[]");
            loadType("double[]");
            loadType("char[]");
            loadType("byte[]");

            loadType("java.lang.Object");
            loadType("java.lang.Object[]");

            loadType("java.lang.String");
            loadType("java.lang.String[]");
        }
        catch (NotFoundException e)
        {
            // This exception should not occur since the types above must exist.
            // Nevertheless...
            throw new CodeGenerationException(e);
        }
    }

    public void loadType(String type) throws NotFoundException
    {
        CtClass objectType = _classPool.get(type);
        _objectTypeMap.put(type, objectType);
    }
    
    public void recordType(String type, CtClass objectType)
    {
        _objectTypeMap.put(type, objectType);
    }

    public CtClass getType(String type)
    {
        return (CtClass) _objectTypeMap.get(type);
    }
}
