package com.primix.tapestry.record;

import com.primix.foundation.prop.PropertyHelper;
import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import java.util.*;
import java.io.*;
import java.rmi.*;
import javax.ejb.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Tracks changes to components on a page, allowing changes to be persisted across
 *  request cycles, and restoring the state of a page and component when needed.
 *
 *  <p>This is an abstract implementation; specific implementations can choose where
 * and how to persist the data.
 *
 *  <p>Implements {@link Externalizable} but does not have any state of its own.
 *  Subclasses must implement <code>readExternal()</code> and
 *  <code>writeExternal()</code>.
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class PageRecorder
implements IPageRecorder, Serializable
{
    protected transient boolean dirty = false;
    protected transient boolean locked = false;

    /**
    *  Invoked to persist all changes that have been accumulated.  If the recorder
    *  saves change incrementally, this should ensure that all changes have been persisted.
    *
    *  <p>Subclasses should check the dirty flag.  If the recorder is dirty, changes
    * should be recorded and the dirty flag cleared.
    *
    */

    public abstract void commit()
    throws PageRecorderCommitException;

    /**
    *  Returns a <code>Collection</code> of {@link IPageChange} objects
    *  identifying changes to the page and its components.
    *
    */

    public abstract Collection getChanges();

    /**
    *  Returns true if the page has observed a change.
    *  The dirty flag is cleared by
    *  {@link #commit()}.
    *
    */

    public boolean isDirty()
    {
        return dirty;
    }

    /**
    *  Returns true if the recorder is locked.  The locked flag
    *  is set by {@link #commit()}.
    *
    */

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean value)
    {
        locked = value;
    }

    /**
    *  Observes the change.  The object of the event is expected to
    *  be an {@link IComponent}.  Ignores the change if not active,
    *  otherwise, sets invokes {@link #recordChange(String, String,
    *  Serializable)}.
    *
    *  <p>If the property name in the event is null, then the recorder
    *  is marked dirty (but 
    *  {@link #recordChange(String, String,
    *  Serializable)} is not invoked.  This is how a "distant" property changes
    *  are propogated to the page recorder (a distant property change is a change to
    *  a property of an object that is itself a property of the page).
    *
    *  <p>If the recorder is not active (typically, when a page is
    *  being rewound), then the event is simply ignored.
    *
    */

    public void observeChange(ObservedChangeEvent event)
    {
        IComponent component;
        String propertyName;
        Serializable newValue;

        component = event.getComponent();
        propertyName = event.getPropertyName();

        if (locked)
            throw new ApplicationRuntimeException(
                "Page recorder for page " + component.getPage().getName() +
                " is locked after a commit(), but received a change to " +
                " property " + propertyName + " of component " +
                component.getExtendedId() + ".");


        if (propertyName == null)
        {
            dirty = true;
            return;
        }

        // Record the change, converting EJBObject to
        // Handle along the way.

        newValue = event.getNewValue();

        try
        {
            recordChange(component.getIdPath(), propertyName, 
                persistValue(newValue));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new ApplicationRuntimeException
                ("Unable to persist property " + propertyName + 
                " of " + component.getExtendedId() + 
                " as " + newValue + ".", t);
        }
    }

    /**
    *  Records a change to a particular component.  Subclasses may
    *  cache these in memory, or record them externally at this time.
    *
    *  <p>This method is responsible for setting the dirty flag if
    *  the described change is real.
    *
    *  @param componentPath the name of the component relative to the
    *  page which contains it.  May be null if the change was to a
    *  property of the page itself.
    *
    *  @param propertyName the name of the property which changed.
    *
    *  @param newValue the new value for the property, which may also
    *  be null.
    *
    *  @see IComponent#getIdPath()
    *
    */

    protected abstract void recordChange(String componentPath, String propertyName, 
        Serializable newValue);

    /**
    *  Rolls back the page to the currently persisted state.
    *
    */

    public void rollback(IPage page)
    {
        Iterator i;
        PageChange change;
        IComponent component;
        PropertyHelper helper;

        i = getChanges().iterator();

        while (i.hasNext())
        {
            change = (PageChange)i.next();

            component = page.getNestedComponent(change.componentPath);

            helper = PropertyHelper.forClass(component.getClass());

            try
            {
                // Restore the object, converting Handle to EJBObject
                // along the way.

                helper.set(component, change.propertyName, restoreValue(change.newValue));
            }
            catch (Throwable t)
            {
                throw new RollbackException(component, change.propertyName, 
                    change.newValue, t);
            }
        }
    }

    /**
    *  Converts an {@link EJBObject} into its handle for persistent storage.
    *
    */

    private Serializable persistValue(Serializable value)
    throws RemoteException
    {
        if (!(value instanceof EJBObject))
            return value;

        EJBObject ejb = (EJBObject)value;

        return ejb.getHandle();
    }

    /**
    *  Converts a {@link Handle}, previously stored by the recorder,
    *  back into a {@link EJBObject}.
    *
    */

    private Object restoreValue(Object value)
    throws RemoteException
    {
        if (!(value instanceof Handle))
            return value;

        Handle handle = (Handle)value;

        return handle.getEJBObject();
    }

}

