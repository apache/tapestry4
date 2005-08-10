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

package org.apache.tapestry.form;

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

public class TranslatedFieldSupportImpl implements TranslatedFieldSupport
{
    private ThreadLocale _threadLocale;

    /**
     * @return Returns the threadLocale.
     */
    public ThreadLocale getThreadLocale()
    {
        return _threadLocale;
    }

    /**
     * @param threadLocale The threadLocale to set.
     */
    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    public String format(TranslatedField field, Object object)
    {
        IValidationDelegate delegate = field.getForm().getDelegate();
        
        return delegate.isInError() ? delegate.getFieldInputValue() : field.getTranslator().format(field, object);
    }
    
    public Object parse(TranslatedField field, String text) throws ValidatorException
    {
        IValidationDelegate delegate = field.getForm().getDelegate();
        
        delegate.recordFieldInputValue(text);
        
        return field.getTranslator().parse(field, text);
    }

    public void renderContributions(TranslatedField field, IMarkupWriter writer, IRequestCycle cycle)
    {
        if (field.getForm().isClientValidationEnabled())
        {
            FormComponentContributorContext context = new FormComponentContributorContextImpl(_threadLocale.getLocale(), cycle, field);

            field.getTranslator().renderContribution(writer, cycle, context, field);
        }
    }
}
