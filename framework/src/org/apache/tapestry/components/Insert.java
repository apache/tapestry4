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

package org.apache.tapestry.components;

import java.text.Format;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Used to insert some text (from a parameter) into the HTML.
 *
 *  [<a href="../../../../../ComponentReference/Insert.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Insert extends AbstractComponent
{
    public abstract IBinding getFormatBinding();

    /**
     *  Prints its value parameter, possibly formatted by its format parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        Object value = getValue();

        if (value == null)
            return;

        String insert = null;

        Format format = getFormat();

        if (format == null)
        {
            insert = value.toString();
        }
        else
        {
            try
            {
                insert = format.format(value);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.format("Insert.unable-to-format", value),
                    this,
                    getFormatBinding().getLocation(),
                    ex);
            }
        }

        String styleClass = getStyleClass();

        if (styleClass != null)
        {
            writer.begin("span");
            writer.attribute("class", styleClass);

            renderInformalParameters(writer, cycle);
        }

        if (getRaw())
            writer.printRaw(insert);
        else
            writer.print(insert);

        if (styleClass != null)
            writer.end(); // <span>
    }

    public abstract Object getValue();

    public abstract Format getFormat();

    public abstract String getStyleClass();

    public abstract boolean getRaw();

}