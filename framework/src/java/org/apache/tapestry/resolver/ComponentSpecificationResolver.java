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

package org.apache.tapestry.resolver;

import org.apache.hivemind.Location;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Service interface for locating component specifications.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface ComponentSpecificationResolver
{
    /**
     *  Passed the namespace of a container (to resolve the type in)
     *  and the type to resolve, performs the processing.  A "bare type"
     *  (without a library prefix) may be in the containerNamespace,
     *  or the framework namespace
     *  (a search occurs in that order).
     * 
     *  @param cycle current request cycle
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param type the component specification
     *  to  find, either a simple name, or prefixed with a library id
     *  (defined for the container namespace)
     * 
     *  @see #getNamespace()
     *  @see #getSpecification()
     * 
     */
    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String type,
        Location location);

    /**
     *  Like {@link #resolve(org.apache.tapestry.IRequestCycle, org.apache.tapestry.INamespace, java.lang.String, org.apache.tapestry.ILocation)},
     *  but used when the type has already been parsed into a library id and a simple type.
     * 
     *  @param cycle current request cycle
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param libraryId the library id within the container namespace, or null
     *  @param type the component specification
     *  to  find as a simple name (without a library prefix)
     *  @param location of reference to be resolved
     *  @throws ApplicationRuntimeException if the type cannot be resolved
     * 
     */
    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String libraryId,
        String type,
        Location location);

    /**
     * The specification resolved by the resolve() method.
     */
    public IComponentSpecification getSpecification();

	/**
	 * The namespace containing the resolved component.
	 */
    public INamespace getNamespace();
}