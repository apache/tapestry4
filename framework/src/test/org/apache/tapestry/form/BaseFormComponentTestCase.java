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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Base class for tests of implementations of {@link org.apache.tapestry.form.IFormComponent}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BaseFormComponentTestCase extends BaseComponentTestCase
{

    protected IValidationDelegate newDelegate()
    {
        return (IValidationDelegate) newMock(IValidationDelegate.class);
    }

    protected void trainIsInError(IValidationDelegate delegate, boolean isInError)
    {
        expect(delegate.isInError()).andReturn(isInError);
    }

    protected IForm newForm()
    {
        return (IForm) newMock(IForm.class);
    }

    protected void trainGetForm(IRequestCycle cycle, IForm form)
    {
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
    }

    protected void trainGetDelegate(IForm form, IValidationDelegate delegate)
    {
        expect(form.getDelegate()).andReturn(delegate);
    }

    protected void trainGetParameter(IRequestCycle cycle, String parameterName,
            String parameterValue)
    {
        expect(cycle.getParameter(parameterName)).andReturn(parameterValue);
    }

    protected void trainWasPrerendered(IForm form, IMarkupWriter writer, IComponent component,
            boolean wasPrerendered)
    {
        expect(form.wasPrerendered(writer, component)).andReturn(wasPrerendered);
    }

    protected void trainIsRewinding(IForm form, boolean isRewinding)
    {
        expect(form.isRewinding()).andReturn(isRewinding);
    }
    
    protected void trainGetElementId(IForm form, IFormComponent component, String name)
    {
        form.getElementId(component);
        component.setName(name);
        expectLastCall().andReturn(name);
    }

    protected IBinding newBinding()
    {
        return (IBinding) newMock(IBinding.class);
    }

    protected IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
    }

    protected IFormComponent newField()
    {
        return (IFormComponent) newMock(IFormComponent.class);
    }
}
