// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

/**
 * Creates a new instance of {@link org.apache.tapestry.IBinding}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface BindingFactory
{
    /**
     * Creates a new binding instance.
     * 
     * @param root
     *        the component that is the source of properties or messages (or
     *        etc.). When the path is "evaluated", the root component provides a
     *        context.
     * @param path
     *        The path used to get (or update) a value for the path. This may be
     *        an OGNL expression, a message key, a literal value, or otherwise
     *        defined by the type of binding.
     * @param location
     *        The location of the binding, used to report any errors related to
     *        the binding, or to the component parameter the binding is bound
     *        to.
     */
    public IBinding createBinding(IComponent root, String path, Location location);
}