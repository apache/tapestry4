// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.contrib.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.hivemind.service.ClassFabUtils;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.record.PropertyChange;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;

/**
 * Component of the {@link Inspector}page used to display the persisent properties of the page.
 * 
 * @author Howard Lewis Ship
 */

public abstract class ShowProperties extends BaseComponent implements PageBeginRenderListener
{

    public abstract void setProperties(List properties);

    public abstract PropertyChange getChange();

    /** Injected */

    public abstract PropertyPersistenceStrategySource getPropertySource();

    public void pageBeginRender(PageEvent event)
    {
        Inspector inspector = (Inspector) getPage();

        IPage inspectedPage = inspector.getInspectedPage();

        String pageName = inspectedPage.getPageName();

        PropertyPersistenceStrategySource source = getPropertySource();

        Collection properties = source.getAllStoredChanges(pageName);

        // TODO: sorting

        setProperties(new ArrayList(properties));
    }

    /**
     * Returns the name of the value's class, if the value is non-null.
     */

    public String getValueClassName()
    {
        Object value = getChange().getNewValue();

        if (value == null)
            return "<null>";

        return ClassFabUtils.getJavaClassName(value.getClass());
    }
}