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

import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Delegate interface used when a page or component specification
 *  can not be found by the normal means.  This allows hooks
 *  to support specifications from unusual locations, or generated
 *  on the fly.
 * 
 *  <p>The delegate must be coded in a threadsafe manner.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public interface ISpecificationResolverDelegate
{
    /**
     *  Invoked by {@link PageSpecificationResolver} to find the indicated
     *  page specification.  Returns
     *  the specification, or null.  The specification, if returned, is not cached by Tapestry
     *  (it is up to the delegate to cache the specification if desired).
     * 
     *  @param cycle used to gain access to framework and Servlet API objects
     *  @param namespace the namespace containing the page
     *  @param simplePageName the name of the page (without any namespace prefix)
     * 
     **/

    public IComponentSpecification findPageSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String simplePageName);

    /**
     *  Invoked by {@link PageSpecificationResolver} to find the indicated
     *  component specification.  Returns
     *  the specification, or null.  The specification, if returned, is not cached by Tapestry
     *  (it is up to the delegate to cache the specification if desired).
     * 
     *  <p>The delegate must be coded in a threadsafe manner.
     * 
     *  @param cycle used to gain access to framework and Servlet API objects
     *  @param namespace the namespace containing the component
     *  @param type the component type (without any namespace prefix)
     * 
     **/

    public IComponentSpecification findComponentSpecification(
        IRequestCycle cycle,
        INamespace namespace,
        String type);
}
