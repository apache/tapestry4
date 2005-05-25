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

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestTextField extends BaseFormComponentTest
{
    public void testWasPrerendered()
    {
        TextField component = (TextField) newInstance(TextField.class);

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

    /**
     * Check when the form is not rewinding, but the cycle is rewinding (a whole page rewind care of
     * the action service).
     */

    public void testFormNotRewinding()
    {
        TextField component = (TextField) newInstance(TextField.class);

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
        trainIsRewinding(cyclec, cycle, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRewind()
    {
        TextField component = (TextField) newInstance(TextField.class);

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

        trainGetElementId(formc, form, component, "fred");

        trainGetParameter(cyclec, cycle, "fred", "fred-value");

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertEquals("fred-value", component.getProperty("value"));
    }

    public void testRender()
    {
        TextField component = (TextField) newInstance(TextField.class, "value", "field value");

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetElementId(formc, form, component, "fred");

        trainGetDelegate(formc, form, delegate);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"field value\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderHidden()
    {
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "value", "field value", "hidden", Boolean.TRUE });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetElementId(formc, form, component, "fred");

        trainGetDelegate(formc, form, delegate);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"password\" name=\"fred\" value=\"field value\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderDisabled()
    {
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "value", "field value", "disabled", Boolean.TRUE });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetElementId(formc, form, component, "fred");

        trainGetDelegate(formc, form, delegate);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" disabled=\"disabled\" name=\"fred\" value=\"field value\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderNull()
    {
        TextField component = (TextField) newInstance(TextField.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainGetDelegate(formc, form, delegate);

        trainWasPrerendered(formc, form, writer, component, false);
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetElementId(formc, form, component, "fred");

        trainGetDelegate(formc, form, delegate);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" class=\"validation-delegate\"/></span>");
    }
}
