package org.apache.tapestry.dojo.form;

import org.apache.tapestry.*;
import org.apache.tapestry.form.BaseFormComponentTestCase;
import org.apache.tapestry.form.MockDelegate;
import org.apache.tapestry.form.TranslatedFieldSupport;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.services.ResponseBuilder;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Tests functionality of {@link GTimePicker} component.
 */
@Test
public class TestGTimePicker extends BaseFormComponentTestCase
{

    public void test_Render()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);

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

        GTimePicker component = newInstance(GTimePicker.class,
                                            "name", "fred",
                                            "script", script,
                                            "validatableFieldSupport", vfs,
                                            "translatedFieldSupport", tfs,
                                            "translator", translator,
                                            "value", dtValue,
                                            "page", page);

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

        expect(tfs.format(component, dtValue)).andReturn(dtValue.toString());

        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);

        expect(page.getLocale()).andReturn(locale).anyTimes();

        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);

        script.execute(eq(component), eq(cycle), eq(prs), isA(Map.class));

        expect(cycle.renderStackPop()).andReturn(component);

        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" "
                     + "value=\"" + dtValue.toString() + "\" id=\"fred\" class=\"validation-delegate\" /></span>");
    }
}
