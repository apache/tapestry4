// Copyright 2009 The Apache Software Foundation
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
package org.apache.tapestry.contrib.form;

import org.apache.tapestry.*;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.valid.ValidationDelegate;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test {@link MultiplePropertySelection}.
 *
 * @author Andreas Andreou
 */
public class TestMultiplePropertySelection extends BaseComponentTestCase
{
    private static final String LINE_SEPARATOR = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));

    @Test
    public void testRender()
    {
        List<String> selected = new ArrayList<String>();
        selected.add("two");

        IPropertySelectionModel model = new StringPropertySelectionModel(
                new String[] {"one", "two", "three"}, new boolean[]{false, false, true});

        final MultiplePropertySelection component = newInstance(MultiplePropertySelection.class,
                "renderer", MultiplePropertySelection.DEFAULT_CHECKBOX_RENDERER,
                "selectedList", selected, "model", model);

        final IRequestCycle cycle = newCycle();
        final IMarkupWriter writer = newBufferWriter();
        final IForm form = newForm();

        expect(cycle.isRewinding()).andReturn(false).anyTimes();

        expect(cycle.renderStackPush(isA(IRender.class))).andReturn(null);
        expect(cycle.renderStackPop()).andReturn(null);
        trainGetAttribute(cycle, TapestryUtils.FORM_ATTRIBUTE, form);
        expect(form.wasPrerendered(writer, component)).andReturn(false);
        expect(form.getDelegate()).andReturn(new ValidationDelegate());
        expect(form.getElementId(component)).andReturn("selector");
        expect(form.isRewinding()).andReturn(false).anyTimes();
        form.setFormFieldUpdating(true);

        replay();

        component.render(writer, cycle);

        assertBuffer("<table border=\"0\" cellpadding=\"0\" cellspacing=\"2\"><tr><td><input type=\"checkbox\" name=\"selector\" id=\"selector.0\" value=\"0\" /></td>" +
                LINE_SEPARATOR +
                "<td><label for=\"selector.0\">one</label></td></tr>" +
                LINE_SEPARATOR +
                "<tr><td><input type=\"checkbox\" name=\"selector\" id=\"selector.1\" value=\"1\" checked=\"checked\" /></td>" +
                LINE_SEPARATOR +
                "<td><label for=\"selector.1\">two</label></td></tr>" +
                LINE_SEPARATOR +
                "<tr><td><input type=\"checkbox\" name=\"selector\" id=\"selector.2\" value=\"2\" disabled=\"disabled\" /></td>" +
                LINE_SEPARATOR +
                "<td><label for=\"selector.2\">three</label></td></tr>" +
                LINE_SEPARATOR +
                "</table>");

        verify();
    }
}
