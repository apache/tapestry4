// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.workbench.components;

import java.util.List;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * This would normally be done entirely <em>without</em> using code. Trying to demonstrate that
 * you can create components without a .jwc file by matching the component type against a class name
 * (in the proper package, as defined in the application specification).
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@ComponentClass(allowBody = false, allowInformalParameters = false)
public abstract class ShowError extends AbstractComponent
{
    @Parameter(required = true)
    public abstract IValidationDelegate getDelegate();

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        IValidationDelegate delegate = getDelegate();

        if (!delegate.getHasErrors())
            return;

        writer.begin("table");
        writer.attribute("class", "inputerror");

        writer.begin("tr");
        writer.attribute("valign", "top");

        writer.begin("td");
        writer.beginEmpty("img");
        writer.attribute("src", "images/Warning.gif");
        writer.attribute("width", 42);
        writer.attribute("height", 41);
        writer.end();

        writer.begin("td");
        writer.attribute("class", "message");

        List<IRender> errorRenders = delegate.getErrorRenderers();

        errorRenders.get(0).render(writer, cycle);

        writer.end("table");
    }
}
