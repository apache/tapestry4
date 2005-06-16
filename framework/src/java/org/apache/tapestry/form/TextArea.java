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
 * Implements a component that manages an HTML &lt;textarea&gt; form element.
 *
 * [<a href="../../../../../ComponentReference/TextArea.html">Component Reference</a>]
 *
 * As of 4.0, TextField can indicate that it is required, use a custom translator, 
 * and perform validation on the submitted text.
 *
 * @author Howard Lewis Ship
 * @author Paul Ferraro
 */
public abstract class TextArea extends AbstractValidatableField
{
    public abstract String getValue();
    
    public abstract void setValue(String value);
    
    /**
     * @see org.apache.tapestry.form.validator.AbstractValidatableField#render(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle, java.lang.String)
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle, String value)
    {
        renderDelegatePrefix(writer, cycle);
        
        writer.begin("textarea");

        writer.attribute("name", getName());

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        renderDelegateAttributes(writer, cycle);
        
        renderContributions(writer, cycle);
        
        renderInformalParameters(writer, cycle);

        if (value != null)
            writer.print(value);

        writer.end();
        
        renderDelegateSuffix(writer, cycle);
    }
    
    /**
     * @see org.apache.tapestry.form.ValidatableField#writeValue(java.lang.Object)
     */
    public void writeValue(Object value)
    {
        setValue((String) value);
    }
    
    /**
     * @see org.apache.tapestry.form.ValidatableField#readValue()
     */
    public Object readValue()
    {
        return getValue();
    }
}