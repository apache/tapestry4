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

import org.apache.tapestry.IActionListener;

/**
 * @author Howard M. Lewis Ship
 */
public interface ListenerMap
{
    /**
     * Gets a listener for the given name (which is both a property name and a method name). The
     * listener is created as needed, but is also cached for later use. The returned object
     * implements the {@link org.apache.tapestry.IActionListener}.
     * 
     * @param name
     *            the name of the method to invoke (the most appropriate method will be selected if
     *            there are multiple overloadings of the same method name)
     * @returns an object implementing {@link IActionListener}.
     * @throws ApplicationRuntimeException
     *             if the listener can not be created.
     */
    public IActionListener getListener(String name);

    /**
     * Returns an unmodifiable collection of the names of the listeners implemented by the target
     * class.
     * 
     * @since 1.0.6
     */
    public Collection getListenerNames();

    /**
     * Returns true if this ListenerMapImpl can provide a listener with the given name.
     * 
     * @since 2.2
     */
    public boolean canProvideListener(String name);
}