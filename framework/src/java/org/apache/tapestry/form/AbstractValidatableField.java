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
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Abstract requirable field implementation that delegates render and bind (i.e. rewind)
 * logic to an implementation of {@link ValidatableFieldSupport}.
 * 
 * @author Paul Ferraro
 * @since  4.0
 */
public abstract class AbstractValidatableField extends AbstractRequirableField implements ValidatableField
{
    /**
     * Injected.
     */
    public abstract ValidatableFieldSupport getValidatableFieldSupport();
    
    /**
     * @see org.apache.tapestry.form.AbstractFormComponent#renderFormComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderFormComponent(writer, cycle);

        getValidatableFieldSupport().render(this, writer, cycle);
    }
    
    protected void renderContributions(IMarkupWriter writer, IRequestCycle cycle)
    {
        getValidatableFieldSupport().renderContributions(this, writer, cycle);
    }

    /**
     * @see org.apache.tapestry.form.RequirableField#bind(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void bind(IMarkupWriter writer, IRequestCycle cycle) throws ValidatorException
    {
        getValidatableFieldSupport().bind(this, writer, cycle, getSubmittedValue(cycle));
    }
    
    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad()
    {
        this.setRequiredMessage(ValidationStrings.getMessagePattern(ValidationStrings.REQUIRED_TEXT_FIELD, getPage().getLocale()));
    }
}
