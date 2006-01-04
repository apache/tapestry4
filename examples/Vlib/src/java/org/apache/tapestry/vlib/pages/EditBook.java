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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Edits the properties of at book.
 * 
 * @author Howard Lewis Ship
 */

@Meta("page-type=MyLibrary")
public abstract class EditBook extends VlibPage implements PageBeginRenderListener
{
    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    public abstract String getPublisherName();

    @Persist("client")
    public abstract Integer getBookId();

    public abstract void setBookId(Integer bookId);

    /**
     * Invoked (from {@link MyLibrary}) to begin editting a book. Gets the attributes from the
     * {@link org.apache.tapestry.vlib.ejb.IBook} and updates the request cycle to render this page,
     */

    public void beginEdit(final Integer bookId)
    {
        setBookId(bookId);

        RemoteCallback<Map> callback = new RemoteCallback()
        {
            public Map doRemote() throws RemoteException
            {
                try
                {
                    return getOperations().getBookAttributes(bookId);

                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        Map attributes = getRemoteTemplate().execute(
                callback,
                "Error setting up page for book #" + bookId + ".");

        setAttributes(attributes);

        getRequestCycle().activate(this);
    }

    @Message
    public abstract String needPublisherName();

    @Message
    public abstract String leavePublisherNameEmpty();

    @Message
    public abstract String updatedBook(Object title);

    /**
     * Used to update the book when the form is submitted.
     */

    public IPage formSubmit(IRequestCycle cycle)
    {
        final Map attributes = getAttributes();

        final Integer publisherId = (Integer) attributes.get("publisherId");
        final String publisherName = getPublisherName();

        if (publisherId == null && HiveMind.isBlank(publisherName))
        {
            setErrorField("inputPublisherName", needPublisherName());
            return null;
        }

        if (publisherId != null && HiveMind.isNonBlank(publisherName))
        {
            setErrorField("inputPublisherName", leavePublisherNameEmpty());
            return null;
        }

        // Check for an error from a validation field

        if (isInError())
            return null;

        final Integer bookId = getBookId();

        RemoteCallback callback = new RemoteCallback()
        {
            public Object doRemote() throws RemoteException
            {

                try
                {
                    IOperations operations = getOperations();

                    if (publisherId != null)
                        operations.updateBook(bookId, attributes);
                    else
                    {
                        operations.updateBook(bookId, attributes, publisherName);
                    }

                    return null;
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
                catch (CreateException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }

            }

        };

        getRemoteTemplate().execute(callback, "Error updating book #" + bookId + ".");

        getModelSource().clear();

        MyLibrary page = (MyLibrary) cycle.getPage("MyLibrary");

        page.setMessage(updatedBook(attributes.get("title")));

        return page;
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
            setAttributes(new HashMap());
    }

}