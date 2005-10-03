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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.components.IPrimaryKeyConverter;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Publisher;
import org.apache.tapestry.vlib.pages.MyLibrary;

/**
 * Allows editting of the publishers in the database, including deleting publishers (which can be
 * dangerous if any books are linked to the publisher).
 * 
 * @author Howard Lewis Ship
 */

public abstract class EditPublishers extends AdminPage implements PageBeginRenderListener
{
    public abstract Publisher getPublisher();

    public abstract void setPublisher(Publisher publisher);

    public abstract Map<Integer, Publisher> getPublisherMap();

    public abstract void setPublisherMap(Map<Integer, Publisher> map);

    public abstract void setPublishers(Publisher[] publishers);

    public abstract void setDeletedPublishers(Set<Integer> deleted);

    public abstract Set<Integer> getDeletedPublishers();

    @Message
    public abstract String outOfDate();

    @Message
    public abstract String updateFailure();

    @Message
    public abstract String publishersUpdated();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    public class PublisherKeyConverter implements IPrimaryKeyConverter
    {
        public Object getPrimaryKey(Object value)
        {
            Publisher publisher = (Publisher) value;

            return publisher.getId();
        }

        public Object getValue(Object primaryKey)
        {
            Integer id = (Integer) primaryKey;

            Publisher result = getPublisherMap().get(id);

            if (result == null)
            {
                getValidationDelegate().record(null, outOfDate());
                throw new PageRedirectException(EditPublishers.this);
            }

            return result;
        }

    }

    public IPrimaryKeyConverter getPublisherConverter()
    {
        return new PublisherKeyConverter();
    }

    public boolean isDeleted()
    {
        return false;
    }

    public void setDeleted(boolean deleted)
    {
        if (deleted)
            getDeletedPublishers().add(getPublisher().getId());
    }

    /**
     * Reads all publishers from the database, building the list of publisher ids, and the map from
     * id to Publisher. Also, sets the deletedPublisherIds property to an empty set.
     */

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
                vengine.rmiFailure(updateFailure(), ex, i++);
            }
        }

        // This is really only needed during render, not rewind

        setPublishers(publishers);

        Map<Integer, Publisher> publisherMap = new HashMap<Integer, Publisher>();

        for (Publisher publisher : publishers)
        {
            publisherMap.put(publisher.getId(), publisher);
        }

        setPublisherMap(publisherMap);
    }

    public void processForm(IRequestCycle cycle)
    {
        Map<Integer, Publisher> publisherMap = getPublisherMap();
        Set<Integer> deleted = getDeletedPublishers();

        // Delete from the map anything that was marked deleted

        publisherMap.keySet().removeAll(deleted);

        Publisher[] updated = publisherMap.values().toArray(new Publisher[0]);
        Integer[] deletedKeys = deleted.toArray(new Integer[0]);

        // Now, push the updates through to the database

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.updatePublishers(updated, deletedKeys);

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
                vengine.rmiFailure(updateFailure(), ex, i++);
            }
        }

        // Clear any cached info about publishers.

        vengine.clearCache();

        MyLibrary page = getMyLibrary();

        page.setMessage(publishersUpdated());

        page.activate();
    }

    public void pageBeginRender(PageEvent event)
    {
        readPublishers();

        setDeletedPublishers(new HashSet());
    }

}