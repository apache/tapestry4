//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import ognl.OgnlRuntime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *  Maps a class to a set of listeners based on the public methods of the class.
 *  {@link net.sf.tapestry.listener.ListenerMapPropertyAccessor} is setup
 *  to provide these methods as named properties of the ListenerMap.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public class ListenerMap
{
    private static final Logger LOG = LogManager.getLogger(ListenerMap.class);

    static {
        OgnlRuntime.setPropertyAccessor(ListenerMap.class, new ListenerMapPropertyAccessor());
    }

    private Object _target;

    /**
     *  A {@link Map} of relevant {@link Method}s, keyed on method name.
     *  This is just the public void methods that take either
     *  a {@link IRequestCycle} or a 
     *  String[] and a {@link IRequestCycle} as parameters,
     *  return void, and throw nothing or just {@link RequestCycleException}.
     *
     **/

    private Map _methodMap;

    /**
     * A {@link Map} of cached listener instances, keyed on method name
     *
     **/

    private Map _listenerCache = new HashMap();

    /**
     * A {@link Map}, keyed on Class, of {@link Map} ... the method map
     * for any particular instance of the given class.
     *
     **/

    private static Map _classMap = new HashMap();

    /**
     *  Implements both listener interfaces.
     *
     **/

    private class SyntheticListener implements IActionListener
    {
        private Method _method;

        SyntheticListener(Method method)
        {
            _method = method;
        }

        private void invoke(IRequestCycle cycle) throws RequestCycleException
        {
            Object[] args = new Object[] { cycle };

            invokeTargetMethod(_target, _method, args);
        }

        public void actionTriggered(IComponent component, IRequestCycle cycle) throws RequestCycleException
        {
            invoke(cycle);
        }

        public String toString()
        {
            StringBuffer buffer = new StringBuffer("SyntheticListener[");

            buffer.append(_target);
            buffer.append(' ');
            buffer.append(_method);
            buffer.append(']');

            return buffer.toString();
        }

    }

    public ListenerMap(Object target)
    {
        _target = target;
    }

    /**
     *  Gets a listener for the given name (which is both a property name
     *  and a method name).  The listener is created as needed, but is
     *  also cached for later use.
     *
     * @throws ApplicationRuntimeException if the listener can not be created.
     **/

    public synchronized Object getListener(String name)
    {
        Object listener = null;

        listener = _listenerCache.get(name);

        if (listener == null)
        {
            listener = createListener(name);

            _listenerCache.put(name, listener);
        }

        return listener;
    }

    /**
     *  Returns an object that implements {@link IActionListener}.
     *  This involves looking up the method by name and determining which
     *  inner class to create.
     **/

    private synchronized Object createListener(String name)
    {
        if (_methodMap == null)
            getMethodMap();

        Method method = (Method) _methodMap.get(name);

        if (method == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("ListenerMap.object-missing-method", _target, name));

        return new SyntheticListener(method);
    }

    /**
     *  Gets the method map for the current instance.  If necessary, it is constructed and cached (for other instances
     *  of the same class).
     *
     **/

    private synchronized Map getMethodMap()
    {
        if (_methodMap != null)
            return _methodMap;

        Class beanClass = _target.getClass();

        synchronized (_classMap)
        {
            _methodMap = (Map) _classMap.get(beanClass);

            if (_methodMap == null)
            {
                _methodMap = buildMethodMap(beanClass);

                _classMap.put(beanClass, _methodMap);
            }
        }

        return _methodMap;
    }

    private static Map buildMethodMap(Class beanClass)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Building method map for class " + beanClass.getName());

        Map result = new HashMap();
        Method[] methods = beanClass.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            Method m = methods[i];
            int mods = m.getModifiers();

            if (Modifier.isStatic(mods))
                continue;

            // Probably not necessary, getMethods() returns only public
            // methods.

            if (!Modifier.isPublic(mods))
                continue;

            // Must return void

            if (m.getReturnType() != Void.TYPE)
                continue;

            Class[] parmTypes = m.getParameterTypes();

            if (parmTypes.length != 1)
                continue;

            // parm must be IRequestCycle

            if (!parmTypes[0].equals(IRequestCycle.class))
                continue;

            Class[] exceptions = m.getExceptionTypes();
            if (exceptions.length > 1)
                continue;

            if (exceptions.length == 1 && !exceptions[0].equals(RequestCycleException.class))
                continue;

            // Ha!  Passed all tests.

            result.put(m.getName(), m);
        }

        return result;

    }

    /**
     *  Invoked by the inner listener/adaptor classes to
     *  invoke the method.
     *
     **/

    private static void invokeTargetMethod(Object target, Method method, Object[] args) throws RequestCycleException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Invoking listener method " + method + " on " + target);

        try
        {
            try
            {
                method.invoke(target, args);
            }
            catch (InvocationTargetException ex)
            {
                Throwable inner = ex.getTargetException();

                if (inner instanceof RequestCycleException)
                    throw (RequestCycleException) inner;

                // Edit out the InvocationTargetException, if possible.

                if (inner instanceof RuntimeException)
                    throw (RuntimeException) inner;

                throw ex;
            }
        }
        catch (RequestCycleException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            // Catch InvocationTargetException or, preferrably,
            // the inner exception here (if its a runtime exception).

            throw new ApplicationRuntimeException(
                Tapestry.getString("ListenerMap.unable-to-invoke-method", method.getName(), target, ex.getMessage()),
                ex);
        }
    }

    /** 
     *  Returns an unmodifiable collection of the 
     *  names of the listeners implemented by the target class.
     *
     *  @since 1.0.6
     *
     **/

    public synchronized Collection getListenerNames()
    {
         return Collections.unmodifiableCollection(getMethodMap().keySet());
    }

    /**
     *  Returns true if this ListenerMap can provide a listener
     *  with the given name.
     * 
     *  @since 2.2
     * 
     **/

    public synchronized boolean canProvideListener(String name)
    {
         return getMethodMap().containsKey(name);
    }

    public String toString()
    {
        return "ListenerMap[" + _target + "]";
    }
}