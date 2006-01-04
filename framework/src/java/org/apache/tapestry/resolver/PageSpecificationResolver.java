// Copyright 2004, 2005 The Apache Software Foundation
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
 * Service interface for locating a page specification given its name.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface PageSpecificationResolver
{
    /**
     * Resolve the name (which may have a library id prefix) to a namespace (see
     * {@link #getNamespace()}) and a specification (see {@link #getSpecification()}).
     * 
     * @throws org.apache.tapestry.PageNotFoundException
     *             if the name cannot be resolved to the name of a page
     */
    public void resolve(IRequestCycle cycle, String prefixedName);

    /**
     * Returns just the name of the page, unqualified by any namespace.
     */
    public String getSimplePageName();

    /**
     * Returns the namespace containing the page.
     */
    public INamespace getNamespace();

    /**
     * Returns the specification for the page.
     */
    public IComponentSpecification getSpecification();
}