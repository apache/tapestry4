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

package org.apache.tapestry.workbench;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidationDelegate;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 **/

public class WorkbenchValidationDelegate extends ValidationDelegate
{
    public void writeAttributes(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
    {
        if (isInError())
            writer.attribute("class", "field-error");
    }

    public void writeSuffix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
    {
        if (isInError())
        {
            writer.print(" ");
            writer.beginEmpty("img");
            writer.attribute("src", "images/Warning-small.gif");
            writer.attribute("height", 20);
            writer.attribute("width", 20);
        }
    }

    public void writeLabelPrefix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (isInError(component))
        {
            writer.begin("span");
            writer.attribute("class", "label-error");
        }
    }

    public void writeLabelSuffix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (isInError(component))
            writer.end(); // <span>
    }
}