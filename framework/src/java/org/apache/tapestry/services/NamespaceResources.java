// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.services;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;

/**
 * Companion to the standard {@link org.apache.tapestry.engine.Namespace implementation} of
 * {@link org.apache.tapestry.INamespace}. Defines resources needed by the Namespace instance to
 * operate (these have grown numerous!)
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface NamespaceResources
{
    /**
     * Finds a child library specification for some parent library specification.
     * 
     * @param libraryResource
     *            the {@link Resource} from which the parent library (or application) specification
     *            was loaded
     * @param path
     *            the relative path from the parent specification resource to the library
     *            specification. As a special case, a path starting with a leading slash is assumed
     *            to be on the classpath.
     * @param location TODO
     * @return the library specification.
     */
    ILibrarySpecification findChildLibrarySpecification(Resource libraryResource, String path, Location location);

    /**
     * Retrieves a page specification, parsing it as necessary.
     * 
     * @param libraryResource
     *            the base resource for resolving the path to the page specification; this will be
     *            the resource for the library (or application) specification
     * @param specificationPath
     *            the path to the specification to be parsed
     * @param location
     *            used to report errors
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the specification doesn't exist, is unreadable or invalid.
     * @see org.apache.tapestry.engine.ISpecificationSource#getPageSpecification(Resource)
     */

    IComponentSpecification getPageSpecification(Resource libraryResource,
            String specificationPath, Location location);

    /**
     * Retrieves a component specification, parsing it as necessary.
     * 
     * @param libraryResource
     *            the base resource for resolving the path to the page specification; this will be
     *            the resource for the library (or application) specification
     * @param specificationPath
     *            the path to the specification to be parsed
     * @param location
     *            used to report errors
     * @throws org.apache.hivemind.ApplicationRuntimeException
     *             if the specification doesn't exist, is unreadable or invalid.
     */

    public IComponentSpecification getComponentSpecification(Resource libraryResource,
            String specificationPath, Location location);

}
