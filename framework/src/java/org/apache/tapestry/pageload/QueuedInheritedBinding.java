// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

/**
 * Handles connecting an inherited binding.  These will be going away soon (if not in
 * release 4.0 itself).
 *
 * @author Howard Lewis Ship
 * @since 4.0
 */
class QueuedInheritedBinding implements IQueuedInheritedBinding
{
    private IComponent _component;
    private String _containerParameterName;
    private String _parameterName;

    QueuedInheritedBinding(
        IComponent component,
        String containerParameterName,
        String parameterName)
    {
        _component = component;
        _containerParameterName = containerParameterName;
        _parameterName = parameterName;
    }

    public void connect()
    {
        IBinding binding = _component.getContainer().getBinding(_containerParameterName);

        if (binding == null)
            return;

        _component.setBinding(_parameterName, binding);
    }
}