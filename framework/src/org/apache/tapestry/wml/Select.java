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
 *  The Select element lets users pick from a list of options. Each option
 *  is specified by an Option element. Each Option element may have one
 *  line of formatted text (which may be wrapped or truncated by the user
 *  agent if too long).
 *
 *  Unless multiple selections are required it is generally easier to use the {@link PropertySelection} component.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/
public abstract class Select extends AbstractComponent
{
    /**
     *  Used by the <code>Select</code> to record itself as a
     *  {@link IRequestCycle} attribute, so that the
     *  {@link Option} components it wraps can have access to it.
     *
     **/

    private final static String ATTRIBUTE_NAME = "org.apache.tapestry.active.Select";

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Select.may-not-nest"),
                this,
                null,
                null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        boolean render = !cycle.isRewinding();

        if (render)
        {
            writer.begin("select");

            writer.attribute("name", getName());

            String value = getValue();
            if (Tapestry.isNonBlank(value))
                writer.attribute("value", value);

            String title = getTitle();
            if (Tapestry.isNonBlank(title))
                writer.attribute("title", title);

            boolean multiple = isMultiple();
            if (multiple)
                writer.attribute("multiple", multiple);

            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (render)
        {
            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    public abstract boolean isMultiple();

    public abstract String getName();

    public abstract String getValue();

    public abstract String getTitle();
}
