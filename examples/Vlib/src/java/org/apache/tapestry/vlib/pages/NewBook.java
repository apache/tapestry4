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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Collects information for a new book.
 * 
 * @author Howard Lewis Ship
 */

public abstract class NewBook extends VlibPage implements PageBeginRenderListener
{
    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    public abstract String getPublisherName();

    @Message
    public abstract String needPublisherName();

    @Message
    public abstract String leavePublisherNameEmpty();

    @Message
    public abstract String addedBook(Object title);

    @InjectComponent("publisherName")
    public abstract IFormComponent getPublisherNameField();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    public void addBook()
    {
        IValidationDelegate delegate = getValidationDelegate();

        final Map attributes = getAttributes();

        final Integer publisherId = (Integer) attributes.get("publisherId");
        final String publisherName = getPublisherName();

        if (publisherId == null && HiveMind.isBlank(publisherName))
        {
            delegate.record(getPublisherNameField(), needPublisherName());
            return;
        }

        if (publisherId != null && HiveMind.isNonBlank(publisherName))
        {
            delegate.record(getPublisherNameField(), leavePublisherNameEmpty());
            return;
        }

        if (isInError())
            return;

        Visit visit = getVisitState();

        Integer userId = visit.getUserId();

        attributes.put("ownerId", userId);
        attributes.put("holderId", userId);

        RemoteCallback<Boolean> callback = new RemoteCallback()
        {
            public Boolean doRemote() throws RemoteException
            {
                IOperations operations = getOperations();

                try
                {
                    if (publisherId != null)
                    {
                        operations.addBook(attributes);

                        return false;
                    }

                    operations.addBook(attributes, publisherName);

                    return true;
                }
                catch (CreateException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        boolean clearCache = getRemoteTemplate().execute(callback, "Error adding new book.");

        if (clearCache)
            getModelSource().clear();

        // Success. First, update the message property of the return page.

        MyLibrary myLibrary = getMyLibrary();

        myLibrary.setMessage(addedBook(attributes.get("title")));

        myLibrary.activate();
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
        {
            Map attributes = new HashMap();

            // Setup defaults for the new book.

            attributes.put("lendable", true);

            setAttributes(attributes);
        }
    }

}