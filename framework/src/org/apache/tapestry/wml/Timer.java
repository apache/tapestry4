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

package org.apache.tapestry.wml;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  The Timer element declares a card timer, which exposes a means of processing inactivity or idle time.
 *  The timer is initialised and started at card entry and is stopped when the card is exited.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 */

public abstract class Timer extends AbstractComponent
{
    /**
     *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean render = !cycle.isRewinding();

        if (render)
        {
            writer.beginEmpty("timer");

            writer.attribute("name", getName());

            String value = readValue();
            if (Tapestry.isNonBlank(value))
                writer.attribute("value", value);
            else
                writer.attribute("value", "0");

            renderInformalParameters(writer, cycle);

            writer.closeTag();
        }
    }

    public abstract String getName();

    public abstract IBinding getValueBinding();

    public String readValue()
    {
        return getValueBinding().getString();
    }
}
