package net.sf.tapestry.enhance;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.Tapestry;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

/**
 *  Wrapper around {@link org.apache.bcel.generic.MethodGen} used to
 *  simplify the creation of new methods within a new class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
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
                Tapestry.getString("MethodFabricator.no-more-arguments"));

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
}
