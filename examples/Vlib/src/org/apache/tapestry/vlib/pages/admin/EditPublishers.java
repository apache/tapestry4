/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
            setError(getString("out-of-date"));
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
                vengine.rmiFailure(getString("read-failure"), ex, i++);
            }
        }

        ListEditMap map = new ListEditMap();

        int count = Tapestry.size(publishers);

        for (i = 0; i < count; i++)
        {
            map.add(publishers[i].getId(), publishers[i]);
        }

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
                vengine.rmiFailure(getString("update-failure"), ex, i++);
            }
        }

        // Clear any cached info about publishers.

        vengine.clearCache();
    }

    public void pageBeginRender(PageEvent event)
    {
        readPublishers();
    }

    public void pageEndRender(PageEvent event)
    {
    }

}