/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 *  Implementation of {@link org.apache.tapestry.INamespace}
 *  that works with a {@link ISpecificationSource} to
 *  obtain page and component specifications as needed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class Namespace implements INamespace
{
    private ILibrarySpecification _specification;
    private ISpecificationSource _specificationSource;
    private String _id;
    private String _extendedId;
    private INamespace _parent;
    private boolean _frameworkNamespace;
    private boolean _applicationNamespace;

    /**
     *  Map of {@link org.apache.tapestry.spec.ComponentSpecification} keyed on page name.
     *  The map is synchronized because different threads may
     *  try to update it simultaneously (due to dynamic page
     *  discovery in the application namespace).
     * 
     **/

    private Map _pages = Collections.synchronizedMap(new HashMap());

    /**
     *  Map of {@link org.apache.tapestry.spec.ComponentSpecification} keyed on
     *  component alias.
     * 
     **/

    private Map _components = Collections.synchronizedMap(new HashMap());

    /**
     *  Map, keyed on id, of {@link INamespace}.
     * 
     **/

    private Map _children = Collections.synchronizedMap(new HashMap());

    public Namespace(
        String id,
        INamespace parent,
        ILibrarySpecification specification,
        ISpecificationSource specificationSource)
    {
        _id = id;
        _parent = parent;
        _specification = specification;
        _specificationSource = specificationSource;

        _applicationNamespace = (_id == null);
        _frameworkNamespace = FRAMEWORK_NAMESPACE.equals(_id);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Namespace@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');

        if (_applicationNamespace)
            buffer.append("<application>");
        else
            buffer.append(getExtendedId());

        buffer.append(']');

        return buffer.toString();
    }

    public String getId()
    {
        return _id;
    }

    public String getExtendedId()
    {
        if (_applicationNamespace)
            return null;

        if (_extendedId == null)
            _extendedId = buildExtendedId();

        return _extendedId;
    }

    public INamespace getParentNamespace()
    {
        return _parent;
    }

    public INamespace getChildNamespace(String id)
    {
        String firstId = id;
        String nextIds = null;

        // Split the id into first and next if it is a dot separated sequence
        int index = id.indexOf('.');
        if (index >= 0)
        {
            firstId = id.substring(0, index);
            nextIds = id.substring(index + 1);
        }

        // Get the first namespace
        INamespace result = (INamespace) _children.get(firstId);

        if (result == null)
        {
            result = createNamespace(firstId);

            _children.put(firstId, result);
        }

        // If the id is a dot separated sequence, recurse to find 
        // the needed namespace
        if (result != null && nextIds != null)
            result = result.getChildNamespace(nextIds);

        return result;
    }

    public List getChildIds()
    {
        return _specification.getLibraryIds();
    }

    public IComponentSpecification getPageSpecification(String name)
    {
        IComponentSpecification result = (IComponentSpecification) _pages.get(name);

        if (result == null)
        {
            result = locatePageSpecification(name);

            _pages.put(name, result);
        }

        return result;
    }

    public List getPageNames()
    {
        Set names = new HashSet();

        names.addAll(_pages.keySet());
        names.addAll(_specification.getPageNames());

        List result = new ArrayList(names);

        Collections.sort(result);

        return result;
    }

    public IComponentSpecification getComponentSpecification(String alias)
    {
        IComponentSpecification result = (IComponentSpecification) _components.get(alias);

        if (result == null)
        {
            result = locateComponentSpecification(alias);
            _components.put(alias, result);
        }

        return result;
    }

    public String getServiceClassName(String name)
    {
        return _specification.getServiceClassName(name);
    }

    public List getServiceNames()
    {
        return _specification.getServiceNames();
    }

    public ILibrarySpecification getSpecification()
    {
        return _specification;
    }

    private String buildExtendedId()
    {
        if (_parent == null)
            return _id;

        String parentId = _parent.getExtendedId();

        // If immediate child of application namespace

        if (parentId == null)
            return _id;

        return parentId + "." + _id;
    }

    /**
     *  Returns a string identifying the namespace, for use in
     *  error messages.  I.e., "Application namespace" or "namespace 'foo'".
     * 
     **/

    public String getNamespaceId()
    {
        if (_frameworkNamespace)
            return Tapestry.getMessage("Namespace.framework-namespace");

        if (_applicationNamespace)
            return Tapestry.getMessage("Namespace.application-namespace");

        return Tapestry.format("Namespace.nested-namespace", getExtendedId());
    }

    /**
     *  Gets the specification from the specification source.
     * 
     *  @throws ApplicationRuntimeException if the named page is not defined.
     * 
     **/

    private IComponentSpecification locatePageSpecification(String name)
    {
        String path = _specification.getPageSpecificationPath(name);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("Namespace.no-such-page", name, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        return _specificationSource.getPageSpecification(location);
    }

    private IComponentSpecification locateComponentSpecification(String type)
    {
        String path = _specification.getComponentSpecificationPath(type);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("Namespace.no-such-alias", type, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        return _specificationSource.getComponentSpecification(location);
    }

    private INamespace createNamespace(String id)
    {
        String path = _specification.getLibrarySpecificationPath(id);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("Namespace.library-id-not-found", id, getNamespaceId()));

        IResourceLocation location = getSpecificationLocation().getRelativeLocation(path);

        // Ok, an absolute path to a library for an application whose specification
        // is in the context root is problematic, cause getRelativeLocation()
        // will still be looking in the context.  Handle this case with the
        // following little kludge:

        if (location.getResourceURL() == null && path.startsWith("/"))
            location = new ClasspathResourceLocation(_specification.getResourceResolver(), path);

        ILibrarySpecification ls = _specificationSource.getLibrarySpecification(location);

        return new Namespace(id, this, ls, _specificationSource);
    }

    public boolean containsPage(String name)
    {
        return _pages.containsKey(name) || (_specification.getPageSpecificationPath(name) != null);
    }

    /** @since 2.3 **/

    public String constructQualifiedName(String pageName)
    {
        String prefix = getExtendedId();

        if (prefix == null)
            return pageName;

        return prefix + SEPARATOR + pageName;
    }

    /** @since 3.0 **/

    public IResourceLocation getSpecificationLocation()
    {
        return _specification.getSpecificationLocation();
    }

    /** @since 3.0 **/

    public boolean isApplicationNamespace()
    {
        return _applicationNamespace;
    }

    /** @since 3.0 **/

    public synchronized void installPageSpecification(
        String pageName,
        IComponentSpecification specification)
    {
        _pages.put(pageName, specification);
    }

    /** @since 3.0 **/

    public synchronized void installComponentSpecification(
        String type,
        IComponentSpecification specification)
    {
        _components.put(type, specification);
    }

    /** @since 3.0 **/

    public boolean containsComponentType(String type)
    {
        return _components.containsKey(type)
            || (_specification.getComponentSpecificationPath(type) != null);
    }

    /** @since 3.0 **/

    public List getComponentTypes()
    {
        Set types = new HashSet();

        types.addAll(_components.keySet());
        types.addAll(_specification.getComponentTypes());

        List result = new ArrayList(types);

        Collections.sort(result);

        return result;
    }

    /** @since 3.0 **/

    public ILocation getLocation()
    {
        if (_specification == null)
            return null;

        return _specification.getLocation();
    }

}
