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

import java.util.List;
import java.util.Map;

import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.util.IPropertyHolder;

/**
 *  Interface for the Specification for a library.  {@link net.sf.tapestry.spec.ApplicationSpecification}
 *  is a specialized kind of library.
 *
 *  @author Geoffrey Longman
 *  @version $Id$
 *  @since 2.2
 *
 **/

public interface ILibrarySpecification extends IPropertyHolder
{


    public String getLibrarySpecificationPath(String id);

    /**
     *  Sets the specification path for an embedded library.
     * 
     *  @throws IllegalArgumentException if a library with the given
     *  id already exists
     * 
     **/

    public void setLibrarySpecificationPath(String id, String path);

    public List getLibraryIds();
    
    public String getPageSpecificationPath(String name);

    public void setPageSpecificationPath(String name, String path);

    public List getPageNames();
    
    public void setComponentSpecificationPath(String alias, String path);

    public String getComponentSpecificationPath(String alias);

    public List getComponentAliases();

    public String getServiceClassName(String name);

    public List getServiceNames();

    public void setServiceClassName(String name, String className);


    /**
     * 
     *  Returns the documentation for this library..
     * 
     * 
     **/

    public String getDescription();

    /**
     *  
     *  Sets the documentation for this library.
     * 
     * 
     **/

    public void setDescription(String description);
    
    /**
     *  Returns a Map of extensions; key is extension name, value is
     *  {@link net.sf.tapestry.spec.ExtensionSpecification}.
     *  May return null.  The returned Map is immutable.
     * 
     **/

    public Map getExtensionSpecifications();

    /**
     *  Adds another extension specification.
     *  
     **/

    public void addExtensionSpecification(String name, ExtensionSpecification extension);
    
    /**
     *  Returns a sorted List of the names of all extensions.  May return the empty list,
     *  but won't return null.
     * 
     **/

    public List getExtensionNames();
    
    /**
     *  Returns the named ExtensionSpecification, or null if it doesn't exist.
     * 
     **/

    public ExtensionSpecification getExtensionSpecification(String name);
    

    /**
     *  Returns an instantiated extension.  Extensions are created as needed and
     *  cached for later use.
     * 
     *  @throws IllegalArgumentException if no extension specification exists for the
     *  given name.
     * 
     **/

    public Object getExtension(String name);

    /**
     *  Returns true if the named extension exists (or can be instantiated),
     *  returns false if the named extension has no specification.
     * 
     **/
    
    public boolean checkExtension(String name);

    /**
     *  Invoked after the entire specification has been constructed
     *  to instantiate any extensions marked immediate.
     * 
     **/

    public void instantiateImmediateExtensions();

    public IResourceResolver getResourceResolver();

    public void setResourceResolver(IResourceResolver resolver);
    
    public String getPublicId();
    
    public void setPublicId(String value);

}
