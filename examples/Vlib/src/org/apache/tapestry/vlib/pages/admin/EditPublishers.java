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

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.ListEditMap;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Publisher;

/**
 *  Allows editting of the publishers in the database, including deleting
 *  publishers (which can be dangerous if any books are linked to the publisher).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class EditPublishers extends AdminPage implements PageRenderListener
{
    public abstract ListEditMap getListEditMap();

    public abstract void setListEditMap(ListEditMap listEditMap);

    public abstract Publisher getPublisher();

    public abstract void setPublisher(Publisher publisher);

    public void synchronizePublisher(IRequestCycle cycle)
    {
        ListEditMap map = getListEditMap();

        Publisher publisher = (Publisher) map.getValue();

        if (publisher == null)
        {
            setError(getMessage("out-of-date"));
            throw new PageRedirectException(this);
        }

        setPublisher(publisher);
    }

    /**
     *  Reads all publishers from the database, building the list
     *  of publisher ids, and the map from id to Publisher.
     *  Also, sets the deletedPublisherIds property to an empty set.
     * 
     **/

    private void readPublishers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Publisher[] publishers = null;

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                publishers = operations.getPublishers();

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("read-failure"), ex, i++);
            }
        }

        ListEditMap map = new ListEditMap();

        int count = Tapestry.size(publishers);

        for (i = 0; i < count; i++)
            map.add(publishers[i].getId(), publishers[i]);

        setListEditMap(map);
    }

    public void processForm(IRequestCycle cycle)
    {
        if (isInError())
            return;

        ListEditMap map = getListEditMap();
        List updateList = map.getValues();
        List deletedIds = map.getDeletedKeys();

        Publisher[] updated = (Publisher[]) updateList.toArray(new Publisher[updateList.size()]);

        Integer[] deleted =
            deletedIds == null
                ? null
                : (Integer[]) deletedIds.toArray(new Integer[deletedIds.size()]);

        // Now, push the updates through to the database

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.updatePublishers(updated, deleted);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoveException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("update-failure"), ex, i++);
            }
        }

        // Clear any cached info about publishers.

        vengine.clearCache();
    }

    public void pageBeginRender(PageEvent event)
    {
        readPublishers();
    }

}