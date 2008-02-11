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
package org.apache.tapestry.form;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;


/**
 * The default implementation of {@link IOptionRenderer} which is used by 
 * the {@link PropertySelection} component if no other renderer is specified
 * in the component parameters.
 */
public class DefaultOptionRenderer implements IOptionRenderer
{

    /**
     * Default instance used by {@link PropertySelection} if no custom renderer is 
     * specified.
     */
    
    public static final IOptionRenderer DEFAULT_INSTANCE = new DefaultOptionRenderer();
    
    /**
     * {@inheritDoc}
     */
    public void renderOptions(IMarkupWriter writer, IRequestCycle cycle, IPropertySelectionModel model, Object selected)
    {
        int count = model.getOptionCount();
        boolean foundSelected = false;
        
        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);
            
            writer.begin("option");
            writer.attribute("value", model.getValue(i));

            if (!foundSelected && isEqual(option, selected))
            {
                writer.attribute("selected", "selected");
                
                foundSelected = true;
            }
            
            if (model.isDisabled(i))
                writer.attribute("disabled", "disabled");
            
            writer.print(model.getLabel(i));

            writer.end();

            writer.println();
        }
    }
    
    protected boolean isEqual(Object left, Object right)
    {
        // Both null, or same object, then are equal

        if (left == right)
            return true;
        
        // If one is null, the other isn't, then not equal.
        
        if (left == null || right == null)
            return false;
        
        // Both non-null; use standard comparison.
        
        return left.equals(right);
    }
}
