package net.sf.tapestry.vlib.pages.admin;

import java.rmi.RemoteException;
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
 *  PageLink which allows books to be transferred, en mass, from one user to another.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TransferBooks extends AdminPage
{
    private Person _fromUser;
    private Person _toUser;
    private boolean _selectionsOk;
    private IPropertySelectionModel _userBookModel;
    private IBookQuery _bookQuery;

    /**
     *  {@link List} of Book primary keys ({@link Integer}).
     *
     **/

    private List _selectedBooks;

    public void detach()
    {
        _fromUser = null;
        _toUser = null;
        _selectionsOk = false;
        _userBookModel = null;
        _bookQuery = null;

        super.detach();
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        super.beginResponse(writer, cycle);

        if (_selectionsOk)
            getUserBookModel();
    }

    public void setFromUser(Person value)
    {
        _fromUser = value;
        fireObservedChange("fromUser", value);
    }

    public Person getFromUser()
    {
        return _fromUser;
    }

    public void setToUser(Person value)
    {
        _toUser = value;
        fireObservedChange("toUser", value);
    }

    public Person getToUser()
    {
        return _toUser;
    }

    public Integer getFromUserPK()
    {
        if (_fromUser == null)
            return null;

        return _fromUser.getPrimaryKey();
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
        if (_toUser == null)
            return null;

        return _toUser.getPrimaryKey();
    }

    public boolean isSelectionsOk()
    {
        return _selectionsOk;
    }

    public void setSelectionsOk(boolean value)
    {
        _selectionsOk = value;

        fireObservedChange("selectionsOk", value);
    }

    public void setBookQuery(IBookQuery value)
    {
        _bookQuery = value;

        fireObservedChange("bookQuery", value);
    }

    public IBookQuery getBookQuery()
    {
        if (_bookQuery == null)
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

            setBookQuery(vengine.createNewQuery());
        }

        return _bookQuery;
    }

    public void processSelectForm(IRequestCycle cycle)
    {
        setSelectionsOk(false);

        if (_fromUser.getPrimaryKey().equals(_toUser.getPrimaryKey()))
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

        int count = _selectedBooks.size();

        if (count == 0)
        {
            setError("No books selected.");
            return;
        }

        Integer[] keys = (Integer[]) _selectedBooks.toArray(new Integer[count]);

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.transferBooks(_toUser.getPrimaryKey(), keys);

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

        setMessage("Transfered " + count + " books to " + _toUser.getNaturalName() + ".");
    }

    public List getSelectedBooks()
    {
        return _selectedBooks;
    }

    public IPropertySelectionModel getUserBookModel()
    {
        if (_userBookModel == null)
            _userBookModel = buildUserBookModel();

        return _userBookModel;
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

                int count = query.ownerQuery(_fromUser.getPrimaryKey());

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Unable to retrieve books owned by " + _fromUser.getNaturalName() + ".",
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
    
    public void setSelectedBooks(List selectedBooks)
    {
        _selectedBooks = selectedBooks;
    }

}