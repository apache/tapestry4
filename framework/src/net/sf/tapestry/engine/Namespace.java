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

package net.sf.tapestry.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.LibrarySpecification;

/**
 *  Implementation of {@link net.sf.tapestry.INamespace}
 *  that works with a {@link net.sf.tapestry.ISpecificationSource} to
 *  obtain page and component specifications as needed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class Namespace implements INamespace
{
    private LibrarySpecification _specification;
    private ISpecificationSource _specificationSource;
    private String _id;
    private String _extendedId;
    private INamespace _parent;

    public Namespace(
        String id,
        INamespace parent,
        LibrarySpecification specification,
        ISpecificationSource specificationSource)
    {
        _id = id;
        _parent = parent;
        _specification = specification;
        _specificationSource = specificationSource;
    }

    /**
     *  Map of {@link ComponentSpecification} keyed on page name.
     * 
     **/

    private Map _pages = new HashMap();

    /**
     *  Map of {@link ComponentSpecification} keyed on
     *  component alias.
     * 
     **/

    private Map _components = new HashMap();

    /**
     *  Map, keyed on id, of {@link INamespace}.
     * 
     **/

    private Map _children = new HashMap();

    public String getId()
    {
        return _id;
    }

    public String getExtendedId()
    {
        if (_parent == null)
            return null;

        if (_extendedId == null)
            _extendedId = buildExtendedId();

        return _extendedId;
    }

    public INamespace getParentNamespace()
    {
        return _parent;
    }

    public synchronized INamespace getChildNamespace(String id)
    {
        INamespace result = (INamespace) _children.get(id);

        if (result == null)
        {
            result = createNamespace(id);

            _children.put(id, result);
        }

        return result;
    }

    public List getChildIds()
    {
        return _specification.getLibraryIds();
    }

    public synchronized ComponentSpecification getPageSpecification(String name)
    {
        ComponentSpecification result = (ComponentSpecification) _pages.get(name);

        if (name == null)
        {
            result = locatePageSpecification(name);

            if (result != null)
                _pages.put(name, result);
        }

        return result;
    }

    public List getPageNames()
    {
        return _specification.getPageNames();
    }

    public synchronized ComponentSpecification getComponentSpecification(String alias)
    {
        ComponentSpecification result = (ComponentSpecification) _components.get(alias);

        if (alias == null)
        {
            result = locateComponentSpecification(alias);

            if (result != null)
                _components.put(alias, result);
        }

        return result;
    }

    public List getComponentAliases()
    {
        return _specification.getComponentAliases();
    }

    public String getServiceClassName(String name)
    {
        return _specification.getServiceClassName(name);
    }

    public List getServiceNames()
    {
        return _specification.getServiceNames();
    }

    public LibrarySpecification getSpecification()
    {
        return _specification;
    }

    private String buildExtendedId()
    {
        String parentId = _parent.getExtendedId();

        if (parentId == null)
            return _id;

        return parentId + "." + _id;
    }

    /**
     *  Returns a string identifying the namespace, for use in
     *  error messages.  I.e., "Application namespace" or "namespace 'foo'".
     * 
     **/

    private String getNamespaceId()
    {
        if (_parent == null)
            return Tapestry.getString("Namespace.application-namespace");

        return Tapestry.getString("Namespace.nested-namespace", getExtendedId());
    }

    private ComponentSpecification locatePageSpecification(String name)
    {
        String path = _specification.getPageSpecificationPath(name);

        if (path == null)
            throw new ApplicationRuntimeException(Tapestry.getString("Namespace.no-such-page", name, getNamespaceId()));

        return _specificationSource.getPageSpecification(path);
    }

    private ComponentSpecification locateComponentSpecification(String alias)
    {
        String path = _specification.getComponentSpecificationPath(alias);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Namespace.no-such-alias", alias, getNamespaceId()));
        ;

        return _specificationSource.getComponentSpecification(path);
    }

    private INamespace createNamespace(String id)
    {
        String path = _specification.getLibrarySpecificationPath(id);

        if (path == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Namespace.library-id-not-found", id, getNamespaceId()));

        LibrarySpecification ls = _specificationSource.getLibrarySpecification(path);
 
        return new Namespace(id, this, ls, _specificationSource);
    }
    
    public boolean containsAlias(String alias)
    {
        return _specification.getComponentSpecificationPath(alias) != null;
    }

    public boolean containsPage(String name)
    {
        return _specification.getPageSpecificationPath(name) != null;
    }

}
