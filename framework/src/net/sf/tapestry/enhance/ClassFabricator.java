/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.enhance;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

/**
 *  Class used to simplify the generation of new classes as
 *  a wrapper around BCEL.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class ClassFabricator
{
    private ClassGen _classGen;
    private InstructionFactory _instructionFactory;

    /**
     *  Creates a public final class.
     * 
     **/

    public ClassFabricator(String className, String parentClassName)
    {
        this(className, parentClassName, Constants.ACC_FINAL | Constants.ACC_PUBLIC);
    }

    /**
     *  General constructor, creates a new instance using the supplied
     *  parameters.  ACC_SUPER is automatically added to the access flags.
     * 
     **/

    public ClassFabricator(String className, String parentClassName, int accessFlags)
    {
        _classGen =
            new ClassGen(
                className,
                parentClassName,
                "<synthetic>",
                accessFlags | Constants.ACC_SUPER,
                null);
    }

	/**
	 *  Creates a new {@link MethodFabricator}.  Invoke
	 *  {@link MethodFabricator#getInstructionList()}
	 *  to obtain the (initially empty) instruction list
	 *  for the method.
	 * 
	 **/
	
    public MethodFabricator createMethod(int accessFlags, Type returnType, String methodName)
    {
        return new MethodFabricator(_classGen, accessFlags, returnType, methodName);
    }

    /**
     *  Creates a default public method that returns void.
     * 
     **/

    public MethodFabricator createMethod(String methodName)
    {
        return createMethod(Constants.ACC_PUBLIC, Type.VOID, methodName);
    }

    /**
     *  Adds a private instance variable of the given type and name.
     * 
     **/

    public void addField(Type type, String name)
    {
        addField(Constants.ACC_PRIVATE, type, name);
    }

    /**
     *  Adds a field with the given access type, type and name.
     * 
     **/

    public void addField(int accessFlags, Type type, String name)
    {
        FieldGen fg = new FieldGen(accessFlags, type, name, _classGen.getConstantPool());

        Field f = fg.getField();

        _classGen.addField(f);
    }

    /**
     *  Adds a public, no-arguments constructor.
     * 
     **/

    public void addDefaultConstructor()
    {
        _classGen.addEmptyConstructor(Constants.ACC_PUBLIC);
    }


	/**
	 *  Adds an interface to the list of interfaces implemented
	 *  by the class.
	 * 
	 **/
	
	public void addInterface(String interfaceName)
	{
		_classGen.addInterface(interfaceName);
	}
	
	public void addInterface(Class interfaceClass)
	{
		addInterface(interfaceClass.getName());
	}
	
	public InstructionFactory getInstructionFactory()
	{
		if (_instructionFactory == null)
			_instructionFactory = new InstructionFactory(_classGen);
		
		return _instructionFactory;
	}

    /**
     *  Invoked very much last, to create the
     *  new {@link org.apache.bcel.classfile.JavaClass} instance.
     * 
     **/

    public JavaClass commit()
    {
        return _classGen.getJavaClass();
    }
    
    /**
     *  Returns the fully qualified class name.
     * 
     **/
    
    public String getClassName()
    {
    	return _classGen.getClassName();
    }
    
    /**
     *  Returns the mutable constant pool.
     * 
     **/
    
    public ConstantPoolGen getConstantPool()
    {
    	return _classGen.getConstantPool();
    }
}
