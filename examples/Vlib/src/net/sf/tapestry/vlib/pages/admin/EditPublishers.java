//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.pages.admin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.vlib.AdminPage;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.Publisher;

/**
 *  Allows editting of the publishers in the database, including deleting
 *  publishers (which can be dangerous if any books are linked to the publisher).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class EditPublishers extends AdminPage
{
    private Publisher[] publishers;
    private Publisher publisher;

    /**
     *  {@link Set} of {@link Integer} primary keys, of Publishers.
     *
     **/

    private Set deletedPublishers;

    public void detach()
    {
        publishers = null;
        publisher = null;
        deletedPublishers = null;

        super.detach();
    }

    public Integer[] getPublisherIds()
    {
        if (publishers == null)
            readPublishers();

        Integer[] ids = new Integer[publishers.length];

        for (int i = 0; i < publishers.length; i++)
            ids[i] = publishers[i].getPrimaryKey();

        return ids;
    }

    public void setPublisherId(Integer value)
    {
        if (publishers == null)
            readPublishers();

        for (int i = 0; i < publishers.length; i++)
            if (publishers[i].getPrimaryKey().equals(value))
            {
                publisher = publishers[i];
                return;
            }

        publisher = null;
    }

    public Publisher getPublisher()
    {
        return publisher;
    }

    public Set getDeletedPublishers()
    {
        if (deletedPublishers == null)
            deletedPublishers = new HashSet();

        return deletedPublishers;
    }

    private void readPublishers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                publishers = operations.getPublishers();

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to obtain list of publishers.", ex, i > 0);
            }
        }

    }

    public void processForm(IRequestCycle cycle)
    {
        if (isInError())
            return;

        List updateList = new ArrayList(publishers.length);
        Set deletedKeys = getDeletedPublishers();

        // Create a List of all the publishers which aren't
        // being deleted.
        for (int i = 0; i < publishers.length; i++)
        {
            if (deletedKeys != null && deletedKeys.contains(publishers[i].getPrimaryKey()))
                continue;

            updateList.add(publishers[i]);
        }

        // Forget any information about publishers that was previously read.

        publishers = null;

        Publisher[] updated = (Publisher[]) updateList.toArray(new Publisher[updateList.size()]);

        Integer[] deleted = null;

        if (deletedKeys != null)
            deleted = (Integer[]) deletedKeys.toArray(new Integer[deletedKeys.size()]);

        // Now, push the updates through to the database

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
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
                vengine.rmiFailure("Remote exception updating publishers.", ex, i > 0);
            }
        }

        // Clear any cached info about publishers.

        vengine.clearCache();
    }

    /**
     *  Always returns false; no publishers are deleted when the form is rendered.
     *
     **/

    public boolean getDeletedPublisher()
    {
        return false;
    }

    /**
     *  If value is true (i.e., the checkbox was checked)
     *  then the primary key of the current Publisher is added
     *  to the deletedPublishers set.
     *
     **/

    public void setDeletedPublisher(boolean value)
    {
        if (value)
        {
            Set set = getDeletedPublishers();

            set.add(publisher.getPrimaryKey());
        }
    }
}