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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  A component that renders an HTML &lt;option&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link Select} component.
 *
 *  [<a href="../../../../../ComponentReference/Option.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Option extends AbstractComponent
{
    /**
     *  Renders the &lt;option&gt; element, or responds when the form containing the element 
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <p>If the <code>label</code> property is set, it is inserted inside the
     *  &lt;option&gt; element.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Select select = Select.get(cycle);
        if (select == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Option.must-be-contained-by-select"),
                this,
                null,
                null);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = select.isRewinding();

        String value = select.getNextOptionId();

        if (rewinding)
        {
            if (!select.isDisabled())
                getSelectedBinding().setBoolean(select.isSelected(value));

            renderBody(writer, cycle);
        }
        else
        {
            writer.begin("option");

            writer.attribute("value", value);

            if (getSelectedBinding().getBoolean())
                writer.attribute("selected", "selected");

            renderInformalParameters(writer, cycle);

            String label = getLabel();

            if (label != null)
                writer.print(label);

            renderBody(writer, cycle);

            writer.end();
        }

    }

    public abstract IBinding getSelectedBinding();

    public abstract String getLabel();
}