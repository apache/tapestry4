// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.contrib.form.checkboxes;

import java.util.Collection;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;

/**
 * @author mb
 * @since 4.0
 */
public abstract class CheckboxGroup extends BaseComponent
{
    public final static String CHECKBOX_GROUP_ATTRIBUTE = "org.apache.tapestry.contrib.form.CheckboxGroup";

    public abstract Collection getCheckboxNames();
    public abstract String getFunctionName();
    public abstract void setFunctionName(String functionName);    

    public void registerControlledCheckbox(ControlledCheckbox checkbox)
    {
    	String name = checkbox.getCheckboxName();
    	String form = checkbox.getForm().getName();
        getCheckboxNames().add(form + "." + name);
    }
    
    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object objOtherGroup = cycle.getAttribute(CHECKBOX_GROUP_ATTRIBUTE);
        cycle.setAttribute(CHECKBOX_GROUP_ATTRIBUTE, this);
        
        initialize(cycle);
        
        super.renderComponent(writer, cycle);

        // clear the registered checkbox names after rendering
        // allows the component to be used in cycles
        getCheckboxNames().clear();

        cycle.setAttribute(CHECKBOX_GROUP_ATTRIBUTE, objOtherGroup);
    }

    private void initialize(IRequestCycle cycle)
    {
        String functionName = "setCheckboxGroup";

        if (!cycle.isRewinding()) {
	        PageRenderSupport body = TapestryUtils.getPageRenderSupport(cycle, this);
	        if (body != null)
	            functionName = body.getUniqueString("setCheckboxGroup");
        }
        
        setFunctionName(functionName);
    }
    
}
