package org.apache.tapestry.vlib.pages.admin;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  Second page in Transfer Books wizard; allows the books owned by the
 *  from user to be selected and transfered to the target user.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public abstract class TransferBooksTransfer extends AdminPage implements PageRenderListener
{
    public abstract Person getFromUser();

    public abstract void setFromUser(Person fromUser);

    public abstract Integer getFromUserPK();

    public abstract void setFromUserPK(Integer fromUserPK);

    public abstract Integer getToUserPK();

    public abstract void setToUserPK(Integer toUserPK);

    public abstract Person getToUser();

    public abstract void setToUser(Person toUser);

    public abstract List getSelectedBooks();

    public abstract IPropertySelectionModel getUserBookModel();

    public abstract void setUserBookModel(IPropertySelectionModel userBookModel);

    public void activate(IRequestCycle cycle, Integer fromUserPK, Integer toUserPK)
    {
        Person fromUser = readPerson(fromUserPK);

        IPropertySelectionModel model = buildUserBookModel(fromUser);

        if (model.getOptionCount() == 0)
        {
            IErrorProperty page = (IErrorProperty) cycle.getPage();
            page.setError(formatString("user-has-no-books", fromUser.getNaturalName()));
            return;
        }

        setFromUserPK(fromUserPK);
        setFromUser(fromUser);

        setToUserPK(toUserPK);

        setUserBookModel(model);

        cycle.setPage(this);
    }

    /**
     *  Only properties toUserPK and fromUserPK are persistent.  We need to refresh
     *  the fromUser, toUser and userBookModel properties at the start
     *  of each request cycle.
     * 
     **/

    public void pageBeginRender(PageEvent event)
    {
        Person fromUser = getFromUser();

        if (fromUser == null)
        {
            fromUser = readPerson(getFromUserPK());
            setFromUser(fromUser);
        }

        if (getUserBookModel() == null)
            setUserBookModel(buildUserBookModel(fromUser));

        Person toUser = getToUser();
        if (toUser == null)
        {
            toUser = readPerson(getToUserPK());
            setToUser(toUser);
        }
    }

    public void pageEndRender(PageEvent event)
    {
    }

    public void formSubmit(IRequestCycle cycle)
    {
        List selectedBooks = getSelectedBooks();

        int count = Tapestry.size(selectedBooks);

        if (count == 0)
        {
            setError(getString("no-books-selected"));
            return;
        }

        Integer[] keys = (Integer[]) selectedBooks.toArray(new Integer[count]);
        Person toUser = getToUser();

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

        Person fromUser = getFromUser();
        IMessageProperty selectPage = (TransferBooksSelect) cycle.getPage("TransferBooksSelect");
        selectPage.setMessage(
            formatString(
                "transfered-books",
                Integer.toString(count),
                fromUser.getNaturalName(),
                toUser.getNaturalName()));
        cycle.setPage(selectPage);
    }

    private IPropertySelectionModel buildUserBookModel(Person user)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book[] books = null;

        for (int i = 0; i < 2; i++)
        {
            books = null;

            try
            {
                IBookQuery query = vengine.createNewQuery();

                int count = query.ownerQuery(user.getPrimaryKey());

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Unable to retrieve books owned by " + user.getNaturalName() + ".",
                    ex,
                    i > 0);
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
