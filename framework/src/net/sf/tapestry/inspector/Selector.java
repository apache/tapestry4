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

package net.sf.tapestry.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.IPropertySelectionRenderer;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.spec.ApplicationSpecification;

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

        inspector.selectComponent((String)null);
    }

    /**
     *  Returns an {IPropertySelectionModel} used to select the name of the page
     *  to inspect.  The page names are sorted.
     *
     **/

    public IPropertySelectionModel getPageModel()
    {
        ApplicationSpecification spec = page.getEngine().getSpecification();

        Collection sortedPageNames = spec.getPageNames();

        String[] pageNames =
            (String[]) sortedPageNames.toArray(new String[sortedPageNames.size()]);

        // This should be cached for later, but there's the possibility that
        // the application specification may be dynamically modified in a running
        // application.

        return new StringPropertySelectionModel(pageNames);
    }

    /**
     *  The crumb trail is all the components from the inspected component up to
     *  (but not including) the page.
     *
     **/

    public List getCrumbTrail()
    {
        List result = null;
        
        Inspector inspector = (Inspector) page;
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

}