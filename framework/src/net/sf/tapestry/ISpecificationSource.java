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
package net.sf.tapestry;

import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;

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
     *  @param resourcePath a full resource path of a
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
     *  @param resourcePath a full resource path of a
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
     *  @return the namespace
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
    
    public ILibrarySpecification getLibrarySpecification(String resourcePath);
    
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
    
}