package net.sf.tapestry.vlib;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageRedirectException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.callback.PageCallback;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.vlib.pages.Login;

/**
 *  Base page used for pages that should be protected by the {@link Login} page.
 *  If the user is not logged in, they are redirected to the Login page first.
 *  Also, implements an error property and a validationDelegate.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Protected extends BasePage implements IErrorProperty
{
    private String _error;
    private IValidationDelegate _validationDelegate;

    public void detach()
    {
        _error = null;
        _validationDelegate = null;

        super.detach();
    }

    public IValidationDelegate getValidationDelegate()
    {
        if (_validationDelegate == null)
            _validationDelegate = new SimpleValidationDelegate();

        return _validationDelegate;
    }

    public void setError(String value)
    {
        _error = value;
    }

    public String getError()
    {
        return _error;
    }

    protected void setErrorField(String componentId, String message, String value)
    {
        IFormComponent component = (IFormComponent) getComponent(componentId);

        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(component);
        delegate.record(message, null, value);

    }

    /**
     *  Returns true if the delegate indicates an error, or the error property is not null.
     *
     **/

    protected boolean isInError()
    {
        return _error != null || getValidationDelegate().getHasErrors();
    }

    /**
     *  Checks if the user is logged in.  If not, they are sent
     *  to the {@link Login} page before coming back to whatever this
     *  page is.
     *
     **/

    public void validate(IRequestCycle cycle) throws RequestCycleException
    {
        Visit visit = (Visit) getVisit();

        if (visit != null && visit.isUserLoggedIn())
            return;

        // User not logged in ... redirect through the Login page.

        Login login = (Login) cycle.getPage("Login");

        login.setCallback(new PageCallback(this));

        throw new PageRedirectException(login);
    }
}