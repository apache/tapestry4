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

package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Implementation of {@link IPropertySelectionRenderer} that
 *  produces a &lt;select&gt; element (containing &lt;option&gt; elements).
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class SelectPropertySelectionRenderer
    implements IPropertySelectionRenderer
{
    /**
     *  Writes the &lt;select&gt; element.  If the
     *  {@link PropertySelection} is {@link PropertySelection#isDisabled() disabled}
     *  then a <code>disabled</code> attribute is written into the tag
     *  (though Navigator 4 will ignore this).
     *
     **/

    public void beginRender(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        writer.begin("select");
        writer.attribute("name", component.getName());

        if (component.isDisabled())
            writer.attribute("disabled", "disabled");

        writer.println();
    }

    /**
     *  Closes the &lt;select&gt; element.
     *
     **/

    public void endRender(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        writer.end(); // <select>
    }

    /**
     *  Writes an &lt;option&gt; element.
     *
     **/

    public void renderOption(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
    {
        writer.beginEmpty("option");
        writer.attribute("value", model.getValue(index));

        if (selected)
            writer.attribute("selected", "selected");

        writer.print(model.getLabel(index));

        writer.end();
        
        writer.println();
    }
}