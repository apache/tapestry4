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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ListenerMapImpl implements ListenerMap
{
    private final Object _target;

    /**
     * Keyed on String method name, value is
     * {@link org.apache.tapestry.listener.ListenerMethodInvoker}.
     */

    private final Map _invokers;

    private final Map _listeners = new HashMap();

    public ListenerMapImpl(Object target, Map invokers)
    {
        Defense.notNull(target, "target");
        Defense.notNull(invokers, "invokers");

        _target = target;
        _invokers = invokers;
    }

    public boolean canProvideListener(String name)
    {
        return _invokers.containsKey(name);
    }

    public synchronized IActionListener getListener(String name)
    {
        IActionListener result = (IActionListener) _listeners.get(name);

        if (result == null)
        {
            result = createListener(name);
            _listeners.put(name, result);
        }

        return result;
    }

    private IActionListener createListener(String name)
    {
        ListenerMethodInvoker invoker = (ListenerMethodInvoker) _invokers.get(name);

        if (invoker == null)
            throw new ApplicationRuntimeException(ListenerMessages.objectMissingMethod(
                    _target,
                    name), _target, null, null);

        return new SyntheticListener(_target, invoker);
    }

    public Collection getListenerNames()
    {
        return Collections.unmodifiableCollection(_invokers.keySet());
    }
}