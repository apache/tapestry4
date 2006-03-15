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

package org.apache.tapestry.components;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * A component that can substitute for any HTML element. [<a
 * href="../../../../../ComponentReference/Any.html">Component Reference</a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class Any extends AbstractComponent
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String element = getElement();

        if (element == null)
            throw new ApplicationRuntimeException(ComponentMessages.anyElementNotDefined(), this,
                    null, null);

        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            writer.begin(element);

            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (!rewinding)
        {
            writer.end(element);
        }

    }

    public abstract String getElement();
}
