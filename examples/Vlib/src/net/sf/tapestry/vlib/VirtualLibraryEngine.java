package net.sf.tapestry.vlib;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.engine.BaseEngine;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IBookQueryHome;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.IOperationsHome;
import net.sf.tapestry.vlib.ejb.Person;
import net.sf.tapestry.vlib.ejb.Publisher;
import net.sf.tapestry.vlib.pages.ApplicationUnavailable;

/**
 *
 *  The engine for the Virtual Library.  
 *  This exists to implement the external 
 *  service, which allows the {@link net.sf.tapestry.vlib.pages.ViewBook}
 *  and {@link net.sf.tapestry.vlib.pages.PersonPage}
 *  pages to be bookmarked, and to provide
 *  a way for shutting down the application when the user logs out.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class VirtualLibraryEngine extends BaseEngine
{
    public static final Log LOG = LogFactory.getLog(VirtualLibraryEngine.class);

    private static final boolean DEBUG_ENABLED = Boolean.getBoolean("net.sf.tapestry.vlib.debug-enabled");

    private transient boolean _killSession;

    // Home interfaces are static, such that they are only
    // looked up once (JNDI lookup is very expensive).

    private static IBookQueryHome _bookQueryHome;
    private static IOperationsHome _operationsHome;
    private transient IOperations _operations;

    private static Context _rootNamingContext;

    private transient IPropertySelectionModel _publisherModel;
    private transient IPropertySelectionModel _personModel;

    private transient String _applicationUnavailableMessage;
    
    /**
     *  Creates an instance of {@link Visit}.
     *
     **/

    public Object createVisit(IRequestCycle cycle)
    {
        cycle.getRequestContext().createSession();

        setStateful();

        return new Visit(this);
    }

    /**
     *  Removes the operations bean instance, if accessed this request cycle.
     *
     *  <p>May invalidate the {@link HttpSession} (see {@link #logout()}).
     **/

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {
        clearCache();
        
        _applicationUnavailableMessage = null;

        if (_killSession)
        {
            try
            {
                HttpSession session = cycle.getRequestContext().getSession();

                if (session != null)
                    session.invalidate();
            }
            catch (IllegalStateException ex)
            {
                // Ignore.
            }
        }
    }

    /**
     *  Sets the visit property to null, and sets a flag that
     *  invalidates the {@link HttpSession} at the end of the request cycle.
     *
     **/

    public void logout()
    {
        Visit visit = (Visit) getVisit();

        if (visit != null)
            visit.setUser(null);

        _killSession = true;
    }

    public boolean isDebugEnabled()
    {
        return DEBUG_ENABLED;
    }

    public IBookQueryHome getBookQueryHome()
    {
        if (_bookQueryHome == null)
            _bookQueryHome = (IBookQueryHome) findNamedObject("vlib/BookQuery", IBookQueryHome.class);

        return _bookQueryHome;
    }

    public IOperationsHome getOperationsHome()
    {
        if (_operationsHome == null)
            _operationsHome = (IOperationsHome) findNamedObject("vlib/Operations", IOperationsHome.class);

        return _operationsHome;
    }

    /**
     *  Returns an instance of the Vlib Operations beans, which is a stateless
     *  session bean for performing certain operations.
     *
     *  <p>The bean is automatically removed at the end of the request cycle.
     *
     **/

    public IOperations getOperations()
    {
        IOperationsHome home;

        for (int i = 0; i < 2; i++)
        {

            if (_operations == null)
            {
                try
                {
                    home = getOperationsHome();
                    _operations = home.create();

                    break;
                }
                catch (CreateException ex)
                {
                    throw new ApplicationRuntimeException("Error creating operations bean.", ex);
                }
                catch (RemoteException ex)
                {
                    rmiFailure("Remote exception creating operations bean.", ex, i > 0);
                }
            }
        }

        return _operations;
    }

    public Object findNamedObject(String name, Class expectedClass)
    {
        Object result = null;

        for (int i = 0; i < 2; i++)
        {
            try
            {
                Object raw = getRootNamingContext().lookup(name);

                result = PortableRemoteObject.narrow(raw, expectedClass);

                break;
            }
            catch (ClassCastException ex)
            {
                throw new ApplicationRuntimeException(
                    "Object " + name + " is not type " + expectedClass.getName() + ".",
                    ex);
            }
            catch (NamingException ex)
            {
                namingFailure("Unable to resolve object " + name + ".", ex, i > 0);
            }
        }

        return result;
    }

    public Context getRootNamingContext()
    {
        for (int i = 0; i < 2; i++)
        {
            if (_rootNamingContext == null)
            {
                try
                {
                    _rootNamingContext = new InitialContext();

                    break;
                }
                catch (NamingException ex)
                {
                    namingFailure("Unable to locate root naming context.", ex, i > 0);
                }
            }
        }

        return _rootNamingContext;
    }

    /**
     *  Builds a model for entering in a publisher name, including an intial
     *  blank option.  Problem:  thie model is held for a long while, so it won't
     *  reflect publishers added by this user or others.  Solution:  coming; perhaps
     *  we'll age-out the model after a few minutes.
     *
     **/

    public IPropertySelectionModel getPublisherModel()
    {
        if (_publisherModel == null)
            _publisherModel = buildPublisherModel();

        return _publisherModel;
    }

    private IPropertySelectionModel buildPublisherModel()
    {
        Publisher[] publishers = null;

        EntitySelectionModel model = new EntitySelectionModel();

        // Add in a default null value, such that the user can
        // not select a specific Publisher.

        model.add(null, "");

        for (int i = 0; i < 2; i++)
        {
            IOperations operations = getOperations();

            try
            {
                publishers = operations.getPublishers();

                // Exit the retry loop

                break;
            }
            catch (RemoteException ex)
            {
                rmiFailure("Unable to obtain list of publishers.", ex, i > 0);
            }
        }

        // Add in the actual publishers.  They are sorted by name.

        for (int i = 0; i < publishers.length; i++)
            model.add(publishers[i].getPrimaryKey(), publishers[i].getName());

        return model;
    }

    /**
     *  Invoked from {@link Visit#clearCache()} (and at the end of the request
     *  cycle) to clear the publisher and person
     *  {@link IPropertySelectionModel} models.
     *
     **/

    public void clearCache()
    {
        _publisherModel = null;
        _personModel = null;
    }

    /**
     *  Returns a model that contains all the known Person's, sorted by last name,
     *  then first.  The label for the model matches the user's natural name.
     *
     **/

    public IPropertySelectionModel getPersonModel()
    {
        if (_personModel == null)
            _personModel = buildPersonModel();

        return _personModel;
    }

    private IPropertySelectionModel buildPersonModel()
    {
        Person[] persons = null;

        for (int i = 0; i < 2; i++)
        {
            IOperations operations = getOperations();

            try
            {
                persons = operations.getPersons();

                break;
            }
            catch (RemoteException ex)
            {
                rmiFailure("Unable to obtain list of persons.", ex, i > 0);
            }
        }

        EntitySelectionModel model = new EntitySelectionModel();

        // On this one, we don't include a null option.

        for (int i = 0; i < persons.length; i++)
            model.add(persons[i].getPrimaryKey(), persons[i].getNaturalName());

        return model;

    }

    /**
     *  Creates a new {@link IBookQuery} EJB instance.
     *
     **/

    public IBookQuery createNewQuery()
    {
        IBookQuery result = null;

        for (int i = 0; i < 2; i++)
        {
            IBookQueryHome home = getBookQueryHome();

            try
            {
                result = home.create();

                break;
            }
            catch (CreateException ex)
            {
                throw new ApplicationRuntimeException("Could not create BookQuery bean.", ex);
            }
            catch (RemoteException ex)
            {
                rmiFailure("Remote exception creating BookQuery bean.", ex, i > 0);
            }
        }

        return result;
    }

    /**
     *  Invoked in various places to present an error message to the user.
     *  This sets the error property of either the 
     *  {@link net.sf.tapestry.vlib.pages.Home} or
     *  {@link net.sf.tapestry.vlib.pages.MyLibrary} page 
     *  (the latter only if the user is logged in),
     *  and sets the selected page for rendering the response.
     *
     **/

    public void presentError(String error, IRequestCycle cycle)
    {
        String pageName = "Home";
        // Get, but don't create, the visit.
        Visit visit = (Visit) getVisit();

        if (visit != null && visit.isUserLoggedIn())
            pageName = "MyLibrary";

        IErrorProperty page = (IErrorProperty) cycle.getPage(pageName);

        page.setError(error);

        cycle.setPage(pageName);
    }

    /**
     *  Invoked after an operation on a home or remote interface
     *  throws a RemoteException; this clears any cache of
     *  home and remote interfaces.  
     *
     * @param message the message for the exception, or for the log message
     * @param ex the exception thrown
     * @param finalFailure if true, an {@link ApplicationRuntimeException}
     * is thrown after the message is logged.
     *
     **/

    public void rmiFailure(String message, RemoteException ex, boolean finalFailure)
    {
        LOG.error(message, ex);

        clearEJBs();

        if (finalFailure)
            punt(message, ex);

    }

    private void punt(String message, Throwable ex)
    {
        _applicationUnavailableMessage = message;
        
        throw new ApplicationRuntimeException(message, ex);
    }

    /**
     *  As with {@link #rmiFailure(String, RemoteException, boolean)}, but for
     * {@link NamingException}.
     *
     **/

    public void namingFailure(String message, NamingException ex, boolean finalFailure)
    {
        LOG.error(message, ex);

        clearEJBs();

        if (finalFailure)
            punt(message, ex);
    }

    private void clearEJBs()
    {
        _bookQueryHome = null;
        _operations = null;
        _operationsHome = null;
        _rootNamingContext = null;
    }

    /**
     *  Invoked when any kind of runtime exception percolates up to the
     *  top level service method.  Normally, the standard Exception
     *  page is displayed; we logout and setup our own version of the page
     *  instead.
     * 
     **/

    protected void activateExceptionPage(IRequestCycle cycle, ResponseOutputStream output, Throwable cause)
        throws ServletException
    {
        try
        {
            logout();

            ApplicationUnavailable page = (ApplicationUnavailable) cycle.getPage("ApplicationUnavailable");

            String message = _applicationUnavailableMessage;
            
            if (message == null)
                message = cause.getMessage();
                
            if (message == null)
                message = cause.getClass().getName();

            page.activate(message, cause);

            cycle.setPage(page);

            renderResponse(cycle, output);
        }
        catch (Throwable t)
        {
            super.activateExceptionPage(cycle, output, cause);
        }
    }

}