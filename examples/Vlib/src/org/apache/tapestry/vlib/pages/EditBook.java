/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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