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
package net.sf.tapestry.spec;

import java.util.List;
import java.util.Map;

import net.sf.tapestry.IResourceLocation;
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

    /**
     *  Returns the specification path (within the classpath) for
     *  an embedded library, or null if
     *  no such library has been defined.
     * 
     **/
    
    public String getLibrarySpecificationPath(String id);

    /**
     *  Sets the specification path for an embedded library.
     * 
     *  @throws IllegalArgumentException if a library with the given
     *  id already exists
     * 
     **/

    public void setLibrarySpecificationPath(String id, String path);

    /**
     *  Returns a sorted list of library ids (or the empty list, but not null).
     * 
     **/
    
    public List getLibraryIds();
    
    public String getPageSpecificationPath(String name);

    public void setPageSpecificationPath(String name, String path);

    /**
     *  Returns a sorted list of page names explicitly defined by this library,
     *  or an empty list (but not null).
     * 
     **/
    
    public List getPageNames();
    
    public void setComponentSpecificationPath(String type, String path);

    public String getComponentSpecificationPath(String type);

    /**
     *  Returns the simple types ('alias' is an archaic term) of
     *  all components defined in this library.
     * 
     *  @deprecated To be removed after release 2.4, use
     *  {@link #getComponentTypes()} instead.
     * 
     **/
    
    public List getComponentAliases();

    /**
     *  Returns the simple types of all components defined in
     *  this library.  Returns a list of strings in sorted order,
     *  or an empty list (but not null).
     * 
     *  @since 2.4
     * 
     **/

    public List getComponentTypes();
    
    public String getServiceClassName(String name);

    /**
     *  Returns a sorted list of service names (or an empty list, but
     *  not null).
     * 
     **/
    
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
     *  Returns an instantiated extension, performing a check to ensure
     *  that the extension is a subtype of the given class (or extends the given
     *  interface).
     * 
     *  @throws IllegalArgumentException if no extension specification exists for
     *  the given name, or if the extension fails the type check.
     * 
     *  @since 2.4
     * 
     **/
    
    public Object getExtension(String name, Class typeConstraint);

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

    /**
     *  Returns the location from which the specification was read.
     * 
     *  @since 2.4
     * 
     **/
    
    public IResourceLocation getSpecificationLocation();
    
    /** @since 2.4 **/
    
    public void setSpecificationLocation(IResourceLocation specificationLocation);    
}
