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
 *  @since 3.0
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
