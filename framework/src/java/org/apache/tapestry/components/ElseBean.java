//Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * @author mb
 */
public abstract class ElseBean extends AbstractComponent 
{
    public abstract String getElement();
    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object conditionObject = cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);

        if (conditionObject instanceof Boolean && !((Boolean) conditionObject).booleanValue()) 
        {
            String element = getElement();
            
            boolean render = !cycle.isRewinding() && HiveMind.isNonBlank(element);
            
            if (render)
            {
                writer.begin(element);
                renderInformalParameters(writer, cycle);
            }

            renderBody(writer, cycle);
            
            if (render)
                writer.end(element);
        }
    }
}
