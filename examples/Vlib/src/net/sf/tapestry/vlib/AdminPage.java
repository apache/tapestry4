package net.sf.tapestry.vlib;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageRedirectException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.PageCallback;
import net.sf.tapestry.vlib.pages.Login;

/**
 *  Base page for any pages restricted to administrators.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class AdminPage extends Protected
{
    private String _message;

    public void detach()
    {
        _message = null;

        super.detach();
    }

    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String value)
    {
        _message = value;
    }

    public void validate(IRequestCycle cycle) throws RequestCycleException
    {
        Visit visit = (Visit) getEngine().getVisit();

        if (visit == null || !visit.isUserLoggedIn())
        {
            Login login = (Login) cycle.getPage("Login");

            login.setCallback(new PageCallback(this));

            throw new PageRedirectException(login);
        }

        if (!visit.getUser().isAdmin())
        {
            visit.getEngine().presentError("That function is restricted to adminstrators.", cycle);

            throw new PageRedirectException(cycle.getPage());
        }
    }
}