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

package org.apache.tapestry.wml;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * The setvar element specifies the variable to set in the current browser context as a side effect
 * of executing a task.
 * 
 * @author David Solis
 * @since 3.0
 */

public abstract class Setvar extends AbstractComponent
{
    /**
     * @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        writer.beginEmpty("setvar");

        writer.attribute("name", getName());

        renderInformalParameters(writer, cycle);

        String value = getValue();

        if (HiveMind.isNonBlank(value))
            writer.attribute("value", value);
        else
            writer.attribute("value", "");

        writer.closeTag();
    }

    public abstract String getName();

    public abstract String getValue();
}