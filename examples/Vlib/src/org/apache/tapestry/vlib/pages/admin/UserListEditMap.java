package org.apache.tapestry.vlib.pages.admin;

import java.util.List;
import java.util.Set;

import org.apache.tapestry.form.ListEditMap;

/**
 *  Subclass of {@link org.apache.tapestry.form.ListEditMap}
 *  that tracks an additional boolean property, resetPassword,
 *  for each value.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class UserListEditMap extends ListEditMap
{
    private Set _resetPasswordKeys;

    public List getResetPasswordKeys()
    {
        return convertSetToList(_resetPasswordKeys);
    }

    public boolean getResetPassword()
    {
        return checkSet(_resetPasswordKeys);
    }

    public void setResetPassword(boolean resetPassword)
    {
        _resetPasswordKeys = updateSet(_resetPasswordKeys, resetPassword);
    }

}
