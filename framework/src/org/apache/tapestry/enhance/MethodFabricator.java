/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.enhance;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.tapestry.Tapestry;

/**
 *  Wrapper around {@link org.apache.bcel.generic.MethodGen} used to
 *  simplify the creation of new methods within a new class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/

public class MethodFabricator
{
    private ClassGen _classGen;
    private MethodGen _methodGen;
    private InstructionList _instructionList = new InstructionList();
    private List _arguments = new ArrayList();
    private boolean _argumentsCommitted = false;

    private static class Argument
    {
        Type _type;
        String _name;

        Argument(Type type, String name)
        {
            _type = type;
            _name = name;
        }
    }

    MethodFabricator(ClassGen classGen, int accessFlags, Type returnType, String methodName)
    {
        _classGen = classGen;
        _methodGen =
            new MethodGen(
                accessFlags,
                returnType,
                null,
                null,
                methodName,
                _classGen.getClassName(),
                _instructionList,
                _classGen.getConstantPool());
    }

    /**
     *  Adds an argument to the method.  Arguments must be
     *  added in order.  Returns the index of the added
     *  argument.  This is an implicit argument in slot 0,
     *  the first explicit argument is in slot 1.
     * 
     *  <p>All arguments must be added before any local variables
     *  are added.
     * 
     *  @throws IllegalStateException if arguments may not be added.
     **/

    public int addArgument(Type type, String name)
    {
        if (_argumentsCommitted)
            throw new IllegalStateException(
                Tapestry.getMessage("MethodFabricator.no-more-arguments"));

        _arguments.add(new Argument(type, name));

        return _arguments.size();
    }

    /**
     *  Adds a local variable of the given type, returning its index.
     *  Local variables come after any arguments.  Arguments may not
     *  be added after local variables.
     * 
     **/

    public int addLocalVariable(Type type, String name)
    {
        if (!_argumentsCommitted)
            commitArguments();

        LocalVariableGen l = _methodGen.addLocalVariable(name, type, null, null);

        return l.getIndex();
    }

    /**
     *  Returns the instruction list for the method
     *  being created.
     * 
     **/

    public InstructionList getInstructionList()
    {
        return _instructionList;
    }

    /**
     *  Convienience method for adding instructions.
     * 
     **/

    public InstructionHandle append(Instruction instruction)
    {
        return _instructionList.append(instruction);
    }

    /**
     *  Convienience method for adding instructions.
     * 
     **/

    public InstructionHandle append(CompoundInstruction instruction)
    {
        return _instructionList.append(instruction);
    }

	/**
	 *  Convienience method for adding instructions.
	 * 
	 **/
	
	public InstructionHandle append(BranchInstruction instruction)
	{
		return _instructionList.append(instruction);
	}

    /**
     *  Commits the method; this is invoked last.  It updates
     *  the {@link org.apache.bcel.generic.MethodGen} with
     *  any arguments that have been added, then
     *  creates the {@link org.apache.bcel.classfile.Method}
     *  object and adds it to the {@link ClassGen}.
     * 
     **/

    public void commit()
    {
        if (!_argumentsCommitted)
            commitArguments();

        InstructionHandle start = _instructionList.getStart();
        InstructionHandle end = _instructionList.getEnd();

        LocalVariableGen[] variables = _methodGen.getLocalVariables();
        for (int i = 0; i < variables.length; i++)
        {
            variables[i].setStart(start);
            variables[i].setEnd(end);
        }

        _methodGen.setMaxStack();

        Method m = _methodGen.getMethod();

        _classGen.addMethod(m);
    }

    /**
     *  Invoked to update the {@link org.apache.bcel.generic.MethodGen} with
     *  any arguments.  This must be done before adding any local variables.
     * 
     **/

    private void commitArguments()
    {
        int count = _arguments.size();
        if (count > 0)
        {
            Type[] types = new Type[count];
            String[] names = new String[count];

            for (int i = 0; i < count; i++)
            {
                Argument a = (Argument) _arguments.get(i);

                types[i] = a._type;
                names[i] = a._name;

                _methodGen.addLocalVariable(a._name, a._type, null, null);
            }

            _methodGen.setArgumentNames(names);
            _methodGen.setArgumentTypes(types);
        }

        _argumentsCommitted = true;
    }

	/**
	 *  Adds an exception handler.  The start and end instructions are indicated by their
	 *  handles (to be honest, I'm shakey on whether the entire end instruction is covered,
	 *  or only until just before the end instruction).  The handler is an instruction
	 *  that should immediately follow the protected block.  The catch type
	 *  determines what kind of exception will be caught.
	 * 
	 **/
	
    public void addExceptionHandler(
        InstructionHandle start,
        InstructionHandle end,
        InstructionHandle handler,
        ObjectType catchType)
    {
        _methodGen.addExceptionHandler(start, end, handler, catchType);
    }
}
