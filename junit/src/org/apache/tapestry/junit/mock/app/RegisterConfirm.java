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

package org.apache.tapestry.junit.mock.app;

import java.util.ResourceBundle;

import org.apache.tapestry.html.BasePage;

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
            _ageRangeStrings = ResourceBundle.getBundle("org.apache.tapestry.junit.mock.app.AgeRangeStrings", getLocale());

        String key = _user.getAgeRange().getName();

        return _ageRangeStrings.getString(key);
    }
}
