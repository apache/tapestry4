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

package org.apache.tapestry.util;

import java.io.Serializable;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

/**
 * The ComponentAddress class contains the path to a component, allowing it to  
 * locate an instance of that component in a different 
 * {@link org.apache.tapestry.IRequestCycle}.
 * 
 * <p>This class needs to be used mostly when working with components
 * accessed via the {@link org.apache.tapestry.IRender} interface. 
 * It allows those components to serialize and
 * pass as a service parameter information about what component they have to 
 * talk to if control returns back to them. 
 * 
 * <p>This situation often occurs when the component used via IRender contains
 * Direct or Action links.
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.2
 * 
 */
public class ComponentAddress implements Serializable
{
    private String _pageName;
    private String _idPath;

    /**
     * Creates a new ComponentAddress object that carries the identification 
     * information of the given component (the page name and the ID path).
     * @param component the component to get the address of
     */
    public ComponentAddress(IComponent component)
    {
        this(component.getPage().getPageName(), component.getIdPath());
    }

    /**
     * Creates a new ComponentAddress using the given Page Name and ID Path
     * @param pageName the name of the page that contains the component
     * @param idPath the ID Path of the component
     */
    public ComponentAddress(String pageName, String idPath)
    {
        _pageName = pageName;
        _idPath = idPath;
    }

    /**
     * Creates a new ComponentAddress using the given Page Name and ID Path
     * relative on the provided Namespace
     * @param namespace the namespace of the page that contains the component
     * @param pageName the name of the page that contains the component
     * @param idPath the ID Path of the component
     */
    public ComponentAddress(
        INamespace namespace,
        String pageName,
        String idPath)
    {
        this(namespace.constructQualifiedName(pageName), idPath);
    }

    /**
     * Finds a component with the current address using the given RequestCycle.
     * @param cycle the RequestCycle to use to locate the component
     * @return IComponent a component that has been initialized for the given RequestCycle
     */
    public IComponent findComponent(IRequestCycle cycle)
    {
        IPage objPage = cycle.getPage(_pageName);
        return objPage.getNestedComponent(_idPath);
    }

    /**
     * Returns the idPath of the component.
     * @return String the ID path of the component
     */
    public String getIdPath()
    {
        return _idPath;
    }

    /**
     * Returns the Page Name of the component.
     * @return String the Page Name of the component
     */
    public String getPageName()
    {
        return _pageName;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int hash = _pageName.hashCode() * 31;
        if (_idPath != null)
            hash += _idPath.hashCode();
        return hash;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ComponentAddress))
            return false;

        if (obj == this)
            return true;

        ComponentAddress objAddress = (ComponentAddress) obj;
        if (!getPageName().equals(objAddress.getPageName()))
            return false;

        String idPath1 = getIdPath();
        String idPath2 = objAddress.getIdPath();
        return (idPath1 == idPath2)
            || (idPath1 != null && idPath1.equals(idPath2));
    }

}
