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
 * Abstract requirable field implementation that delegates render and rewind
 * logic to an implementation of {@link RequirableFieldSupport}.
 * 
 * @author Paul Ferraro
 * @since  4.0
 */
public abstract class AbstractRequirableField extends AbstractFormComponent implements RequirableField
{
    /**
     * Injected.
     */
    public abstract RequirableFieldSupport getRequirableFieldSupport();
    
    /**
     * May by used by required fields to override the default required message pattern.
     * @param message
     */
    public abstract void setRequiredMessage(String message);
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#rewindFormComponent(org.apache.tapestry.IRequestCycle)
     */
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        getRequirableFieldSupport().rewind(this, writer, cycle);
    }
    
    /**
     * @see org.apache.tapestry.form.RequirableField#getSubmittedValue(org.apache.tapestry.IRequestCycle)
     */
    public String getSubmittedValue(IRequestCycle cycle)
    {
        return cycle.getParameter(getName());
    }
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        getRequirableFieldSupport().render(this, writer, cycle);
    }
}
