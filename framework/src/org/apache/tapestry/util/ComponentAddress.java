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
