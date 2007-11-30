// Copyright Oct 7, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import org.apache.tapestry.*;
import org.apache.tapestry.form.BaseFormComponentTestCase;
import org.apache.tapestry.form.MockDelegate;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.ResponseBuilder;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Locale;
import java.util.Map;


/**
 * Tests functionality of {@link DropdownTimePicker} component.
 *
 * @author jkuhnert
 */
@Test
public class TestDropdownTimePicker extends BaseFormComponentTestCase
{

    public void test_Render()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        DateTranslator translator = new DateTranslator();
        translator.setPattern("hh:mm a");
        ResponseBuilder resp = newMock(ResponseBuilder.class);

        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        checkOrder(form, false);
        IPage page = newPage();
        Locale locale = Locale.getDefault();

        MockDelegate delegate = new MockDelegate();

        IScript script = newMock(IScript.class);

        Date dtValue = new Date();

        DropdownTimePicker component = newInstance(DropdownTimePicker.class,
                                                   "name", "fred",
                                                   "script", script,
                                                   "validatableFieldSupport", vfs,
                                                   "translator", translator,
                                                   "value", dtValue,
                                                   "page", page,
                                                   "templateTagName", "div");

        expect(cycle.renderStackPush(component)).andReturn(component);
        expect(form.getName()).andReturn("testform").anyTimes();

        form.setFormFieldUpdating(true);

        IMarkupWriter writer = newBufferWriter();

        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);

        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        expect(cycle.isRewinding()).andReturn(false).anyTimes();

        delegate.setFormComponent(component);

        expect(cycle.getResponseBuilder()).andReturn(resp).anyTimes();
        expect(resp.isDynamic()).andReturn(false).anyTimes();

        vfs.renderContributions(component, writer, cycle);

        expect(page.getLocale()).andReturn(locale).anyTimes();

        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);

        script.execute(eq(component), eq(cycle), eq(prs), isA(Map.class));

        expect(cycle.renderStackPop()).andReturn(component);

        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><div id=\"fred\" class=\"validation-delegate\"> </div></span>");
    }

    public void test_Json_Time()
    {
        long time = System.currentTimeMillis();

        JSONObject json = new JSONObject();
        json.put("time", time);

        assertEquals(json.toString(), "{\"time\":"+time+"}");
    }
}
