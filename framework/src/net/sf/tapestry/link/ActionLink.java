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
 *  [<a href="../../../../../ComponentReference/ActionLink.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ActionLink extends GestureLink implements IAction
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