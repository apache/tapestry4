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

package net.sf.tapestry.spec;

import java.util.*;

import net.sf.tapestry.INamespace;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  Specification for a library.  {@link net.sf.tapestry.spec.ApplicationSpecification}
 *  is a specialized kind of library.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class LibrarySpecification extends BasePropertyHolder
{
    /**
     *  Map of page name to page specification path.
     * 
     **/

    private Map _pages;

    /**
     *  Map of component alias to component specification path.
     * 
     **/
    private Map _components;

    /**
     *  Map of service name to service class name.
     * 
     **/

    private Map _services;

    /**
     *  Map of library id to library specification path.
     * 
     **/

    private Map _libraries;

    private String _dtdVersion;

    private String _description;
    
    public String getLibrarySpecificationPath(String id)
    {
        return (String) get(_libraries, id);
    }

    /**
     *  Sets the specification path for an embedded library.
     * 
     *  @throws IllegalArgumentException if a library with the given
     *  id already exists
     * 
     **/

    public void setLibrarySpecificationPath(String id, String path)
    {
        if (_libraries == null)
            _libraries = new HashMap();

        if (_libraries.containsKey(id))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-child-namespace-id", id));

        _libraries.put(id, path);
    }

    public List getLibraryIds()
    {
        return sortedKeys(_libraries);
    }

    public String getPageSpecificationPath(String name)
    {
        return (String) get(_pages, name);
    }

    public void setPageSpecificationPath(String name, String path)
    {
        if (_pages == null)
            _pages = new HashMap();

        if (_pages.containsKey(name))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-page-name", name));

        _pages.put(name, path);
    }

    public List getPageNames()
    {
        return sortedKeys(_pages);
    }

    public void setComponentSpecificationPath(String alias, String path)
    {
        if (_components == null)
            _components = new HashMap();

        if (_components.containsKey(alias))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-component-alias", alias));

        _components.put(alias, path);
    }

    public String getComponentSpecificationPath(String alias)
    {
        return (String) get(_components, alias);
    }

    public List getComponentAliases()
    {
        return sortedKeys(_components);
    }

    public String getServiceClassName(String name)
    {
        return (String) get(_services, name);
    }

    public List getServiceNames()
    {
        return sortedKeys(_services);
    }

    public void setServiceClassName(String name, String className)
    {
        if (_services == null)
            _services = new HashMap();

        if (_services.containsKey(name))
            throw new IllegalArgumentException(Tapestry.getString("LibrarySpecification.duplicate-service-name", name));

        _services.put(name, className);
    }

    private List sortedKeys(Map map)
    {
        if (map == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(map.keySet());

        Collections.sort(result);

        return result;
    }

    private Object get(Map map, Object key)
    {
        if (map == null)
            return null;

        return map.get(key);
    }
  
    /**
     * 
     *  Returns the documentation for this library..
     * 
     * 
     **/

    public String getDescription()
    {
        return _description;
    }

    /**
     *  
     *  Sets the documentation for this library.
     * 
     * 
     **/

    public void setDescription(String description)
    {
        _description = description;
    }

    
    public String getDTDVersion()
    {
        return _dtdVersion;
    }

    /**
     *  Sets the version number of the DTD from which this specification
     *  was created, if known.  This method exists as a convienience for
     *  the Spindle plugin.
     * 
     * 
     **/
    
    public void setDTDVersion(String dtdVersion)
    {
        _dtdVersion = dtdVersion;
    }    
   
}
