/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Category;

import net.sf.tapestry.*;
import net.sf.tapestry.Tapestry;

/**
 *  An implementation of the Decorator pattern.  The decorator
 *  pattern allows new functionality to be assigned to an existing class.
 *  As implemented here, the Decorator object is a smart lookup between
 *  a particular class (the class to be Decorated, called
 *  the <em>subject class</em>) and some object instance
 *  that can provide the extra functionality (called the
 *  <em>adaptor</em>).  The implementation of the adaptor is not relevant
 *  to the Decorator class.
 *
 *  <p>Adaptors are registered before they can be used; the registration maps a
 *  particular class to an adaptor instance.  The adaptor instance will be used
 *  when the subject class matches the registered class, or the subject class
 *  inherits from the registered class.
 *
 *  <p>This means that a search must be made that walks the inheritance tree
 *  (upwards from the subject class) to find a registered mapping.
 *
 *  <p>In addition, adaptors can be registered against <em>interfaces</em>.
 *  Searching of interfaces occurs after searching of classes.  The exact order is:
 *
 *  <ul>
 *  <li>Search for the subject class, then each super-class of the subject class
 *      (excluding java.lang.Object)
 *  <li>Search interfaces, starting with interfaces implemented by the subject class,
 *  continuing with interfaces implemented by the super-classes, then
 *  interfaces extended by earlier interfaces (the exact order is a bit fuzzy)
 *  <li>Search for a match for java.lang.Object, if any
 *  </ul>
 *
 *  <p>The first match terminates the search.
 *
 *  <p>The Decorator caches the results of search; a subsequent search for the
 *  same subject class will be resolved immediately.
 *
 *  <p>This class is thread safe.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 **/

public class Decorator
{
	private static final Category CAT = Category.getInstance(Decorator.class);

	/**
	 *  A Map of adaptor objects, keyed on registration Class.
	 *
	 **/

	private Map registrations = new HashMap();

	/**
	 *  A Map of adaptor objects, keyed on subject Class.
	 *
	 **/

	private Map cache = new HashMap();

	/**
	 *  Registers an adaptor for a registration class.
	 *
	 *  @throws IllegalArgumentException if an adaptor has already
	 *  been registered for the given class.
	 **/

	public void register(Class registrationClass, Object adaptor)
	{
		synchronized (registrations)
		{
			if (registrations.containsKey(registrationClass))
				throw new IllegalArgumentException(
					Tapestry.getString("Decorator.duplicate-registration", registrationClass.getName()));

			registrations.put(registrationClass, adaptor);
		}

			if (CAT.isInfoEnabled())
				CAT.info("Registered " + adaptor + " for " + registrationClass.getName());

		// Can't tell what is and isn't valid in the cache.
		// Also, normally all registrations occur before any adaptors
		// are searched for, so this is not a big deal.

        synchronized (cache)
	    {
			cache.clear();
	    }
	}

	/**
	 *  Gets the adaptor for the specified subjectClass.
	 *
	 *  @throws IllegalArgumentException if no adaptor could be found.
	 *
	 **/

	public Object getAdaptor(Class subjectClass)
	{
		Object result;

		if (CAT.isDebugEnabled())
			CAT.debug("Getting adaptor for class " + subjectClass.getName());

		synchronized (cache)
		{
			result = cache.get(subjectClass);

			if (result != null)
			{
				if (CAT.isDebugEnabled())
					CAT.debug("Found " + result + " in cache");

				return result;
			}
		}

			result = searchForAdaptor(subjectClass);

			// Record the result in the cache

			synchronized (cache)
			{
				cache.put(subjectClass, result);
			}

				if (CAT.isDebugEnabled())
					CAT.debug("Found " + result);

		return result;
	}

	/**
	 * Searches the registration Map for a match, based on inheritance.
	 *
	 * <p>Searches class inheritance first, then interfaces (in a rather vague order).
	 * Really should match the order from the JVM spec.
	 *
	 * <p>There's a degenerate case where we may check the same interface more than once:
	 * <ul>
	 * <li>Two interfaces, I1 and I2
	 * <li>Two classes, C1 and C2
	 * <li>I2 extends I1
	 * <li>C2 extends C1
	 * <li>C1 implements I1
	 * <li>C2 implements I2
	 * <li>The search will be: C2, C1, I2, I1, I1
	 * <li>I1 is searched twice, because C1 implements it, and I2 extends it
	 * <li>There are other such cases, but none of them cause infinite loops
	 * and most are rare (we could guard against it, but its relatively expensive).
	 * <li>Multiple checks only occur if we don't find a registration
	 * </ul>
	 *
	 **/

	private Object searchForAdaptor(Class subjectClass)
	{
		LinkedList queue = null;
		Class[] interfaces;
		Class searchClass;
		Object result;
		int length;

		if (registrations == null)
			throw new IllegalArgumentException("No adaptors have been registered.");

		if (CAT.isDebugEnabled())
			CAT.debug("Searching for adaptor for class " + subjectClass.getName());

		synchronized (registrations)
		{
			// Step one: work up through the class inheritance.

			searchClass = subjectClass;

			// Primitive types have null, not Object, as their parent
			// class.

			while (searchClass != Object.class && searchClass != null)
			{
				result = registrations.get(searchClass);
				if (result != null)
					return result;

				// Not an exact match.  If the search class
				// implements any interfaces, add them to the queue.

				interfaces = searchClass.getInterfaces();
				length = interfaces.length;

				if (queue == null && length > 0)
					queue = new LinkedList();

				for (int i = 0; i < length; i++)
					queue.addLast(interfaces[i]);

				// Advance up to the next superclass

				searchClass = searchClass.getSuperclass();

			}

			// Ok, the easy part failed, lets start searching
			// interfaces.

			if (queue != null)
			{
				while (!queue.isEmpty())
				{
					searchClass = (Class) queue.removeFirst();

					result = registrations.get(searchClass);
					if (result != null)
						return result;

					// Interfaces can extend other interfaces; add them
					// to the queue.

					interfaces = searchClass.getInterfaces();
					length = interfaces.length;

					for (int i = 0; i < length; i++)
						queue.addLast(interfaces[i]);
				}
			}

			// Not a match on interface; our last gasp is to check
			// for a registration for java.lang.Object

			result = registrations.get(Object.class);
			if (result != null)
				return result;

		}

			// No match?  That's rare ... and an error.

			throw new IllegalArgumentException(
				Tapestry.getString("Decorator.adaptor-not-found", subjectClass.getName()));
	}

	public String toString()
	{
		StringBuffer buffer;
		Iterator i;
		Map.Entry entry;
		boolean first = true;
		Class registeredClass;

		buffer = new StringBuffer();
		buffer.append("Decorator[");

		if (registrations != null)
		{
			synchronized (registrations)
			{
				i = registrations.entrySet().iterator();

				while (i.hasNext())
				{
					if (!first)
						buffer.append(' ');

					entry = (Map.Entry) i.next();

					registeredClass = (Class) entry.getKey();

					buffer.append(registeredClass.getName());
					buffer.append("=");
					buffer.append(entry.getValue());

					first = false;
				}
			}
		}

		buffer.append("]");

		return buffer.toString();

	}
}