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

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.valid.FieldLabel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Tests for the {@link org.apache.tapestry.valid.FieldLabel}&nbsp;component. TODO: Test that
 * proves the raw parameter is honored.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestFieldLabel extends HiveMindTestCase
{
    private Creator _creator = new Creator();

    private static CharArrayWriter _writer;

    private PrintWriter newPrintWriter()
    {
        _writer = new CharArrayWriter();

        return new PrintWriter(_writer);
    }

    private IMarkupWriter newWriter()
    {
        return new MarkupWriterImpl("text/html", newPrintWriter(), new AsciiMarkupFilter());

    }

    protected void tearDown() throws Exception
    {
        _writer = null;

        super.tearDown();
    }

    private void assertOutput(String expected)
    {
        assertEquals(expected, _writer.toString());

        _writer.reset();
    }

    private IBinding newBinding(Location l)
    {
        MockControl control = newControl(IBinding.class);
        IBinding binding = (IBinding) control.getMock();

        binding.getLocation();
        control.setReturnValue(l);

        return binding;
    }

    private IRequestCycle newCycle(IForm form)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(false);

        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        control.setReturnValue(form);

        return cycle;
    }

    private IForm newForm()
    {
        return (IForm) newMock(IForm.class);
    }

    private IForm newForm(IValidationDelegate delegate)
    {
        MockControl control = newControl(IForm.class);
        IForm form = (IForm) control.getMock();

        form.getDelegate();
        control.setReturnValue(delegate);

        return form;
    }

    private IPage newPage()
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

        FieldLabel fl = (FieldLabel) _creator.newInstance(FieldLabel.class);

        fl.render(null, cycle);

        verifyControls();
    }

    public void testNoField()
    {
        IValidationDelegate delegate = new MockDelegate();
        IForm form = newForm(delegate);
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(form);

        replayControls();

        FieldLabel fl = (FieldLabel) _creator.newInstance(FieldLabel.class, new Object[]
        { "displayName", "FredFlintstone" });

        fl.render(writer, cycle);

        assertOutput("{LABEL-PREFIX}FredFlintstone{LABEL-SUFFIX}");

        verifyControls();
    }

    public void testNoFieldOrDisplayName()
    {
        IForm form = newForm();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(form);
        Location l = newLocation();
        IBinding binding = newBinding(l);
        IPage page = newPage();

        replayControls();

        FieldLabel fl = (FieldLabel) _creator.newInstance(FieldLabel.class, new Object[]
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

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(form);
        IFormComponent field = newField("MyLabel");
        Location l = newLocation();

        FieldLabel fl = (FieldLabel) _creator.newInstance(FieldLabel.class, new Object[]
        { "location", l, "field", field });

        form.prerenderField(writer, field, l);

        form.getDelegate();
        formc.setReturnValue(delegate);

        replayControls();

        fl.render(writer, cycle);

        assertOutput("{LABEL-PREFIX}MyLabel{LABEL-SUFFIX}");

        verifyControls();
    }

    public void testNoDisplayNameInField()
    {
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(form);

        MockControl fieldc = newControl(IFormComponent.class);

        IFormComponent field = (IFormComponent) fieldc.getMock();

        Location l = newLocation();
        IPage page = newPage();

        FieldLabel fl = (FieldLabel) _creator.newInstance(FieldLabel.class, new Object[]
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