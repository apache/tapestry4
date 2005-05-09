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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Base class for tests of implementations of {@link org.apache.tapestry.form.IFormComponent}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public abstract class BaseFormComponentTest extends HiveMindTestCase
{

    protected IValidationDelegate newDelegate()
    {
        return (IValidationDelegate) newMock(IValidationDelegate.class);
    }

    protected IForm newForm()
    {
        return (IForm) newMock(IForm.class);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    protected void train(MockControl control, IRequestCycle cycle, IForm form)
    {
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        control.setReturnValue(form);
    }

    protected void train(MockControl control, IRequestCycle cycle, String parameterName,
            String parameterValue)
    {
        cycle.getParameter(parameterName);
        control.setReturnValue(parameterValue);
    }

    protected void trainWasPrerendered(MockControl control, IForm form, IMarkupWriter writer,
            Submit submit, boolean wasPrerendered)
    {
        form.wasPrerendered(writer, submit);
        control.setReturnValue(wasPrerendered);
    }

    protected void trainIsRewinding(MockControl control, IForm form, boolean isRewinding)
    {
        form.isRewinding();
        control.setReturnValue(isRewinding);
    }

    protected void trainGetElementId(MockControl control, IForm form, IFormComponent component,
            String name)
    {
        form.getElementId(component);
        control.setReturnValue(name);
    }

    protected IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    protected IBinding newBinding()
    {
        return (IBinding) newMock(IBinding.class);
    }

    protected IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
    }
}
