package net.sf.tapestry.junit.mock.c5;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.listener.ListenerMap;

/**
 *  For the Block tests, this simply stores a flag indicating whether
 *  the particular element (DirectLink, ActionLink or Form) has been
 *  triggered, as well as listener methods.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
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
