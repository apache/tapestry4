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

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 *  Implements a component that manages an HTML &lt;textarea&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/TextArea.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class TextArea extends AbstractFormComponent
{

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);
		
        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // Used whether rewinding or not.

        String name = form.getElementId(this);

        if (rewinding)
        {
        	if (!isDisabled())
	            setValue(cycle.getRequestContext().getParameter(name));

            return;
        }
        
        if (cycle.isRewinding())
        	return;

        writer.begin("textarea");

        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        renderInformalParameters(writer, cycle);

        String value = getValue();

        if (value != null)
            writer.print(value);

        writer.end();

    }

    public abstract boolean isDisabled();

    public abstract String getValue();

    public abstract void setValue(String value);
}