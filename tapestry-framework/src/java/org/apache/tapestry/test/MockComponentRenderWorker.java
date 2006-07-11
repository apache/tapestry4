// Copyright Jul 10, 2006 The Apache Software Foundation
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
package org.apache.tapestry.test;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.services.ComponentRenderWorker;


/**
 * Placeholder class used to allow proper creation of {@link AbstractComponent} classes
 * that normally rely on it being injected at runtime. Used primarily by {@link Creator}.
 * 
 * @author jkuhnert
 */
public class MockComponentRenderWorker implements ComponentRenderWorker
{

    /** Does nothing. */
    public MockComponentRenderWorker()
    {
    }
    
    /** 
     * {@inheritDoc}
     */
    public void renderBody(IRequestCycle cycle, Body component)
    {
    }

    /** 
     * {@inheritDoc}
     */
    public void renderComponent(IRequestCycle cycle, IComponent component)
    {
    }

}
