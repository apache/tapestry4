//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock.c5;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerMap;

/**
 *  For the Block tests, this simply stores a flag indicating whether
 *  the particular element (DirectLink, ActionLink or Form) has been
 *  triggered, as well as listener methods.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Visit
{
    private boolean _directLinkTriggered;
    private boolean _actionLinkTriggered;
    private boolean _formTriggered;

    private transient ListenerMap _listeners;

    public boolean isActionLinkTriggered()
    {
        return _actionLinkTriggered;
    }

    public boolean isDirectLinkTriggered()
    {
        return _directLinkTriggered;
    }

    public boolean isFormTriggered()
    {
        return _formTriggered;
    }

    public void setActionLinkTriggered(boolean actionLinkTriggered)
    {
        _actionLinkTriggered = actionLinkTriggered;
    }

    public void setDirectLinkTriggered(boolean directLinkTriggered)
    {
        _directLinkTriggered = directLinkTriggered;
    }

    public void setFormTriggered(boolean formTriggered)
    {
        _formTriggered = formTriggered;
    }

    public void directTrigger(IRequestCycle cycle)
    {
        setDirectLinkTriggered(true);
    }

    public void actionTrigger(IRequestCycle cycle)
    {
        setActionLinkTriggered(true);
    }

    public void formTrigger(IRequestCycle cycle)
    {
        setFormTriggered(true);
    }

    public ListenerMap getListeners()
    {
        if (_listeners == null)
            _listeners = new ListenerMap(this);

        return _listeners;
    }
}
