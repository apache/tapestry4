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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.html.BasePage;

/**
 *  The Tapestry Inspector page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public abstract class Inspector extends BasePage
{
    private Map _blocks = new HashMap();

    protected void finishLoad()
    {
        _blocks.put(View.TEMPLATE, getComponent("templateBlock"));
        _blocks.put(View.SPECIFICATION, getComponent("specificationBlock"));
        _blocks.put(View.ENGINE, getComponent("engineBlock"));
        _blocks.put(View.PROPERTIES, getComponent("propertiesBlock"));
    }

    public abstract View getView();

    public abstract void setView(View value);

    public abstract String getInspectedPageName();
    
    public abstract void setInspectedPageName(String value);

    public abstract String getInspectedIdPath();

    public abstract void setInspectedIdPath(String value);

    /** 
     *  Invoked to change the component being inspected within the current
     *  page.
     *
     *  @since 1.0.6
     **/

    public void selectComponent(String idPath)
    {
        setInspectedIdPath(idPath);
    }

    /**
     *  Method invoked by the {@link InspectorButton} component, 
     *  to begin inspecting a page.
     *
     **/

    public void inspect(String pageName, IRequestCycle cycle)
    {
        setInspectedPageName(pageName);
        selectComponent((String) null);

        cycle.activate(this);
    }

    /**
     *  Listener for the component selection, which allows a particular component.  
     *  
     *  <p>The context is a single string,
     *  the id path of the component to be selected (or null to inspect
     *  the page itself).  This invokes
     *  {@link #selectComponent(String)}.
     *
     **/

    public void selectComponent(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();

        String newIdPath;

        // The up button may generate a null context.

        if (parameters == null)
            newIdPath = null;
        else
            newIdPath = (String) parameters[0];

        selectComponent(newIdPath);
    }

    /**
     *  Returns the {@link IPage} currently inspected by the Inspector, as determined
     *  from the inspectedPageName property.
     *
     **/

    public IPage getInspectedPage()
    {
        return getRequestCycle().getPage(getInspectedPageName());
    }

    /**
     *  Returns the {@link IComponent} current inspected; this is determined
     *  from the inspectedPageName and inspectedIdPath properties.
     *
     **/

    public IComponent getInspectedComponent()
    {
        return getInspectedPage().getNestedComponent(getInspectedIdPath());
    }

    public String getInspectorTitle()
    {
        return "Tapestry Inspector: " + getEngine().getSpecification().getName();
    }

    /**
     *  Returns the {@link Block} for the currently selected view.
     *
     **/

    public Block getBlockForView()
    {
        return (Block) _blocks.get(getView());
    }
}