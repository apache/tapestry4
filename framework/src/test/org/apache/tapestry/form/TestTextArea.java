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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.TextArea}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestTextArea extends BaseFormComponentTest
{
    public void testWasPrerendered()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRewindNotForm()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainGetElementId(formc, form, component, "barney");
        trainIsRewinding(cyclec, cycle, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRewindingForm()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, true);
        trainGetElementId(formc, form, component, "barney");
        train(cyclec, cycle, "barney", "submitted value");

        replayControls();

        component.render(writer, cycle);

        assertEquals("submitted value", component.getProperty("value"));

        verifyControls();
    }

    public void testRewindFormDisabled()
    {
        TextArea component = (TextArea) newInstance(TextArea.class, "disabled", Boolean.TRUE);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, true);
        trainGetElementId(formc, form, component, "barney");

        replayControls();

        component.render(writer, cycle);

        assertNull(component.getProperty("value"));

        verifyControls();
    }

    public void testRender()
    {
        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "value", "text area value" });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(cyclec, cycle, false);
        trainGetDelegate(formc, form, delegate);

        delegate.writePrefix(writer, cycle, component, null);

        writer.begin("textarea");
        writer.attribute("name", "fred");

        delegate.writeAttributes(writer, cycle, component, null);

        writer.print("text area value");

        writer.end();

        delegate.writeSuffix(writer, cycle, component, null);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRenderDisabled()
    {
        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "value", "text area value", "disabled", Boolean.TRUE });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(cyclec, cycle, false);
        trainGetDelegate(formc, form, delegate);

        delegate.writePrefix(writer, cycle, component, null);

        writer.begin("textarea");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");

        delegate.writeAttributes(writer, cycle, component, null);

        writer.print("text area value");

        writer.end();

        delegate.writeSuffix(writer, cycle, component, null);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRenderWithInformalParameters()
    {
        IBinding binding = newBinding("informal-value");

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "value", "text area value", "specification", new ComponentSpecification() });

        component.setBinding("informal", binding);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(cyclec, cycle, false);
        trainGetDelegate(formc, form, delegate);

        delegate.writePrefix(writer, cycle, component, null);

        writer.begin("textarea");
        writer.attribute("name", "fred");
        writer.attribute("informal", "informal-value");

        delegate.writeAttributes(writer, cycle, component, null);

        writer.print("text area value");

        writer.end();

        delegate.writeSuffix(writer, cycle, component, null);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRenderNullValue()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(cyclec, cycle, false);
        trainGetDelegate(formc, form, delegate);

        delegate.writePrefix(writer, cycle, component, null);

        writer.begin("textarea");
        writer.attribute("name", "fred");

        delegate.writeAttributes(writer, cycle, component, null);

        writer.end();

        delegate.writeSuffix(writer, cycle, component, null);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }
}
