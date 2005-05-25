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

package org.apache.tapestry.valid;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.BaseFormComponentTest;
import org.apache.tapestry.form.IFormComponent;
import org.easymock.MockControl;

/**
 * Tests for the {@link org.apache.tapestry.valid.FieldLabel} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestFieldLabel extends BaseFormComponentTest
{
    private IForm newForm(IValidationDelegate delegate)
    {
        MockControl control = newControl(IForm.class);
        IForm form = (IForm) control.getMock();

        form.getDelegate();
        control.setReturnValue(delegate);

        return form;
    }

    private IPage newFred()
    {
        MockControl control = newControl(IPage.class);
        IPage page = (IPage) control.getMock();

        page.getPageName();
        control.setReturnValue("Fred");

        page.getIdPath();
        control.setReturnValue(null);

        return page;
    }

    private IFormComponent newField(String displayName)
    {
        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getDisplayName();
        control.setReturnValue(displayName);

        return field;
    }

    public void testRewinding()
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(true);

        replayControls();

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class);

        fl.render(null, cycle);

        verifyControls();
    }

    public void testNoField()
    {
        IValidationDelegate delegate = new MockDelegate();
        IForm form = newForm(delegate);
        IMarkupWriter writer = newBufferWriter();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        trainIsRewinding(cyclec, cycle, false);
        trainGetForm(cyclec, cycle, form);

        replayControls();

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class, new Object[]
        { "displayName", "FredFlintstone" });

        fl.render(writer, cycle);

        assertBuffer("{LABEL-PREFIX}FredFlintstone{LABEL-SUFFIX}");

        verifyControls();
    }

    public void testNoFieldRaw()
    {
        IValidationDelegate delegate = new MockDelegate();
        IForm form = newForm(delegate);
        IMarkupWriter writer = newBufferWriter();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        trainIsRewinding(cyclec, cycle, false);
        trainGetForm(cyclec, cycle, form);

        replayControls();

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class, new Object[]
        { "displayName", "<b>FredFlintstone</b>", "raw", Boolean.TRUE });

        fl.render(writer, cycle);

        assertBuffer("{LABEL-PREFIX}<b>FredFlintstone</b>{LABEL-SUFFIX}");

        verifyControls();
    }

    public void testNoFieldOrDisplayName()
    {
        IForm form = newForm();
        IMarkupWriter writer = newBufferWriter();
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        Location l = newLocation();
        IBinding binding = newBinding(l);
        IPage page = newFred();

        trainIsRewinding(cyclec, cycle, false);
        trainGetForm(cyclec, cycle, form);

        replayControls();

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class, new Object[]
        { "id", "label", "page", page, "container", page });

        fl.setBinding("field", binding);

        try
        {
            fl.render(writer, cycle);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Value for parameter 'field' in component Fred/label is null, and a non-null value is required.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(binding, ex.getBinding());
        }

        verifyControls();
    }

    public void testDisplayNameFromField()
    {
        IValidationDelegate delegate = new MockDelegate();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();
        IFormComponent field = newField("MyLabel");
        Location l = newLocation();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        trainIsRewinding(cyclec, cycle, false);
        trainGetForm(cyclec, cycle, form);

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class, new Object[]
        { "location", l, "field", field });

        form.prerenderField(writer, field, l);

        form.getDelegate();
        formc.setReturnValue(delegate);

        replayControls();

        fl.render(writer, cycle);

        assertBuffer("{LABEL-PREFIX}MyLabel{LABEL-SUFFIX}");

        verifyControls();
    }

    public void testNoDisplayNameInField()
    {
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockControl fieldc = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) fieldc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        trainIsRewinding(cyclec, cycle, false);
        trainGetForm(cyclec, cycle, form);

        Location l = newLocation();
        IPage page = newFred();

        FieldLabel fl = (FieldLabel) newInstance(FieldLabel.class, new Object[]
        { "id", "label", "location", l, "field", field, "page", page, "container", page });

        form.prerenderField(writer, field, l);

        field.getDisplayName();
        fieldc.setReturnValue(null);

        field.getExtendedId();
        fieldc.setReturnValue("Fred/field");

        replayControls();

        try
        {
            fl.render(writer, cycle);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Display name for Fred/label was not specified and was not provided by field Fred/field.",
                    ex.getMessage());
        }

        verifyControls();
    }
}