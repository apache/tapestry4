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

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.ApplicationServlet;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.PageRecorderCommitException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.ObservedChangeEvent;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.util.prop.OgnlUtils;

/**
 *  Tracks changes to components on a page, allowing changes to be persisted across
 *  request cycles, and restoring the state of a page and component when needed.
 *
 *  <p>This is an abstract implementation; specific implementations can choose where
 *  and how to persist the data.
 *
 * @author Howard Ship
 * @version $Id$
 * 
 **/

public abstract class PageRecorder implements IPageRecorder
{
    public static final String VALUE_PERSISTER_EXTENSION_NAME = "net.sf.tapestry.value-persister";

    private IValuePersister _persister;

    private boolean _dirty = false;
    private boolean _locked = false;
    private boolean _discard = false;

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
        return _dirty;
    }

    /**
     *  Returns true if the recorder is locked.  The locked flag
     *  is set by {@link #commit()}.
     *
     **/

    public boolean isLocked()
    {
        return _locked;
    }

    public void setLocked(boolean value)
    {
        _locked = value;
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
        IComponent component = event.getComponent();
        String propertyName = event.getPropertyName();

        if (_locked)
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "PageRecorder.change-after-lock",
                    component.getPage().getPageName(),
                    propertyName,
                    component.getExtendedId()));

        if (propertyName == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageRecorder.null-property-name", component.getExtendedId()));

        Object activeValue = event.getNewValue();

        try
        {
            Object storableValue = _persister.convertToStorableValue(activeValue);

            recordChange(component.getIdPath(), propertyName, storableValue);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "PageRecorder.unable-to-persist",
                    propertyName,
                    component.getExtendedId(),
                    activeValue),
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

            String propertyName = change.getPropertyName();

            IComponent component = page.getNestedComponent(change.getComponentPath());

            Object storedValue = change.getNewValue();

            try
            {
                Object activeValue = _persister.convertToActiveValue(storedValue);

                OgnlUtils.set(propertyName, resolver, component, activeValue);
            }
            catch (Throwable t)
            {
                throw new RollbackException(component, propertyName, storedValue, t);
            }
        }
    }

    /** @since 2.0.2 **/

    public boolean isMarkedForDiscard()
    {
        return _discard;
    }

    /** @since 2.0.2 **/

    public void markForDiscard()
    {
        _discard = true;
    }

    protected void setDirty(boolean dirty)
    {
        this._dirty = dirty;
    }

    protected boolean getDirty()
    {
        return _dirty;
    }

    /**
     *  Finds the {@link net.sf.tapestry.record.IValuePersister} as an
     *  attribute in the {@link javax.servlet.ServletContext}.  If not
     *  found it is created.  If an application extension named
     *  <code>net.sf.tapestry.value-persister</code>
     *  exists, it is used as the shared persister, otherwise
     *  an instance of {@link net.sf.tapestry.record.DefaultValuePersister}
     *  is created.  Subclasses may override this method, but must
     *  invoke this implementation.
     * 
     **/

    public void initialize(String pageName, IRequestCycle cycle)
    {
        ApplicationServlet servlet = cycle.getRequestContext().getServlet();
        String servletName = servlet.getServletName();

        ServletContext context = servlet.getServletContext();

        String name = VALUE_PERSISTER_EXTENSION_NAME + "." + servletName;

        _persister = (IValuePersister) context.getAttribute(name);

        if (_persister == null)
        {
            IApplicationSpecification spec = cycle.getEngine().getSpecification();

            if (spec.checkExtension(VALUE_PERSISTER_EXTENSION_NAME))
                _persister = (IValuePersister) spec.getExtension(VALUE_PERSISTER_EXTENSION_NAME, IValuePersister.class);
            else
                _persister = new DefaultValuePersister();

            _persister.initialize(cycle);
            
            context.setAttribute(name, _persister);
        }
    }

}