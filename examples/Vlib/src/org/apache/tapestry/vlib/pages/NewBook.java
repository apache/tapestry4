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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.vlib.Protected;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 *  Collects information for a new book.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class NewBook extends Protected implements PageRenderListener
{

    public abstract Map getAttributes();

    public abstract void setAttributes(Map attributes);

    public abstract String getPublisherName();

    public void addBook(IRequestCycle cycle)
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

        if (isInError())
            return;

        Visit visit = (Visit) getVisit();
        Integer userId = visit.getUserId();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

        attributes.put("ownerId", userId);
        attributes.put("holderId", userId);

        int i = 0;
        while (true)
        {
            try
            {

                IOperations operations = vengine.getOperations();

                if (publisherId != null)
                    operations.addBook(attributes);
                else
                {
                    operations.addBook(attributes, publisherName);

                    // Clear the app's cache of info; in this case, known publishers.

                    vengine.clearCache();
                }

                break;
            }
            catch (CreateException ex)
            {
                setError("Error adding book: " + ex.getMessage());
                return;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception adding new book.", ex, i++);
            }
        }

        // Success.  First, update the message property of the return page.

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

        myLibrary.setMessage(format("added-book", attributes.get("title")));

        myLibrary.activate(cycle);
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getAttributes() == null)
        {
            Map attributes = new HashMap();

            // Setup defaults for the new book.

            attributes.put("lendable", Boolean.TRUE);

            setAttributes(attributes);
        }
    }

}