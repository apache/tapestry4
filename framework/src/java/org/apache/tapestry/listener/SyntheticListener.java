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

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * @author Howard Lewis Ship
 * @since 3.1
 */

public class SyntheticListener implements IActionListener
{
    private Object _target;

    private Method _method;

    public SyntheticListener(Object target, Method method)
    {
        _target = target;
        _method = method;
    }

    private void invoke(IRequestCycle cycle)
    {
        Object[] args = new Object[]
        { cycle };

        ListenerMap.invokeTargetMethod(_target, _method, args);
    }

    public void actionTriggered(IComponent component, IRequestCycle cycle)
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