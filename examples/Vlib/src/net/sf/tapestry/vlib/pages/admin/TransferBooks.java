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
import java.util.List;

import javax.ejb.FinderException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.vlib.AdminPage;
import net.sf.tapestry.vlib.EntitySelectionModel;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.Person;

/**
 *  Page which allows books to be transferred, en mass, from one user to another.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TransferBooks extends AdminPage
{
    private Person fromUser;
    private Person toUser;
    private boolean selectionsOk;
    private IPropertySelectionModel userBookModel;
    private IBookQuery bookQuery;

    /**
     *  {@link List} of Book primary keys ({@link Integer}).
     *
     **/

    private List selectedBooks;

    public void detach()
    {
        fromUser = null;
        toUser = null;
        selectionsOk = false;
        userBookModel = null;
        bookQuery = null;

        super.detach();
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        super.beginResponse(writer, cycle);

        if (selectionsOk)
            getUserBookModel();
    }

    public void setFromUser(Person value)
    {
        fromUser = value;
        fireObservedChange("fromUser", value);
    }

    public Person getFromUser()
    {
        return fromUser;
    }

    public void setToUser(Person value)
    {
        toUser = value;
        fireObservedChange("toUser", value);
    }

    public Person getToUser()
    {
        return toUser;
    }

    public Integer getFromUserPK()
    {
        if (fromUser == null)
            return null;

        return fromUser.getPrimaryKey();
    }

    public void setFromUserPK(Integer value)
    {
        setFromUser(readPerson(value));
    }

    public void setToUserPK(Integer value)
    {
        setToUser(readPerson(value));
    }

    public Integer getToUserPK()
    {
        if (toUser == null)
            return null;

        return toUser.getPrimaryKey();
    }

    public boolean isSelectionsOk()
    {
        return selectionsOk;
    }

    public void setSelectionsOk(boolean value)
    {
        selectionsOk = value;

        fireObservedChange("selectionsOk", value);
    }

    public void setBookQuery(IBookQuery value)
    {
        bookQuery = value;

        fireObservedChange("bookQuery", value);
    }

    public IBookQuery getBookQuery()
    {
        if (bookQuery == null)
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

            setBookQuery(vengine.createNewQuery());
        }

        return bookQuery;
    }

    public void processSelectForm(IRequestCycle cycle)
    {
        setSelectionsOk(false);

        if (fromUser.getPrimaryKey().equals(toUser.getPrimaryKey()))
        {
            setError("Please select two different people.");
            return;
        }

        if (getUserBookModel().getOptionCount() == 0)
        {
            setError("Selected user owns no books.");
            return;
        }

        // Selections ok, enable bottom half of page.

        setSelectionsOk(true);
    }

    public void processXferForm(IRequestCycle cycle)
    {
        setSelectionsOk(false);

        List selectedKeys = getSelectedBooks();
        int count = selectedKeys.size();

        if (count == 0)
        {
            setError("No books selected.");
            return;
        }

        Integer[] keys = (Integer[]) selectedKeys.toArray(new Integer[count]);

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.transferBooks(toUser.getPrimaryKey(), keys);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to transfer ownership of books.", ex, i > 0);
            }
        }

        setMessage("Transfered " + count + " books to " + toUser.getNaturalName() + ".");
    }

    public List getSelectedBooks()
    {
        if (selectedBooks == null)
            selectedBooks = new ArrayList();

        return selectedBooks;
    }

    public IPropertySelectionModel getUserBookModel()
    {
        if (userBookModel == null)
            userBookModel = buildUserBookModel();

        return userBookModel;
    }

    private IPropertySelectionModel buildUserBookModel()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book[] books = null;

        for (int i = 0; i < 2; i++)
        {
            books = null;

            try
            {
                IBookQuery query = getBookQuery();

                int count = query.ownerQuery(fromUser.getPrimaryKey());

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Unable to retrieve books owned by " + fromUser.getNaturalName() + ".",
                    ex,
                    i > 0);

                setBookQuery(null);
            }
        }

        EntitySelectionModel model = new EntitySelectionModel();

        if (books != null)
            for (int i = 0; i < books.length; i++)
                model.add(books[i].getPrimaryKey(), books[i].getTitle());

        return model;
    }

    private Person readPerson(Integer primaryKey)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Person result = null;

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations ops = vengine.getOperations();

                result = ops.getPerson(primaryKey);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to retrieve person #" + primaryKey + ".", ex, i > 0);
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }

        }

        return result;
    }
}