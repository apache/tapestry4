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

package net.sf.tapestry.vlib;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.engine.SimpleEngine;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IBookQueryHome;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.IOperationsHome;
import net.sf.tapestry.vlib.ejb.Person;
import net.sf.tapestry.vlib.ejb.Publisher;

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

public class VirtualLibraryEngine extends SimpleEngine
{
    public static final Logger LOG = LogManager.getLogger(VirtualLibraryEngine.class);

    private static boolean debugEnabled = Boolean.getBoolean("net.sf.tapestry.vlib.debug-enabled");

    private transient boolean killSession;

    // Home interfaces are static, such that they are only
    // looked up once (JNDI lookup is very expensive).

    private static IBookQueryHome bookQueryHome;
    private static IOperationsHome operationsHome;
    private transient IOperations operations;

    private static Context rootNamingContext;

    private transient IPropertySelectionModel publisherModel;
    private transient IPropertySelectionModel personModel;

    /**
     *  Creates an instance of {@link Visit}.
     *
     **/

    public Object createVisit(IRequestCycle cycle)
    {
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

        if (killSession)
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

        killSession = true;
    }

    public boolean isDebugEnabled()
    {
        return debugEnabled;
    }

    public IBookQueryHome getBookQueryHome()
    {
        if (bookQueryHome == null)
            bookQueryHome = (IBookQueryHome) findNamedObject("vlib/BookQuery", IBookQueryHome.class);

        return bookQueryHome;
    }

    public IOperationsHome getOperationsHome()
    {
        if (operationsHome == null)
            operationsHome = (IOperationsHome) findNamedObject("vlib/Operations", IOperationsHome.class);

        return operationsHome;
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

            if (operations == null)
            {
                try
                {
                    home = getOperationsHome();
                    operations = home.create();

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

        return operations;
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
            if (rootNamingContext == null)
            {
                try
                {
                    rootNamingContext = new InitialContext();

                    break;
                }
                catch (NamingException ex)
                {
                    namingFailure("Unable to locate root naming context.", ex, i > 0);
                }
            }
        }

        return rootNamingContext;
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
        if (publisherModel == null)
            publisherModel = buildPublisherModel();

        return publisherModel;
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
            IOperations bean = getOperations();

            try
            {
                publishers = bean.getPublishers();

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
        publisherModel = null;
        personModel = null;
    }

    /**
     *  Returns a model that contains all the known Person's, sorted by last name,
     *  then first.  The label for the model matches the user's natural name.
     *
     **/

    public IPropertySelectionModel getPersonModel()
    {
        if (personModel == null)
            personModel = buildPersonModel();

        return personModel;
    }

    private IPropertySelectionModel buildPersonModel()
    {
        Person[] persons = null;

        for (int i = 0; i < 2; i++)
        {
            IOperations bean = getOperations();

            try
            {
                persons = bean.getPersons();

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
     * @param throwException if true, an {@link ApplicationRuntimeException}
     * is thrown after the message is logged.
     *
     **/

    public void rmiFailure(String message, RemoteException ex, boolean throwException)
    {
        LOG.error(message, ex);

        if (throwException)
            throw new ApplicationRuntimeException(message, ex);

        clearEJBs();
    }

    /**
     *  As with {@link #rmiFailure(String, RemoteException, boolean)}, but for
     * {@link NamingException}.
     *
     **/

    public void namingFailure(String message, NamingException ex, boolean throwException)
    {
        LOG.error(message, ex);

        if (throwException)
            throw new ApplicationRuntimeException(message, ex);

        clearEJBs();
    }

    private void clearEJBs()
    {
        bookQueryHome = null;
        operations = null;
        operationsHome = null;
        rootNamingContext = null;
    }
}