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

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.VirtualLibraryDelegate;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Second page in Transfer Books wizard; allows the books owned by the from user to be selected and
 * transfered to the target user.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

@Meta(
{ "page-type=TransferBooks", "admin-page=true" })
public abstract class TransferBooksTransfer extends VlibPage implements PageBeginRenderListener
{
    public abstract Person getFromUser();

    public abstract void setFromUser(Person fromUser);

    @Persist("client")
    public abstract Integer getFromUserId();

    public abstract void setFromUserId(Integer fromUserId);

    @Persist("client")
    public abstract Integer getToUserId();

    public abstract void setToUserId(Integer toUserId);

    public abstract Person getToUser();

    public abstract void setToUser(Person toUser);

    public abstract List getSelectedBooks();

    public abstract IPropertySelectionModel getUserBookModel();

    public abstract void setUserBookModel(IPropertySelectionModel userBookModel);

    @Bean(VirtualLibraryDelegate.class)
    public abstract IValidationDelegate getValidationDelegate();

    @Message
    public abstract String userHasNoBooks(String userName);

    @InjectPage("admin/TransferBooksSelect")
    public abstract TransferBooksSelect getSelectPage();

    @Message
    public abstract String transferedBooks(int count, String fromName, String toName);

    public void activate(Integer fromUserId, Integer toUserId)
    {
        Person fromUser = getRemoteTemplate().getPerson(fromUserId);

        IPropertySelectionModel model = buildUserBookModel(fromUser);

        if (model.getOptionCount() == 0)
        {
            TransferBooksSelect page = getSelectPage();
            page.setError(userHasNoBooks(fromUser.getNaturalName()));
            return;
        }

        setFromUserId(fromUserId);
        setFromUser(fromUser);

        setToUserId(toUserId);

        setUserBookModel(model);

        getRequestCycle().activate(this);
    }

    /**
     * Only properties toUserPK and fromUserPK are persistent. We need to refresh the fromUser,
     * toUser and userBookModel properties at the start of each request cycle.
     */

    public void pageBeginRender(PageEvent event)
    {
        Person fromUser = getFromUser();

        if (fromUser == null)
        {
            fromUser = getRemoteTemplate().getPerson(getFromUserId());
            setFromUser(fromUser);
        }

        if (getUserBookModel() == null)
            setUserBookModel(buildUserBookModel(fromUser));

        Person toUser = getToUser();
        if (toUser == null)
        {
            toUser = getRemoteTemplate().getPerson(getToUserId());
            setToUser(toUser);
        }
    }

    public IPage formSubmit()
    {
        List selectedBooks = getSelectedBooks();

        final Integer[] keys = (Integer[]) selectedBooks.toArray(new Integer[0]);
        final Person toUser = getToUser();

        RemoteCallback callback = new RemoteCallback()
        {
            public Object doRemote() throws RemoteException
            {
                try
                {
                    getOperations().transferBooks(toUser.getId(), keys);
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }

                return null;
            }
        };

        getRemoteTemplate().execute(callback, "Unable to transfer ownership of books.");

        Person fromUser = getFromUser();

        TransferBooksSelect selectPage = getSelectPage();

        selectPage.setMessage(transferedBooks(keys.length, fromUser.getNaturalName(), toUser
                .getNaturalName()));

        return selectPage;
    }

    private IPropertySelectionModel buildUserBookModel(final Person user)
    {
        RemoteCallback<Book[]> callback = new RemoteCallback()
        {
            public Book[] doRemote() throws RemoteException
            {
                IBookQuery query = getBookQuerySource().newQuery();

                int count = query.ownerQuery(user.getId(), null);

                return count > 0 ? query.get(0, count) : null;
            }
        };

        Book[] books = getRemoteTemplate().execute(
                callback,
                "Unable to retrieve books owned by " + user.getNaturalName() + ".");

        EntitySelectionModel model = new EntitySelectionModel();

        if (books != null)
            for (int i = 0; i < books.length; i++)
                model.add(books[i].getId(), books[i].getTitle());

        return model;
    }

}
