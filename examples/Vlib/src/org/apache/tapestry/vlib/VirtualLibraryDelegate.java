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

package org.apache.tapestry.vlib;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidationDelegate;

/**
 *  Implementation of {@link org.apache.tapestry.valid.IValidationDelegate} 
 *  which uses the
 *  correct CSS class when rendering errors.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class VirtualLibraryDelegate extends ValidationDelegate
{
    public void writeLabelPrefix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (isInError(component))
        {
            writer.begin("span");
            writer.attribute("class", "invalid-field");
        }
    }

    public void writeLabelSuffix(
        IFormComponent component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (isInError(component))
            writer.end();
    }

    public void writeAttributes(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
    {
        if (isInError())
            writer.attribute("class", "error");
    }


    public void writeSuffix(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IFormComponent component,
        IValidator validator)
    {
    }

}