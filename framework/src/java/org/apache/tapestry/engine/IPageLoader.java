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

package org.apache.tapestry.engine;

import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ITemplateComponent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Interface exposed to components as they are loaded by the page loader.
 * 
 * @see IComponent#finishLoad(IRequestCycle, IPageLoader,
 *      org.apache.tapestry.spec.IComponentSpecification)
 * @author Howard Lewis Ship
 */

public interface IPageLoader
{
    /**
     * Invoked to create an implicit component (one which is defined in the containing component's
     * template, rather that in the containing component's specification).
     * 
     * @see org.apache.tapestry.services.impl.ComponentTemplateLoaderImpl
     * @since 3.0
     */

    public IComponent createImplicitComponent(IRequestCycle cycle, IComponent container, String componentId,
            String componentType, Location location);

    /**
     * Invoked by the {@link IPageSource}to load a specific page. This method is not reentrant. The
     * page is immediately attached to the {@link IEngine engine}.
     * 
     * @param name
     *            the simple (unqualified) name of the page to load
     * @param namespace
     *            from which the page is to be loaded (used when resolving components embedded by
     *            the page)
     * @param cycle
     *            the request cycle the page is initially loaded for (this is used to define the
     *            locale of the new page, and provide access to the corect specification source,
     *            etc.).
     * @param specification
     *            the specification for the page
     */

    public IPage loadPage(String name, INamespace namespace, IRequestCycle cycle, IComponentSpecification specification);

    /**
     * Invoked by a component (from within its
     * {@link IComponent#finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}method) to
     * load the template for the component. This will result in new components being created, and
     * the "outers" of the component being updated.
     * 
     * @see ITemplateComponent
     * @since 4.0
     */
    public void loadTemplateForComponent(IRequestCycle cycle, ITemplateComponent component);
}