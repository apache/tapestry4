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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  The onevent element binds a task to a particular intrinsic event for the immediately enclosing element, ie,
 *  specifying an onevent element inside an "XYZ" element associates an intrinsic event binding with the "XYZ" element.
 *  The user agent must ignore any onevent element specifying a type that does not correspond to a legal intrinsic event
 *  for the immediately enclosing element.
 *
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class OnEvent extends AbstractComponent
{
    /**
     *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        boolean render = !cycle.isRewinding();

        if (render)
        {
            writer.begin("onevent");

            writer.attribute("type", getType());

            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (render)
        {
            writer.end();
        }
    }

    public abstract String getType();

}
