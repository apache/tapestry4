//
// Tapestry Web Application Framework
// Copyright (c) 2002 by Howard Lewis Ship
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
