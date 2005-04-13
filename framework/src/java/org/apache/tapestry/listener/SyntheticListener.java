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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * Adapter class that combines a target object (typically, a component) with a
 * {@link org.apache.tapestry.listener.ListenerMethodInvoker}. This is the bridge from listener
 * method names to listener method invocations.
 * <p>
 * TODO: It would really be nice if we could get the location of the listener binding into thrown
 * exceptions. As implemented, as best, it will be the location of the &lt;page-specification&gt;
 * (or &lt;component&gt;) of the page (or component) containing the listener method.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SyntheticListener implements IActionListener
{
    private final Object _target;

    private final ListenerMethodInvoker _invoker;

    public SyntheticListener(Object target, ListenerMethodInvoker invoker)
    {
        Defense.notNull(target, "target");
        Defense.notNull(invoker, "invoker");

        _target = target;
        _invoker = invoker;
    }

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    {
        _invoker.invokeListenerMethod(_target, cycle);
    }
}