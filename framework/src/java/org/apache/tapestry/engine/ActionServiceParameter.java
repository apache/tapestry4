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

package org.apache.tapestry.engine;

import org.apache.hivemind.Defense;
import org.apache.tapestry.IComponent;

/**
 * Parameter object used with {@link org.apache.tapestry.engine.ActionService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ActionServiceParameter
{

    private IComponent _component;

    private String _actionId;

    public ActionServiceParameter(IComponent component, String actionId)
    {
        Defense.notNull(component, "component");
        Defense.notNull(actionId, "actionId");

        _component = component;
        _actionId = actionId;
    }

    public String getActionId()
    {
        return _actionId;
    }

    public IComponent getComponent()
    {
        return _component;
    }
}