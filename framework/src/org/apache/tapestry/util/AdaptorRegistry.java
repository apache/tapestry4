//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.Tapestry;

/**
 *  An implementation of the <b>Adaptor</b> pattern.  The adaptor
 *  pattern allows new functionality to be assigned to an existing class.
 *  As implemented here, this is a smart lookup between
 *  a particular class (the class to be adapted, called
 *  the <em>subject class</em>) and some object instance
 *  that can provide the extra functionality (called the
 *  <em>adaptor</em>).  The implementation of the adaptor is not relevant
 *  to the AdaptorRegistry class.
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
 *  <p>The AdaptorRegistry caches the results of search; a subsequent search for the
 *  same subject class will be resolved immediately.
 * 
 *  <p>AdaptorRegistry does a minor tweak of the "natural" inheritance.
 *  Normally, the parent class of an object array (i.e., <code>Foo[]</code>) is
 *  simply <code>Object</code>, even though you may assign 
 *  <code>Foo[]</code> to a variable of type <code>Object[]</code>.  AdaptorRegistry
 *  "fixes" this by searching for <code>Object[]</code> as if it was the superclass of
 *  any object array.  This means that the search path for <code>Foo[]</code> is
 *  <code>Foo[]</code>, <code>Object[]</code>, then a couple of interfaces 
 *  {@link java.lang.Cloneable}, {@link java.io.Serializable}, etc. that are\
 *  implicitily implemented by arrarys), and then, finally, <code>Object</code>
 * 
 *  <p>
 *  This tweak doesn't apply to scalar arrays, since scalar arrays may <em>not</em>
 *  be assigned to <code>Object[]</code>. 
 *
 *  <p>This class is thread safe.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class AdaptorRegistry
{
    private static final Log LOG = LogFactory.getLog(AdaptorRegistry.class);

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

    public synchronized void register(Class registrationClass, Object adaptor)
    {
        if (registrations.containsKey(registrationClass))
            throw new IllegalArgumentException(
                Tapestry.format(
                    "AdaptorRegistry.duplicate-registration",
                    Tapestry.getClassName(registrationClass)));

        registrations.put(registrationClass, adaptor);

        if (LOG.isDebugEnabled())
            LOG.debug("Registered " + adaptor + " for " + Tapestry.getClassName(registrationClass));

        // Can't tell what is and isn't valid in the cache.
        // Also, normally all registrations occur before any adaptors
        // are searched for, so this is not a big deal.

        cache.clear();
    }

    /**
     *  Gets the adaptor for the specified subjectClass.
     *
     *  @throws IllegalArgumentException if no adaptor could be found.
     *
     **/

    public synchronized Object getAdaptor(Class subjectClass)
    {
        Object result;

        if (LOG.isDebugEnabled())
            LOG.debug("Getting adaptor for class " + Tapestry.getClassName(subjectClass));

        result = cache.get(subjectClass);

        if (result != null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Found " + result + " in cache");

            return result;
        }

        result = searchForAdaptor(subjectClass);

        // Record the result in the cache

        cache.put(subjectClass, result);

        if (LOG.isDebugEnabled())
            LOG.debug("Found " + result);

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
     *  <p>
     *  This method is only called from a synchronized block, so it is
     *  implicitly synchronized.
     * 
     **/

    private Object searchForAdaptor(Class subjectClass)
    {
        LinkedList queue = null;
        Object result = null;

        if (LOG.isDebugEnabled())
            LOG.debug("Searching for adaptor for class " + Tapestry.getClassName(subjectClass));

        // Step one: work up through the class inheritance.

        Class searchClass = subjectClass;

        // Primitive types have null, not Object, as their parent
        // class.

        while (searchClass != Object.class && searchClass != null)
        {
            result = registrations.get(searchClass);
            if (result != null)
                return result;

            // Not an exact match.  If the search class
            // implements any interfaces, add them to the queue.

            Class[] interfaces = searchClass.getInterfaces();
            int length = interfaces.length;

            if (queue == null && length > 0)
                queue = new LinkedList();

            for (int i = 0; i < length; i++)
                queue.addLast(interfaces[i]);

            // Advance up to the next superclass

            searchClass = getSuperclass(searchClass);

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

                Class[] interfaces = searchClass.getInterfaces();
                int length = interfaces.length;

                for (int i = 0; i < length; i++)
                    queue.addLast(interfaces[i]);
            }
        }

        // Not a match on interface; our last gasp is to check
        // for a registration for java.lang.Object

        result = registrations.get(Object.class);
        if (result != null)
            return result;

        // No match?  That's rare ... and an error.

        throw new IllegalArgumentException(
            Tapestry.format(
                "AdaptorRegistry.adaptor-not-found",
                Tapestry.getClassName(subjectClass)));
    }

    /**
     *  Returns the superclass of the given class, with a single tweak:  If the 
     *  search class is an array class, and the component type is an object class
     *  (but not Object), then the simple Object array class is returned.  This reflects
     *  the fact that an array of any class may be assignable to <code>Object[]</code>,
     *  even though the superclass of an array is always simply <code>Object</code>.
     * 
     **/

    private Class getSuperclass(Class searchClass)
    {
        if (searchClass.isArray())
        {
            Class componentType = searchClass.getComponentType();

            if (!componentType.isPrimitive() && componentType != Object.class)
                return Object[].class;
        }

        return searchClass.getSuperclass();
    }

    public synchronized String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("AdaptorRegistry[");

        Iterator i = registrations.entrySet().iterator();
        boolean showSep = false;

        while (i.hasNext())
        {
            if (showSep)
                buffer.append(' ');

            Map.Entry entry = (Map.Entry) i.next();

            Class registeredClass = (Class) entry.getKey();

            buffer.append(Tapestry.getClassName(registeredClass));
            buffer.append("=");
            buffer.append(entry.getValue());

            showSep = true;
        }

        buffer.append("]");

        return buffer.toString();
    }
}