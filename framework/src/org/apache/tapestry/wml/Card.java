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
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  A deck contains a collection of cards. There is a variety of card types, each specifying a different mode of
 *  user interaction.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class Card extends AbstractComponent
{
    private static final String ATTRIBUTE_NAME = "org.apache.tapestry.wml.Card";

    /**
     *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Card.cards-may-not-nest"),
                this,
                null,
                null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        writer.begin("card");

        String title = getTitle();
        if (Tapestry.isNonBlank(title))
            writer.attribute("title", title);

        renderInformalParameters(writer, cycle);

        IMarkupWriter nestedWriter = writer.getNestedWriter();

        renderBody(nestedWriter, cycle);

        nestedWriter.close();

        writer.end();

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    public abstract String getTitle();
}
