//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.record;

import java.util.Collection;
import java.util.Iterator;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IPageRecorder;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.util.prop.OgnlUtils;

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

    public abstract void commit();

    /**
     *  Returns a <code>Collection</code> of 
     *  {@link IPageChange} objects
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
                Tapestry.format(
                    "PageRecorder.change-after-lock",
                    component.getPage().getPageName(),
                    propertyName,
                    component.getExtendedId()));

        if (propertyName == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("PageRecorder.null-property-name", component.getExtendedId()));

        Object activeValue = event.getNewValue();

        try
        {
            recordChange(component.getIdPath(), propertyName, activeValue);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new ApplicationRuntimeException(
                Tapestry.format(
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

    protected abstract void recordChange(
        String componentPath,
        String propertyName,
        Object newValue);

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
                OgnlUtils.set(propertyName, resolver, component, storedValue);
            }
            catch (Throwable t)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "PageRecorder.unable-to-rollback",
                        new Object[] { propertyName, component, storedValue, t.getMessage()}),
                    t);
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
        _dirty = dirty;
    }

    protected boolean getDirty()
    {
        return _dirty;
    }

}