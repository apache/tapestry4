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

package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.LibrarySpecification;

/**
 *  Defines access to component specifications.
 *
 *  @see ComponentSpecification
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface ISpecificationSource
{    
    /**
     *  Retrieves a component specification, parsing it as necessary.
     *  
     *  @parameter resourcePath a full resource path of a
     *  component specification file.
     * 
     *  @throws ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/
    
    public ComponentSpecification getComponentSpecification(String resourcePath);  
 
    /**
     *  Retrieves a component specification, parsing it as necessary.
     *  
     *  @parameter resourcePath a full resource path of a
     *  page specification file.
     * 
     *  @throws ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/
        
    public ComponentSpecification getPageSpecification(String resourcePath);
            
	/**
	 *  Gets a specification from the cache, possibly parsing it at the same time.
	 *
	 *  <p>The type is used to locate the resource that defines the specification.  In
	 *  practical terms, this is the XML file which contains the specification.
	 *
	 *  @throws ApplicationRuntimeException if the specification cannot be located or loaded.
	 *
     *  @deprecated To be removed in 2.3.  
     *  Use {@link #getComponentSpecification(String)} or {@link #getPageSpecification(String)}
     *  instead.
     * 
	 **/

	public ComponentSpecification getSpecification(String type);
    
	/**
	 *  Invoked to have the source clear any internal cache.  This is most often
	 *  used when debugging an application.
	 *
	 **/

	public void reset();
    
    /**
     *  Returns a {@link INamespace} for the given id.
     * 
     *  @param id the name of the namespace, possibly as a dotted name
     *  sequence.  Null for the application namespace, "framework"
     *  for the framework namespace.
     *  @returns the namespace
     *  @throws ApplicationRuntimeException if the namespace cannot
     *  be located.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getNamespace(String id);
    
    /**
     *  Returns a {@link LibrarySpecification} with the given path.
     * 
     *  @param resourcePath the resource path of the specification
     *  to return
     *  @throws ApplicationRuntimeException if the specification
     *  cannot be read
     * 
     *  @since 2.2
     * 
     **/
    
    public LibrarySpecification getLibrarySpecification(String resourcePath);
    
    /**
     *  Returns the {@link INamespace} for the application.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getApplicationNamespace();
    
    
    /**
     *  Returns the {@link INamespace} for the framework itself.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getFrameworkNamespace();
    
    /**
     *  Returns the {@link INamespace} responsible for the given page.
     *  Checks to see if the name contains a colon and, if so, extracts
     *  its namespace id.  If not, searches first the application, then the
     *  framework, for the matching name.
     * 
     *  @since 2.2
     * 
     **/
    
    public INamespace getNamespaceForPageName(String name);
}