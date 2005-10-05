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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.util.DefaultPrimaryKeyConverter;
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

public abstract class EditPublishers extends AdminPage implements PageBeginRenderListener,
        PageDetachListener
{
    public abstract Publisher getPublisher();

    @Message
    public abstract String outOfDate();

    @Message
    public abstract String updateFailure();

    @Message
    public abstract String publishersUpdated();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    private DefaultPrimaryKeyConverter _converter;

    public DefaultPrimaryKeyConverter getConverter()
    {
        if (_converter == null)
            _converter = new DefaultPrimaryKeyConverter()
            {
                // Here's why we DON'T use @Bean ...

                @Override
                protected Object provideMissingValue(Object key)
                {
                    getValidationDelegate().record(null, outOfDate());
                    throw new PageRedirectException(EditPublishers.this);
                }

            };

        return _converter;
    }

    public void pageBeginRender(PageEvent event)
    {
        readPublishers();
    }

    public void pageDetached(PageEvent event)
    {
        _converter = null;
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

        DefaultPrimaryKeyConverter converter = getConverter();

        converter.clear();

        for (Publisher publisher : publishers)
        {
            converter.add(publisher.getId(), publisher);
        }
    }

    protected Publisher[] extractUpdatedPublishers()
    {
        List publishers = getConverter().getValues();

        return (Publisher[]) publishers.toArray(new Publisher[0]);
    }

    protected Integer[] extractDeletedKeys()
    {
        Set deletedValues = getConverter().getDeletedValues();
        List keys = new ArrayList(deletedValues.size());

        Iterator i = deletedValues.iterator();
        while (i.hasNext())
        {
            Publisher p = (Publisher) i.next();
            keys.add(p.getId());
        }

        return (Integer[]) keys.toArray(new Integer[0]);
    }

    public void processForm(IRequestCycle cycle)
    {
        Publisher[] updated = extractUpdatedPublishers();
        Integer[] deletedKeys = extractDeletedKeys();

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

}