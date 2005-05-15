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

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

/**
 * Filter interface used with {@link org.apache.tapestry.listener.ListenerInvoker}. Implementations
 * of this filter interface may be plugged into the listener method invocation pipeline. Typical
 * applications involve handling transactions.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ListenerInvokerFilter
{
    public void invokeListener(IActionListener listener, IComponent source, IRequestCycle cycle,
            ListenerInvoker delegate);
}
