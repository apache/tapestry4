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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.vlib.Protected;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 *  Edits the properties of at book.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public abstract class EditBook extends Protected implements PageRenderListener
{
    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    public abstract String getPublisherName();

    public abstract Integer getBookId();

    public abstract void setBookId(Integer bookId);

    /**
     *  Invoked (from {@link MyLibrary}) to begin editting a book.
     *  Gets the attributes from the {@link org.apache.tapestry.vlib.ejb.IBook} 
     *  and updates the request cycle to render this page,
     *
     **/

    public void beginEdit(IRequestCycle cycle, Integer bookId)
    {
        setBookId(bookId);

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                // Get the attributes as a source for our input fields.

                IOperations operations = vengine.getOperations();

                setAttributes(operations.getBookAttributes(bookId));

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Remote exception setting up page for book #" + bookId + ".",
                    ex,
                    i++);
            }
        }

        cycle.activate(this);
    }

    /**
     *  Used to update the book when the form is submitted.
     *
     **/

    public void formSubmit(IRequestCycle cycle)
    {
        Map attributes = getAttributes();

        Integer publisherId = (Integer) attributes.get("publisherId");
        String publisherName = getPublisherName();

        if (publisherId == null && Tapestry.isBlank(publisherName))
        {
            setErrorField("inputPublisherName", getMessage("need-publisher-name"));
            return;
        }

        if (publisherId != null && Tapestry.isNonBlank(publisherName))
        {
            setErrorField("inputPublisherName", getMessage("leave-publisher-name-empty"));
            return;
        }

        // Check for an error from a validation field

        if (isInError())
            return;

        // OK, do the update.

        VirtualLibraryEngine vengine = (VirtualLibraryEngine)cycle.getEngine();
        Integer bookId = getBookId();

        int i = 0;
        while (true)
        {
            IOperations bean = vengine.getOperations();

            try
            {
                if (publisherId != null)
                    bean.updateBook(bookId, attributes);
                else
                {
                    bean.updateBook(bookId, attributes, publisherName);
					vengine.clearCache();
                }

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (CreateException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception updating book #" + bookId + ".", ex, i++);

                continue;
            }
        }

        MyLibrary page = (MyLibrary) cycle.getPage("MyLibrary");
        page.setMessage(format("updated-book", attributes.get("title")));
		page.activate(cycle);
      
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
            setAttributes(new HashMap());
    }

}