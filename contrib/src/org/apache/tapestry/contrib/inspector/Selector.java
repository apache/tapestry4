/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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