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
 *  This component serves as a container for one item that is listed as a choice in a {@link Select}. A {@link Select}
 *  offers a selection of choices from which usersu may choose one or more items. The select list is created using a
 *  select element which contains a collection of option elements. A string or text describing the item appears between
 *  the opening and closing option tags.
 *
 *  In order to have a dynamic onpick attribute it is better to use a concrete class of
 *  {@link org.apache.tapestry.link.ILinkRenderer} with the {@link OptionRenderer}.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class Option extends AbstractComponent {

	/**
	 *  @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 **/

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean render = !cycle.isRewinding();

        if (render)
        {
            writer.begin("option");

            String value = getValue();
            if (Tapestry.isNonBlank(value))
                writer.attribute("value", value);

            renderInformalParameters(writer, cycle);

            renderBody(writer, cycle);

            writer.end();
        }
	}

    public abstract String getValue();
}
