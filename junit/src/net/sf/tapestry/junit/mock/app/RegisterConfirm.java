package net.sf.tapestry.junit.mock.app;

import java.util.ResourceBundle;

import net.sf.tapestry.html.BasePage;

/**
 *  Part of Mock application, displays the data input on the Register page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class RegisterConfirm extends BasePage
{
    private ResourceBundle _ageRangeStrings;

    private User _user;

    public void detach()
    {
        _user = null;

        super.detach();
    }

    public User getUser()
    {
        return _user;
    }

    public void setUser(User user)
    {
        _user = user;
    }

    public String getFormattedAge()
    {
        if (_ageRangeStrings == null)
            _ageRangeStrings = ResourceBundle.getBundle("net.sf.tapestry.junit.mock.app.AgeRangeStrings", getLocale());

        String key = _user.getAgeRange().getName();

        return _ageRangeStrings.getString(key);
    }
}
