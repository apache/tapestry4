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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.event.ResetEventListener;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ListenerMapSourceImpl implements ListenerMapSource, ResetEventListener
{
    /**
     * Sorts {@link Method}s into descending order by parameter count.
     */

    private static class ParameterCountComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            Method m1 = (Method) o1;
            Method m2 = (Method) o2;

            return m2.getParameterTypes().length - m1.getParameterTypes().length;
        }

    }

    /**
     * Keyed on Class, value is a Map. The inner Map is an invoker map ... keyed on listener method
     * name, value is {@link org.apache.tapestry.listener.ListenerMethodInvoker}.
     */

    private final Map _classToInvokerMap = new HashMap();

    public ListenerMap getListenerMapForObject(Object object)
    {
        Defense.notNull(object, "object");

        Class objectClass = object.getClass();

        Map invokerMap = findInvokerMap(objectClass);

        return new ListenerMapImpl(object, invokerMap);
    }

    public synchronized void resetEventDidOccur()
    {
        _classToInvokerMap.clear();
    }

    private synchronized Map findInvokerMap(Class targetClass)
    {
        Map result = (Map) _classToInvokerMap.get(targetClass);

        if (result == null)
        {
            result = buildInvokerMapForClass(targetClass);
            _classToInvokerMap.put(targetClass, result);
        }

        return result;
    }

    private Map buildInvokerMapForClass(Class targetClass)
    {
        // map, keyed on method name, value is List of Method
        // only public void methods go into this map.

        Map map = new HashMap();

        Method[] methods = targetClass.getMethods();

        // Sort all the arrays, just once, and the methods will be
        // added to the individual lists in the correct order
        // (descending by parameter count).

        Arrays.sort(methods, new ParameterCountComparator());

        for (int i = 0; i < methods.length; i++)
        {
            Method m = methods[i];

            if (m.getReturnType() != void.class)
                continue;

            if (Modifier.isStatic(m.getModifiers()))
                continue;

            String name = m.getName();

            addMethodToMappedList(map, m, name);
        }

        return convertMethodListMapToInvokerMap(map);
    }

    private Map convertMethodListMapToInvokerMap(Map map)
    {
        Map result = new HashMap();

        Iterator i = map.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();
            List methodList = (List) e.getValue();

            Method[] methods = convertMethodListToArray(methodList);

            ListenerMethodInvoker invoker = createListenerMethodInvoker(name, methods);

            result.put(name, invoker);
        }

        return result;
    }

    /**
     * This implementation returns a new
     * {@link org.apache.tapestry.listener.ListenerMethodInvokerImpl}. Subclasses can override to
     * provide their own implementation.
     */

    protected ListenerMethodInvokerImpl createListenerMethodInvoker(String name, Method[] methods)
    {
        return new ListenerMethodInvokerImpl(name, methods);
    }

    private Method[] convertMethodListToArray(List methodList)
    {
        int size = methodList.size();
        Method[] result = new Method[size];

        return (Method[]) methodList.toArray(result);
    }

    private void addMethodToMappedList(Map map, Method m, String name)
    {
        List l = (List) map.get(name);

        if (l == null)
        {
            l = new ArrayList();
            map.put(name, l);
        }

        l.add(m);
    }
}