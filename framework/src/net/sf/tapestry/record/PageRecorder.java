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
package net.sf.tapestry.record;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBObject;
import javax.ejb.Handle;
import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.PageRecorderCommitException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.ObservedChangeEvent;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  Tracks changes to components on a page, allowing changes to be persisted across
 *  request cycles, and restoring the state of a page and component when needed.
 *
 *  <p>This is an abstract implementation; specific implementations can choose where
 * and how to persist the data.
 *
 *  <p>Implements {@link Serializable} but does not have any state of its own.
 *  Subclasses must implement <code>readExternal()</code> and
 *  <code>writeExternal()</code>.
 *
 * @author Howard Ship
 * @version $Id$
 * 
 **/

public abstract class PageRecorder implements IPageRecorder, Serializable
{
    protected transient boolean dirty = false;
    protected transient boolean locked = false;
    protected transient boolean discard = false;

    /**
     *  Invoked to persist all changes that have been accumulated.  If the recorder
     *  saves change incrementally, this should ensure that all changes have been persisted.
     *
     *  <p>Subclasses should check the dirty flag.  If the recorder is dirty, changes
     *  should be recorded and the dirty flag cleared.
     *
     **/

    public abstract void commit() throws PageRecorderCommitException;

    /**
     *  Returns a <code>Collection</code> of 
     *  {@link net.sf.tapestry.IPageChange} objects
     *  identifying changes to the page and its components.
     *
     **/

    public abstract Collection getChanges();

    /**
     *  Returns true if the page has observed a change.
     *  The dirty flag is cleared by
     *  {@link #commit()}.
     *
     **/

    public boolean isDirty()
    
    {
        return dirty;
    }

    /**
     *  Returns true if the recorder is locked.  The locked flag
     *  is set by {@link #commit()}.
     *
     **/

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
     *  Object)}.
     *
     *  <p>If the property name in the event is null, then the recorder
     *  is marked dirty (but 
     *  {@link #recordChange(String, String,
     *  Object)} is not invoked.  This is how a "distant" property changes
     *  are propogated to the page recorder (a distant property change is a change to
     *  a property of an object that is itself a property of the page).
     *
     *  <p>If the recorder is not active (typically, when a page is
     *  being rewound), then the event is simply ignored.
     *
     **/

    public void observeChange(ObservedChangeEvent event)
    {
        IComponent component;
        String propertyName;
        Object newValue;

        component = event.getComponent();
        propertyName = event.getPropertyName();

        if (locked)
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "PageRecorder.change-after-lock",
                    component.getPage().getName(),
                    propertyName,
                    component.getExtendedId()));

        if (propertyName == null)
        {
            dirty = true;
            return;
        }

        newValue = event.getNewValue();

        try
        {
            recordChange(component.getIdPath(), propertyName, newValue);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageRecorder.unable-to-persist", propertyName, component.getExtendedId(), newValue),
                t);
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
     **/

    protected abstract void recordChange(String componentPath, String propertyName, Object newValue);

    /**
     *  Rolls back the page to the currently persisted state.
     *
     **/

    public void rollback(IPage page)
    {
        Collection changes = getChanges();
        
        if (changes.isEmpty())
            return;
            
        IResourceResolver resolver = page.getEngine().getResourceResolver();            
        Iterator i = changes.iterator();

        while (i.hasNext())
        {
            PageChange change = (PageChange) i.next();

            IComponent component = page.getNestedComponent(change.componentPath);

            try
            {

                OgnlUtils.set(change.propertyName, resolver, component, change.newValue);
            }
            catch (Throwable t)
            {
                throw new RollbackException(component, change.propertyName, change.newValue, t);
            }
        }
    }

    /**
     *  Invoked by subclasses to converts an object into
     *  a {@link Serializable} value for for persistent storage.
     *
     *  <p>This implementation implements a special case
     *  for converting an {@link EJBObject} into a {@link Handle}
     *  for storage.
     *
     *  @since 0.2.9
     **/

    protected Serializable persistValue(Object value) throws IOException
    {
        if (!(value instanceof EJBObject))
        {
            try
            {
                return (Serializable) value;
            }
            catch (ClassCastException ex)
            {
                throw new PageRecorderSerializationException(ex);
            }
        }

        try
        {
            EJBObject ejb = (EJBObject) value;

            return ejb.getHandle();
        }
        catch (RemoteException ex)
        {
            throw new PageRecorderSerializationException(ex);
        }
    }

    /**
     *  Invoked by subclasses to restore a persisted value to its
     *  runtime value.  This implementation converts {@link Handle}s, stored
     *  persistently, back into {@link EJBObject}s.
     *
     *  @since 0.2.9
     **/

    protected Object restoreValue(Object value) throws IOException
    {
        if (!(value instanceof Handle))
            return value;

        try
        {
            Handle handle = (Handle) value;

            return handle.getEJBObject();
        }
        catch (RemoteException ex)
        {
            throw new PageRecorderSerializationException(ex);
        }
    }

    /** @since 2.0.2 **/

    public boolean isMarkedForDiscard()
    {
        return discard;
    }

    /** @since 2.0.2 **/

    public void markForDiscard()
    {
        discard = true;
    }

}