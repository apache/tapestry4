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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IRequestCycle;

/**
 * Interface exposed to components as they are loaded by the page loader.
 *
 * @see IComponent#finishLoad(IRequestCycle, IPageLoader, org.apache.tapestry.spec.IComponentSpecification)
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public interface IPageLoader
{
    /**
     *  Returns the engine for which this page loader is curently
     *  constructing a page.
     *
     *  @since 0.2.12
     * 
     **/

    public IEngine getEngine();

    /**
     *  A convienience; returns the template source provided by
     *  the {@link IEngine engine}.
     *
     *  @since 0.2.12
     * 
     **/

    public ITemplateSource getTemplateSource();

    /**
     *  Invoked to create an implicit component (one which is defined in the
     *  containing component's template, rather that in the containing component's
     *  specification).
     * 
     *  @see org.apache.tapestry.BaseComponentTemplateLoader
     *  @since 3.0
     * 
     **/

    public IComponent createImplicitComponent(
        IRequestCycle cycle,
        IComponent container,
        String componentId,
        String componentType,
        ILocation location);
}