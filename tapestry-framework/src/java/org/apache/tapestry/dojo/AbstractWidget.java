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
package org.apache.tapestry.dojo;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;


/**
 * The base widget class for all dojo related widget components.
 *
 * @author jkuhnert
 */
public abstract class AbstractWidget extends AbstractComponent implements IWidget
{
    
    public abstract void setDestroy(boolean destroy);
    
    /**
     * Determined dynamically at runtime during rendering, informs widget implementations
     * if they should destroy their client side widget equivalents or leave them in tact.
     * 
     * @return True if the widget should be destroyed on this render, false otherwise.
     */
    public abstract boolean getDestroy();
    
    /**
     * {@inheritDoc}
     */
    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if(!cycle.isRewinding()) {

            if (!cycle.getResponseBuilder().isDynamic() 
                    || cycle.getResponseBuilder().explicitlyContains(this)) {
                
                setDestroy(false);
            } else {
                
                setDestroy(true);
            }
        }
        
        renderWidget(writer, cycle);
    }
}
