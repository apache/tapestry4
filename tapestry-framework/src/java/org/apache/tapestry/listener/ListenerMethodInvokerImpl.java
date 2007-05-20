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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Logic for mapping a listener method name to an actual method invocation; this
 * may require a little searching to find the correct version of the method,
 * based on the number of parameters to the method (there's a lot of flexibility
 * in terms of what methods may be considered a listener method).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ListenerMethodInvokerImpl implements ListenerMethodInvoker
{

    /**
     * Used as default byte value in null method parameters for native types
     */
    private static final byte DEFAULT_BYTE = -1;

    /**
     * Used as default short value in null method parameters for native types
     */
    private static final short DEFAULT_SHORT = -1;
    
    /**
     * Methods with a name appropriate for this class, sorted into descending
     * order by number of parameters.
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
        
        if (listenerParameters == null)
            listenerParameters = new Object[0];
        
        if (searchAndInvoke(target, cycle, listenerParameters))
            return;
        
        throw new ApplicationRuntimeException(ListenerMessages.noListenerMethodFound(_name, listenerParameters, target),
                target, null, null);
    }
    
    private boolean searchAndInvoke(Object target, IRequestCycle cycle, Object[] listenerParameters)
    {
        BrowserEvent event = null;
        if (listenerParameters.length > 0 
                && BrowserEvent.class.isInstance(listenerParameters[listenerParameters.length - 1]))
            event = (BrowserEvent)listenerParameters[listenerParameters.length - 1];
        
        List invokeParms = new ArrayList();

        Method possibleMethod = null;

        methods:
            for (int i = 0; i < _methods.length; i++, invokeParms.clear()) {
                
                if (!_methods[i].getName().equals(_name))
                   continue;
                
                Class[] parms = _methods[i].getParameterTypes();
                
                // impossible to call this
                
                if (parms.length > (listenerParameters.length + 1) ) {
                    
                    if (possibleMethod == null)
                        possibleMethod = _methods[i];
                    else if (parms.length < possibleMethod.getParameterTypes().length)
                        possibleMethod = _methods[i];
                    
                    continue;
                }
                
                int listenerIndex = 0;
                for (int p = 0; p < parms.length && listenerIndex < (listenerParameters.length + 1); p++) {
                    
                    // special case for BrowserEvent
                    if (BrowserEvent.class.isAssignableFrom(parms[p])) {
                        if (event == null)
                            continue methods;
                        
                        if (!invokeParms.contains(event))
                            invokeParms.add(event);
                        
                        continue;
                    }
                    
                    // special case for request cycle
                    if (IRequestCycle.class.isAssignableFrom(parms[p])) {
                        invokeParms.add(cycle);
                        continue;
                    }
                    
                    if (event != null && listenerIndex < (listenerParameters.length + 1)
                            || listenerIndex < listenerParameters.length) {
                        invokeParms.add(listenerParameters[listenerIndex]);
                        listenerIndex++;
                    }
                }
                
                if (invokeParms.size() != parms.length) {

                    // set possible method just in case
                    
                    if (possibleMethod == null)
                        possibleMethod = _methods[i];
                    else if (parms.length < possibleMethod.getParameterTypes().length)
                        possibleMethod = _methods[i];

                    continue;
                }
                
                invokeListenerMethod(_methods[i], target, cycle, invokeParms.toArray(new Object[invokeParms.size()]));
                
                return true;
            }

        // if we didn't have enough parameters but still found a matching method name go ahead
        // and do your best to fill in the parameters and invoke it

        if (possibleMethod != null) {

            Class[] parms = possibleMethod.getParameterTypes();
            Object[] args = new Object[parms.length];
            
            for (int p=0; p < parms.length; p++) {

                // setup primitive defaults
                
                if (parms[p].isPrimitive()) {

                    if (parms[p] == Boolean.TYPE) {

                        args[p] = Boolean.FALSE;
                    } else if (parms[p] == Byte.TYPE) {

                        args[p] = new Byte(DEFAULT_BYTE);
                    } else if (parms[p] == Short.TYPE) {

                        args[p] = new Short(DEFAULT_SHORT);
                    } else if (parms[p] == Integer.TYPE) {

                        args[p] = new Integer(-1);
                    } else if (parms[p] == Long.TYPE) {

                        args[p] = new Long(-1);
                    } else if (parms[p] == Float.TYPE) {

                        args[p] = new Float(-1);
                    } else if (parms[p] == Double.TYPE) {

                        args[p] = new Double(-1);
                    }
                }

                if (IRequestCycle.class.isAssignableFrom(parms[p])) {
                    args[p] = cycle;
                }
            }
            
            invokeListenerMethod(possibleMethod, target, cycle, args);
            
            return true;
        }

        return false;
    }

    private void invokeListenerMethod(Method listenerMethod, Object target,
            IRequestCycle cycle, Object[] parameters)
    {
        
        Object methodResult = null;
        
        try
        {
            methodResult = invokeTargetMethod(target, listenerMethod, parameters);
        }
        catch (InvocationTargetException ex)
        {
            Throwable targetException = ex.getTargetException();
            
            if (targetException instanceof ApplicationRuntimeException)
                throw (ApplicationRuntimeException) targetException;

            throw new ApplicationRuntimeException(ListenerMessages.listenerMethodFailure(listenerMethod, target,
                            targetException), target, null, targetException);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ListenerMessages.listenerMethodFailure(listenerMethod, target, ex), target,
                    null, ex);

        }
        
        // void methods return null
        
        if (methodResult == null) return;
        
        // The method scanner, inside ListenerMapSourceImpl,
        // ensures that only methods that return void, String,
        // or assignable to ILink or IPage are considered.

        if (methodResult instanceof String)
        {
            cycle.activate((String) methodResult);
            return;
        }
        
        if (methodResult instanceof ILink)
        {
            ILink link = (ILink) methodResult;

            String url = link.getAbsoluteURL();

            cycle.sendRedirect(url);
            return;
        }

        cycle.activate((IPage) methodResult);
    }
    
    /**
     * Provided as a hook so that subclasses can perform any additional work
     * before or after invoking the listener method.
     */

    protected Object invokeTargetMethod(Object target, Method listenerMethod,
            Object[] parameters)
        throws IllegalAccessException, InvocationTargetException
    {
        return listenerMethod.invoke(target, parameters);
    }


    public String getMethodName()
    {
        return _name;
    }

    public String toString()
    {
        return "ListenerMethodInvokerImpl[" +
               "_name='" + _name + '\'' +
               ']';
    }
}
