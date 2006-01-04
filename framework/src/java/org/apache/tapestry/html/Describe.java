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

package org.apache.tapestry.html;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.describe.HTMLDescriber;

/**
 * Component that makes use of {@link org.apache.tapestry.describe.HTMLDescriber}to produce HTML
 * output that describes an object.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class Describe extends AbstractComponent
{
    /**
     * Parameter object: the object to be described.
     */
    public abstract Object getObject();

    /**
     * Injected service.
     */
    public abstract HTMLDescriber getDescriber();

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        getDescriber().describeObject(getObject(), writer);
    }
}