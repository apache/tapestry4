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
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  A high level component used to render a drop-down list of options that the user may select.
 *
 *  Informal parameters are applied to the <select> tag.  To have greater control over the <option> tags, you must use
 *  a Select and Option or a concrete class of {@link org.apache.tapestry.link.ILinkRenderer} with the
 *  {@link OptionRenderer}.
 *
 *  @version $Id$
 *  @author David Solis
 */

public abstract class PropertySelection extends AbstractComponent
{
    /**
     *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean render = !cycle.isRewinding();

        if (render)
        {
            IPropertySelectionModel model = getModel();

            writer.begin("select");

            writer.attribute("name", getName());

            renderInformalParameters(writer, cycle);

            writer.println();

            int count = model.getOptionCount();

            for (int i = 0; i < count; i++)
            {

                writer.begin("option");
                writer.attribute("value", model.getValue(i));

                writer.print(model.getLabel(i));

                writer.end();
                writer.println();
            }

            writer.end();
        }
    }

    public abstract IPropertySelectionModel getModel();

    public abstract String getName();
}
