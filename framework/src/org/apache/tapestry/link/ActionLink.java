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

package org.apache.tapestry.link;

import org.apache.tapestry.IAction;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;

/**
 *  A component for creating a link that is handled using the action service.
 * 
 *  [<a href="../../../../../ComponentReference/ActionLink.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class ActionLink extends AbstractLinkComponent implements IAction
{
    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.
     * 
     *  <p>Note that this method can be called when the
     *  component is not rendering, therefore it must
     *  directly access the {@link IBinding} for the stateful
     *  parameter.
     *
     **/

    public boolean getRequiresSession()
    {
    	IBinding statefulBinding = getStatefulBinding();
    	
        if (statefulBinding == null)
            return true;

        return statefulBinding.getBoolean();
    }

    public ILink getLink(IRequestCycle cycle)
    {
        String actionId = cycle.getNextActionId();

        if (cycle.isRewound(this))
        {
            getListener().actionTriggered(this, cycle);

            throw new RenderRewoundException(this);
        }

        return getLink(cycle, Tapestry.ACTION_SERVICE, new Object[] { actionId });
    }

    public abstract IBinding getStatefulBinding();

    public abstract IActionListener getListener();
}