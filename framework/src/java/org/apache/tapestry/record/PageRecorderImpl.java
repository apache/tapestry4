// Copyright 2005 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPageRecorder;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PageRecorderImpl implements IPageRecorder
{
    private String _pageName;

    private IRequestCycle _requestCycle;

    private PropertyPersistenceStrategySource _strategySource;

    private boolean _locked = false;

    private ErrorLog _log;

    public PageRecorderImpl(String pageName, IRequestCycle requestCycle,
            PropertyPersistenceStrategySource strategySource, ErrorLog log)
    {
        Defense.notNull(pageName, "pageName");
        Defense.notNull(requestCycle, "requestCycle");
        Defense.notNull(strategySource, "strategySource");
        Defense.notNull(log, "log");

        _pageName = pageName;
        _requestCycle = requestCycle;
        _strategySource = strategySource;
        _log = log;
    }

    public void commit()
    {
        _locked = true;
    }

    public Collection getChanges()
    {
        return _strategySource.getAllStoredChanges(_pageName, _requestCycle);
    }

    public void rollback(IPage page)
    {
        Collection changes = getChanges();

        Iterator i = changes.iterator();

        while (i.hasNext())
        {
            IPageChange change = (IPageChange) i.next();

            applyChange(page, change);
        }
    }

    private void applyChange(IPage page, IPageChange change)
    {
        String idPath = change.getComponentPath();

        IComponent component = (idPath == null) ? page : page.getNestedComponent(idPath);

        PropertyUtils.write(component, change.getPropertyName(), change.getNewValue());
    }

    public void observeChange(ObservedChangeEvent event)
    {
        IComponent component = event.getComponent();
        String propertyName = event.getPropertyName();

        if (_locked)
        {
            _log.error(RecordMessages.recorderLocked(propertyName, component), null, null);
            return;
        }

        PropertyPersistenceStrategy strategy = findStrategy(component, propertyName);

        if (strategy != null)
            strategy.store(_pageName, component.getIdPath(), propertyName, event.getNewValue());
    }

    // package private for testing

    PropertyPersistenceStrategy findStrategy(IComponent component, String propertyName)
    {
        // So much for Law of Demeter!

        IPropertySpecification propertySpecification = component.getSpecification()
                .getPropertySpecification(propertyName);

        if (propertySpecification == null)
        {
            _log.error(
                    RecordMessages.missingPropertySpecification(propertyName, component),
                    null,
                    null);
            return null;
        }

        String name = propertySpecification.getPersistence();

        // Should check for nulls, but the architecture of the framework pretty much
        // ensures that we won't get here unless there is a property
        // and a persistence value for the property.

        try
        {
            return _strategySource.getStrategy(name);
        }
        catch (ApplicationRuntimeException ex)
        {
            _log.error(ex.getMessage(), propertySpecification.getLocation(), ex);

            return null;
        }
    }

}