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

package org.apache.tapestry.engine;

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 *  Defines access to component specifications.
 *
 *  @see IComponentSpecification
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
     *  @param specificationLocation the location where the specification
     *  may be read from.
     * 
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/

    public IComponentSpecification getComponentSpecification(IResourceLocation specificationLocation);

    /**
     *  Retrieves a component specification, parsing it as necessary.
     *  
     *  @param specificationLocation the location where the specification
     *  may be read from.
     * 
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the specification doesn't
     *  exist, is unreadable or invalid.
     * 
     *  @since 2.2
     * 
     **/

    public IComponentSpecification getPageSpecification(IResourceLocation specificationLocation);

    /**
     *  Invoked to have the source clear any internal cache.  This is most often
     *  used when debugging an application.
     *
     **/

    public void reset();

    /**
     *  Returns a {@link org.apache.tapestry.spec.LibrarySpecification} with the given path.
     * 
     *  @param specificationLocation the resource path of the specification
     *  to return
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the specification
     *  cannot be read
     * 
     *  @since 2.2
     * 
     **/

    public ILibrarySpecification getLibrarySpecification(IResourceLocation specificationLocation);

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