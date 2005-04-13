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

import org.apache.tapestry.IRequestCycle;

/**
 * An object, used by a {@link org.apache.tapestry.listener.ListenerMap}, to match requests
 * (possibly with service parameters) to methods (possibly with arguments). Given a request, a
 * (possibly null or empty) array of service parameters, and a target object (and its set of public
 * void methods), the mapping will search for the mostly likely mapping. In order:
 * <ul>
 * <li>public void method(params) (where the method takes the same number of parameters as there
 * are service parameters)
 * <li>public void method(IRequestCycle, params)
 * <li>public void method()
 * <li>public void method(IRequestCycle)
 * </ul>
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ListenerMethodInvoker
{
    public void invokeListenerMethod(Object target, IRequestCycle cycle);
}