// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.form.validator;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Abstract implementation of {@link org.apache.tapestry.form.validator.Validator} that delegates to
 * some other object.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class AbstractValidatorWrapper implements Validator
{
    protected abstract Validator getDelegate();

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        getDelegate().validate(field, messages, object);
    }

    public boolean getAcceptsNull()
    {
        return getDelegate().getAcceptsNull();
    }

    public boolean isRequired()
    {
        return getDelegate().isRequired();
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        getDelegate().renderContribution(writer, cycle, context, field);
    }

}
