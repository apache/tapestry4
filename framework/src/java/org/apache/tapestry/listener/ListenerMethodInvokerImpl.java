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

package org.apache.tapestry.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 * Logic for mapping a listener method name to an actual method invocation; this may require a
 * little searching to find the correct version of the method, based on the number of parameters to
 * the method (there's a lot of flexibility in terms of what methods may be considered a listener
 * method).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ListenerMethodInvokerImpl implements ListenerMethodInvoker
{
    /**
     * Methods with a name appropriate for this class, sorted into descending order by number of
     * parameters.
     */

    private final Method[] _methods;

    /**
     * The listener method name, used in some error messages.
     */

    private final String _name;

    public ListenerMethodInvokerImpl(String name, Method[] methods)
    {
        Defense.notNull(name, "name");
        Defense.notNull(methods, "methods");

        _name = name;
        _methods = methods;
    }

    public void invokeListenerMethod(Object target, IRequestCycle cycle)
    {
        Object[] listenerParameters = cycle.getListenerParameters();

        // method(parameters)
        if (searchAndInvoke(target, false, true, cycle, listenerParameters))
            return;

        // method(IRequestCycle, parameters)
        if (searchAndInvoke(target, true, true, cycle, listenerParameters))
            return;

        // method()
        if (searchAndInvoke(target, false, false, cycle, listenerParameters))
            return;

        // method(IRequestCycle)
        if (searchAndInvoke(target, true, false, cycle, listenerParameters))
            return;

        throw new ApplicationRuntimeException(ListenerMessages.noListenerMethodFound(
                _name,
                listenerParameters,
                target), target, null, null);
    }

    private boolean searchAndInvoke(Object target, boolean includeCycle, boolean includeParameters,
            IRequestCycle cycle, Object[] listenerParameters)
    {
        int listenerParameterCount = Tapestry.size(listenerParameters);
        int methodParameterCount = includeParameters ? listenerParameterCount : 0;

        if (includeCycle)
            methodParameterCount++;

        for (int i = 0; i < _methods.length; i++)
        {
            Method m = _methods[i];

            // Since the methods are sorted, descending, by parameter count,
            // there's no point in searching past that point.

            Class[] parameterTypes = m.getParameterTypes();

            if (parameterTypes.length < methodParameterCount)
                break;

            if (parameterTypes.length != methodParameterCount)
                continue;

            boolean firstIsCycle = parameterTypes.length > 0
                    && parameterTypes[0] == IRequestCycle.class;

            // When we're searching for a "traditional" style listener method,
            // one which takes the request cycle as its first parameter,
            // then check that first parameter is *exactly* IRequestCycle
            // On the other hand, if we're looking for new style
            // listener methods (includeCycle is false), then ignore
            // any methods whose first parameter is the request cycle
            // (we'll catch those in a later search).

            if (includeCycle != firstIsCycle)
                continue;

            invokeListenerMethod(
                    m,
                    target,
                    includeCycle,
                    includeParameters,
                    cycle,
                    listenerParameters);

            return true;
        }

        return false;
    }

    private void invokeListenerMethod(Method listenerMethod, Object target, boolean includeCycle,
            boolean includeParameters, IRequestCycle cycle, Object[] listenerParameters)
    {
        Object[] parameters = new Object[listenerMethod.getParameterTypes().length];
        int cursor = 0;

        if (includeCycle)
            parameters[cursor++] = cycle;

        if (includeParameters)
            for (int i = 0; i < Tapestry.size(listenerParameters); i++)
                parameters[cursor++] = listenerParameters[i];

        try
        {
            listenerMethod.invoke(target, parameters);
        }
        catch (InvocationTargetException ex)
        {
            Throwable targetException = ex.getTargetException();

            if (targetException instanceof ApplicationRuntimeException)
                throw (ApplicationRuntimeException) targetException;

            throw new ApplicationRuntimeException(ListenerMessages.listenerMethodFailure(
                    listenerMethod,
                    target,
                    targetException), target, null, targetException);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ListenerMessages.listenerMethodFailure(
                    listenerMethod,
                    target,
                    ex), target, null, ex);

        }
    }
}