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
 * Terminator implementation for the tapestry.listener.InvokeListener pipeline; this is what finally
 * turns around and invokes
 * {@link org.apache.tapestry.IActionListener#actionTriggered(IComponent, IRequestCycle)}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ListenerInvokerTerminator implements ListenerInvoker
{

    /**
     * @param listener
     *            to be invoked, may be null if no listener is found (a convienience for all the
     *            places where listeners are optional)
     * @param source
     *            the component generating the listener "event", to be passed to the listener. Not
     *            generally used, but may not be null.
     * @param cycle
     *            the current request cycle, to be passed to the listener.
     * @param delegate
     *            the next invoker in the pipeline
     */
    public void invokeListener(IActionListener listener, IComponent source, IRequestCycle cycle)
    {
        Defense.notNull(source, "source");
        Defense.notNull(cycle, "cycle");

        if (listener != null)
            listener.actionTriggered(source, cycle);
    }

}