// Copyright May 20, 2006 The Apache Software Foundation
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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.Body;


/**
 * Interface that defines the chain of render workers that will be 
 * invoked after all {@link IComponent} render invocations.
 * 
 * @author jkuhnert
 */
public interface ComponentRenderWorker
{
    
    /**
     * Invoked just after the components render call, giving services 
     * implementing the {@link ComponentRenderWorker} interface a guaranteed 
     * state to work off of.
     * 
     * @param cycle
     *          The associated request for this render.
     * @param component
     *          The component that has just been rendered.
     */
    void renderComponent(IRequestCycle cycle, IComponent component);
    
    /**
     * Special render for handling html element targets. This is invoked
     * just after the body component renders its body, but before the script
     * data is written out. 
     * 
     * @param cycle
     *          The associated request cycle.
     * @param component
     *          The {@link Body} component, which holds the needed {@link PageRenderSupport} object.
     */
    void renderBody(IRequestCycle cycle, Body component);
}
