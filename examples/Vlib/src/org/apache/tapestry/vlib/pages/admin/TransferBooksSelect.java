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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.AdminPage;

/**
 *  First page in Transfer Books wizard; allows the two users to be selected.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class TransferBooksSelect extends AdminPage
{
    public abstract Integer getFromUserId();
    public abstract Integer getToUserId();

    public void formSubmit(IRequestCycle cycle)
    {
        Integer fromUserId = getFromUserId();
        Integer toUserId = getToUserId();

        if (fromUserId.equals(toUserId))
        {
            setError(getMessage("select-different-users"));
            return;
        }

        TransferBooksTransfer next = (TransferBooksTransfer) cycle.getPage("TransferBooksTransfer");

        next.activate(cycle, fromUserId, toUserId);
    }

}