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

import java.util.List;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;

/**
 *  A component for creating a link using the direct service; used for actions that
 *  are not dependant on dynamic page state.
 *
 *  [<a href="../../../../../ComponentReference/DirectLink.html">Component Reference</a>]
 *
 * @author Howard Lewis Ship
 * @version $Id$
 *
 **/

public abstract class DirectLink extends AbstractLinkComponent implements IDirect
{

    public abstract IBinding getStatefulBinding();
    public abstract IActionListener getListener();

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.  May be invoked when not renderring.
     *
     **/

    public boolean isStateful()
    {
        IBinding statefulBinding = getStatefulBinding();

        if (statefulBinding == null)
            return true;

        return statefulBinding.getBoolean();
    }

    public ILink getLink(IRequestCycle cycle)
    {
        return getLink(cycle, Tapestry.DIRECT_SERVICE, constructServiceParameters(getParameters()));
    }

    /**
     *  Converts a service parameters value to an array
     *  of objects.  
     *  This is used by the {@link DirectLink}, {@link ServiceLink}
     *  and {@link ExternalLink}
     *  components.
     *
     *  @param parameterValue the input value which may be
     *  <ul>
     *  <li>null  (returns null)
     *  <li>An array of Object (returns the array)
     *  <li>A {@link List} (returns an array of the values in the List})
     *  <li>A single object (returns the object as a single-element array)
     *  </ul>
     * 
     *  @return An array representation of the input object.
     * 
     *  @since 2.2
     **/

    public static Object[] constructServiceParameters(Object parameterValue)
    {
        if (parameterValue == null)
            return null;

        if (parameterValue instanceof Object[])
            return (Object[]) parameterValue;

        if (parameterValue instanceof List)
        {
            List list = (List) parameterValue;

            return list.toArray();
        }

        return new Object[] { parameterValue };
    }

    /**
     *  Invoked by the direct service to trigger the application-specific
     *  action by notifying the {@link IActionListener listener}.
     *
     *  @throws org.apache.tapestry.StaleSessionException if the component is stateful, and
     *  the session is new.
     * 
     **/

    public void trigger(IRequestCycle cycle)
    {
        IActionListener listener = getListener();
        
        if (listener == null)
        	throw Tapestry.createRequiredParameterException(this, "listener");

        listener.actionTriggered(this, cycle);
    }

    /** @since 2.2 **/

    public abstract Object getParameters();
}