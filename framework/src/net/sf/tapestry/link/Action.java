//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.link;

import net.sf.tapestry.IAction;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderRewoundException;
import net.sf.tapestry.RequestCycleException;

/**
 *  A component for creating a link that is handled using the action service.
 * 
 *  [<a href="../../../../../ComponentReference/Action.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Action extends GestureLink implements IAction
{
    private IActionListener _listener;
    private IBinding _statefulBinding;

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
        if (_statefulBinding == null)
            return true;
            
         return _statefulBinding.getBoolean();
    }

    /**
     *  Returns {@link IEngineService#ACTION_SERVICE}.
     * 
     **/

    protected String getServiceName()
    {
        return IEngineService.ACTION_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        String actionId;
 
        actionId = cycle.getNextActionId();

        if (cycle.isRewound(this))
        {
            _listener.actionTriggered(this, cycle);

            throw new RenderRewoundException(this);
        }

        return new Object[] { actionId };
    }
    
    public IBinding getStatefulBinding()
    {
        return _statefulBinding;
    }

    public void setStatefulBinding(IBinding statefulBinding)
    {
        _statefulBinding = statefulBinding;
    }


    public IActionListener getListener()
    {
        return _listener;
    }

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

}