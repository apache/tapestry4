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

package org.apache.tapestry.contrib.components;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.Conditional;

/**
 *  Represents an alternative whithin a {@link Choose} component. 
 *  The default alternative is described by the Otherwise component.
 *
 *  [<a href="../../../../../../ComponentReference/contrib.When.html">Component Reference</a>]
 *
 *  @author David Solis
 *  @version $Id$
 * 
 **/
public abstract class When extends Conditional
{
    /** Parent of this component. */

    private Choose _choose;

    /**
     *  Renders its wrapped components only if the condition is true and its parent {@link Choose}
     *  allows it. In addition, if element is specified, can emulate that HTML element.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Choose choose = getChoose();

        if (choose == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("When.must-be-contained-by-choose"),
                this,
                null,
                null);

        if (!choose.isConditionMet() && getCondition())
        {
            choose.setConditionMet(true);
            super.renderComponent(writer, cycle);
        }
    }

    protected boolean evaluateCondition()
    {
        return true;
    }

    public boolean getInvert()
    {
        // This component doesn't require invert parameter.
        return false;
    }

    /**
     *  @return Choose
     */
    public Choose getChoose()
    {
        return _choose;
    }

    /**
     *  Sets the choose.
     *  @param value The choose to set
     */
    public void setChoose(Choose value)
    {
        _choose = value;
    }

}
