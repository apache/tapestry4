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
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  The do element provides a general mechanism for the user to act upon the current card,
 *  in other words a card-level user interface element.
 *  The representation of the do element is user agent dependent and the author must only assume
 *  that the element is mapped to a unique user interface widget that the user can activate.
 *  For example, the widget mapping may be to a graphically rendered button, a soft or function key, a voice-activated command sequence, or any other interface that has a simple "activate" operation with no inter-operation persistent state.
 *  The do element may appear at both the card and deck-level.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class Do extends AbstractComponent
{
    /**
     *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean render = !cycle.isRewinding();

        if (render)
        {
            writer.begin("do");

            writer.attribute("type", getType());

            String label = getLabel();
            if (Tapestry.isNonBlank(label))
                writer.attribute("label", label);

            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (render)
            writer.end();
    }

    public abstract String getType();

    public abstract String getLabel();
}