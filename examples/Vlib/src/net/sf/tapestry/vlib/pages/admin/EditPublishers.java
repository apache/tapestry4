/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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