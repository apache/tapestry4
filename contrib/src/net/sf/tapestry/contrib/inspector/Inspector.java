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
package net.sf.tapestry.contrib.inspector;

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;
import net.sf.tapestry.html.BasePage;

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

        cycle.setPage(this);
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