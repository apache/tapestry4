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
import net.sf.tapestry.spec.ILibrarySpecification;
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
    private ILibrarySpecification _specification;
    private ISpecificationSource _specificationSource;
    private String _id;
    private String _extendedId;
    private INamespace _parent;
    private boolean _frameworkNamespace;
    private boolean _applicationNamespace;

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

    public synchronized INamespace getChildNamespace(String id)
    {
        String firstId = id;
        String nextIds = null;
        
        // Split the id into first and next if it is a dot separated sequence
        int index = id.indexOf('.');
        if (index >= 0) {
            firstId = id.substring(0, index);
            nextIds = id.substring(index+1);
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

    public synchronized ComponentSpecification getPageSpecification(String name)
    {
        ComponentSpecification result = (ComponentSpecification) _pages.get(name);

        if (result == null)
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

        if (result == null)
        {
            result = locateComponentSpecification(alias);
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

    private String getNamespaceId()
    {
        if (_frameworkNamespace)
            return Tapestry.getString("Namespace.framework-namespace");
            
        if (_applicationNamespace)
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

        ILibrarySpecification ls = _specificationSource.getLibrarySpecification(path);
 
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
