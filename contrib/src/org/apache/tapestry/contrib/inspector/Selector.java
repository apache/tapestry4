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

package org.apache.tapestry.contrib.inspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

/**
 *  Component of the {@link Inspector} page used to select the page and "crumb trail"
 *  of the inspected component.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Selector extends BaseComponent
{
    /**
     *  When the form is submitted,
     *  the inspectedPageName of the {@link Inspector} page will be updated,
     *  but we need to reset the inspectedIdPath as well.
     *
     **/

    public void formSubmit(IRequestCycle cycle)
    {
        Inspector inspector = (Inspector) getPage();

        inspector.selectComponent((String) null);
    }

    /**
     *  Returns an {IPropertySelectionModel} used to select the name of the page
     *  to inspect.  The page names are sorted.
     *
     **/

    public IPropertySelectionModel getPageModel()
    {
        return new StringPropertySelectionModel(getPageNames());
    }

    /**
     *  The crumb trail is all the components from the inspected component up to
     *  (but not including) the page.
     *
     **/

    public List getCrumbTrail()
    {
        List result = null;

        Inspector inspector = (Inspector) getPage();
        IComponent component = inspector.getInspectedComponent();
        IComponent container = null;

        while (true)
        {
            container = component.getContainer();
            if (container == null)
                break;

            if (result == null)
                result = new ArrayList();

            result.add(component);

            component = container;
        }

        if (result == null)
            return null;

        // Reverse the list, such that the inspected component is last, and the
        // top-most container is first.

        Collections.reverse(result);

        return result;
    }

    private String[] getPageNames()
    {
        Set names = new HashSet();

        ISpecificationSource source = getPage().getEngine().getSpecificationSource();

        addPageNames(names, source.getFrameworkNamespace());
        addPageNames(names, source.getApplicationNamespace());

        List l = new ArrayList(names);
        Collections.sort(l);

        return (String[]) l.toArray(new String[l.size()]);
    }

    private void addPageNames(Set names, INamespace namespace)
    {
        String idPrefix = namespace.getExtendedId();

        List pageNames = namespace.getPageNames();
        int count = pageNames.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) pageNames.get(i);

            if (idPrefix == null)
                names.add(name);
            else
                names.add(idPrefix + ":" + name);
        }

        List ids = namespace.getChildIds();
        count = ids.size();

        for (int i = 0; i < count; i++)
        {
            String id = (String) ids.get(i);

            addPageNames(names, namespace.getChildNamespace(id));
        }
    }

}