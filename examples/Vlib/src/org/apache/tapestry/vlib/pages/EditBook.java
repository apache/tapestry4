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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.vlib.Protected;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 *  Edits the properties of at book.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class EditBook extends Protected
{
    private Integer bookPK;
    private Map attributes;
    private String publisherName;

    private static final int MAP_SIZE = 11;

    public void detach()
    {
        attributes = null;
        bookPK = null;
        publisherName = null;

        super.detach();
    }

    public Map getAttributes()
    {
        if (attributes == null)
            attributes = new HashMap(MAP_SIZE);

        return attributes;
    }

    public String getPublisherName()
    {
        return publisherName;
    }

    public void setPublisherName(String value)
    {
        publisherName = value;
    }

    /**
     *  Gets the book's primary key as a String.
     *
     **/

    public String getBookPrimaryKey()
    {
        return bookPK.toString();
    }

    /**
     *  Updates the book's primary key value (converting from String to Integer).
     *  This allows a Hidden component in the form to synchronize the book being
     *  editted ... which fixes the Browser Back Button problem.
     *
     **/

    public void setBookPrimaryKey(String value)
    {
        bookPK = new Integer(value);
    }

    /**
     *  Invoked (from {@link MyLibrary}) to begin editting a book.
     *  Gets the attributes from the {@link org.apache.tapestry.vlib.ejb.IBook} 
     *  and updates the request cycle to render this page,
     *
     **/

    public void beginEdit(Integer bookPK, IRequestCycle cycle)
    {
        this.bookPK = bookPK;

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                // Get the attributes as a source for our input fields.

                IOperations operations = vengine.getOperations();

                attributes = operations.getBookAttributes(bookPK);

                break;
            }
            catch (FinderException ex)
            {
                // TBD:  Dress this up and send them back to the Search or
                // MyLibrary page.

                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Remote exception setting up page for book #" + bookPK + ".",
                    ex,
                    i > 0);
            }
        }

        cycle.setPage(this);
    }

    /**
     *  Used to update the book when the form is submitted.
     *
     **/

    public void formSubmit(IRequestCycle cycle)
    {
        Integer publisherPK = (Integer) attributes.get("publisherPK");

        if (publisherPK == null && Tapestry.isNull(publisherName))
        {
            setErrorField(
                "inputPublisherName",
                "Must provide a publisher name if the publisher option is empty.");
            return;
        }

        if (publisherPK != null && !Tapestry.isNull(publisherName))
        {
            setErrorField(
                "inputPublisherName",
                "Must leave the publisher name blank if selecting a publisher from the list.");
            return;
        }

        // Check for an error from a validation field

        if (isInError())
            return;

        // OK, do the update.

        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = visit.getEngine();

        for (int i = 0; i < 2; i++)
        {

            IOperations bean = vengine.getOperations();

            try
            {
                if (publisherPK != null)
                    bean.updateBook(bookPK, attributes);
                else
                {
                    bean.updateBook(bookPK, attributes, publisherName);
                    visit.clearCache();
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
                vengine.rmiFailure("Remote exception updating book #" + bookPK + ".", ex, i > 0);

                continue;
            }
        }

        MyLibrary page = (MyLibrary) cycle.getPage("MyLibrary");
        page.setMessage("Updated book.");

        cycle.setPage(page);
    }

}