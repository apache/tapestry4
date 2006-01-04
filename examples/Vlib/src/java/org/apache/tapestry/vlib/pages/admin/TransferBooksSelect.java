// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.VirtualLibraryDelegate;
import org.apache.tapestry.vlib.VlibPage;

/**
 * First page in Transfer Books wizard; allows the two users to be selected.
 * 
 * @author Howard Lewis Ship
 */
@Meta(
{ "page-type=TransferBooks", "admin-page=true" })
public abstract class TransferBooksSelect extends VlibPage
{
    public abstract Integer getFromUserId();

    public abstract Integer getToUserId();

    @Message
    public abstract String selectDifferentUsers();

    @InjectComponent("to")
    public abstract IFormComponent getToField();

    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    @InjectPage("admin/TransferBooksTransfer")
    public abstract TransferBooksTransfer getNextPage();

    public void formSubmit()
    {
        Integer fromUserId = getFromUserId();
        Integer toUserId = getToUserId();

        if (fromUserId.equals(toUserId))
        {
            getValidationDelegate().record(getToField(), selectDifferentUsers());
            return;
        }

        TransferBooksTransfer next = getNextPage();

        next.activate(fromUserId, toUserId);
    }

}