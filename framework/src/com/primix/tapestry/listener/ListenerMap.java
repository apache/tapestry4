/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.listener;

import com.primix.tapestry.*;
import com.primix.tapestry.util.prop.*;
import java.util.*;
import java.lang.reflect.*;
import org.apache.log4j.*;

/**
 *  Maps a class to a set of listeners based on the public methods of the class.
 *  {@link ListenerMapHelper} is registered as the {@link PropertyHelper} for
 *  this class.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.2
 */


public class ListenerMap
{
	private static final Category CAT = Category.getInstance(ListenerMap.class);
	
	static
	{
		PropertyHelper.register(ListenerMap.class, ListenerMapHelper.class);
	}
	
	private Object target;
	
	/**
	 *  A {@link Map} of relevant {@link Method}s, keyed on method name.
	 *  This is just the public void methods that take either
	 *  a {@link IRequestCycle} or a 
	 *  String[] and a {@link IRequestCycle} as parameters,
	 *  return void, and throw nothing or just {@link RequestCycleException}.
	 *
	 */
	
	private Map methodMap;
	
	
	/**
	 * A {@link Map} of cached listener instances, keyed on method name
	 *
	 */
	
	private Map listenerCache;
	
	/**
	 * A {@link Map}, keyed on Class, of {@link Map} ... the method map
	 * for any particular instance of the given class.
	 *
	 */
	
	private static Map classMap;
	
	/**
	 *  Implements both listener interfaces.
	 *
	 */
	
	private class SyntheticListener
		implements IDirectListener, IActionListener
	{
		private Method method;
		
		SyntheticListener(Method method)
		{
			this.method = method;
		}
		
		
		private void invoke(IRequestCycle cycle) 
			throws RequestCycleException
		{
			Object[] args = new Object[] { cycle };
			
			invokeTargetMethod(target, method, args);
		}
		
		public void actionTriggered(IComponent component, IRequestCycle cycle)
			throws RequestCycleException
		{
			invoke(cycle);
		}
		
		public void directTriggered(IDirect component, String[] context, IRequestCycle cycle)
			throws RequestCycleException
		{
			invoke(cycle);
		}
		
		public String toString()
		{
			StringBuffer buffer = new StringBuffer("SyntheticListener[");
			
			buffer.append(target);
			buffer.append(' ');
			buffer.append(method);
			buffer.append(']');
			
			return buffer.toString();
		}
		
	}
	
	/**
	 *  Class used when the method includes a context (String[]) parameter.  This
	 *  must be a {@link IDirectListener}.
	 *
	 */
	
	private class SyntheticContextListener
		implements IDirectListener
	{
		private Method method;
		
		SyntheticContextListener(Method method)
		{
			this.method = method;
		}
		
		public void directTriggered(IDirect component, String[] context, IRequestCycle cycle)
			throws RequestCycleException
		{
			Object[] args = new Object[] {context, cycle};
			
			invokeTargetMethod(target, method, args);
		}
		
		public String toString()
		{
			StringBuffer buffer = new StringBuffer("SyntheticContextListener[");
			
			buffer.append(target);
			buffer.append(' ');
			buffer.append(method);
			buffer.append(']');
			
			return buffer.toString();
		}
	}
	
	public ListenerMap(Object target)
	{
		this.target = target;
	}
	
	/**
	 *  Gets a listener for the given name (which is both a property name
	 *  and a method name).  The listener is created as needed, but is
	 *  also cached for later use.
	 *
	 * @throws ApplicationRuntimeException if the listener can not be created.
	 */
	
	public Object getListener(String name)
	{
		Object listener = null;
		
		if (listenerCache == null)
		{
			synchronized(this)
			{
				if (listenerCache == null)
					listenerCache = new HashMap();
			}
		}
		
		synchronized(listenerCache)
		{
			listener = listenerCache.get(name);
		}
		
		if (listener == null)
		{
			listener = createListener(name);
			
			synchronized(listenerCache)
			{
				listenerCache.put(name, listener);
			}
		}
		
		return listener;
	}
	
	/**
	 *  Returns an object that implements {@link IDirectListener} and/or {@link IActionListener}.
	 *  This involves looking up the method by name and determining which
	 *  inner class to create.
	 */
	
	private Object createListener(String name)
	{
		Method method;
		
		if (methodMap == null)
			getMethodMap();
		
		synchronized(methodMap)
		{
			method = (Method)methodMap.get(name);
		}
		
		if (method == null)
			throw new ApplicationRuntimeException(
				"Object " + target + 
					" does not implement a listener method named '" +
					name + "'.");
		
		if (method.getParameterTypes().length == 1)
			return new SyntheticListener(method);
		
		// OK, must have two parameters (String[] and IRequestCycle)).
		
		return new SyntheticContextListener(method);
	}
	
	/**
	 *  Gets the method map for the current instance.  If necessary, it is constructed and cached (for other instances
	 *  of the same class).
	 *
	 */
	
	private Map getMethodMap()
	{
		if (methodMap != null)
			return methodMap;
		
		if (classMap == null)
		{
			synchronized(this)
			{
				if (classMap == null)
					classMap = new HashMap();
			}
		}
		
		Class beanClass = target.getClass();
		
		synchronized(classMap)
		{
			methodMap = (Map)classMap.get(beanClass);
		}
		
		if (methodMap == null)
		{
			methodMap = buildMethodMap(beanClass);
			
			synchronized(classMap)
			{
				classMap.put(beanClass, methodMap);
			}
		}
		
		return methodMap;
	}
	
	private static Map buildMethodMap(Class beanClass)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Building method map for class " + beanClass.getName());
		
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
			
			// Must have either 1 or 2 parameters
			
			if (parmTypes.length < 1 || parmTypes.length > 2)
				continue;
			
			// If two parms, first parm must be String[]
			if (parmTypes.length == 2 &&
					!parmTypes[0].equals(String[].class))
				continue;
			
			// 2nd/last parm must be IRequestCycle
			
			if (!parmTypes[parmTypes.length - 1].equals(IRequestCycle.class))
				continue;
			
			Class[] exceptions = m.getExceptionTypes();
			if (exceptions.length > 1)
				continue;
			
			if (exceptions.length == 1 &&
					!exceptions[0].equals(RequestCycleException.class))
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
	 */
	
	private static void invokeTargetMethod(Object target, Method method, Object[] args)
		throws RequestCycleException
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Invoking listener method " + method + " on " + target);
		
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
					throw (RequestCycleException)inner;
				
				throw ex;
			}
		}
		catch (RequestCycleException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw new ApplicationRuntimeException("Unable to invoke method " +
						method.getName() + " on " + target + ": " + ex.getMessage(),
					ex);
		}
	}

	public String toString()
	{
		return "ListenerMap[" + target + "]";
	}
}

