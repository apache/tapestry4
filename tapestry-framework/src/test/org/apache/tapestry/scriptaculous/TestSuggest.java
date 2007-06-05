package org.apache.tapestry.scriptaculous;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.*;
import static org.apache.tapestry.Capturer.capture;
import static org.apache.tapestry.Capturer.newCapturer;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.MockForm;
import org.apache.tapestry.form.TranslatedFieldSupport;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationDelegate;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tests core functionality of {@link Suggest} component.
 */
@Test
public class TestSuggest extends BaseComponentTestCase {

    public void test_Render_Component()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        checkOrder(cycle, false);
        
        ResponseBuilder resp = newMock(ResponseBuilder.class);
        IEngineService engine = newMock(IEngineService.class);
        ILink link = newMock(ILink.class);
        IScript script = newMock(IScript.class);
        PageRenderSupport prs = newMock(PageRenderSupport.class);

        TranslatedFieldSupport translator = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport validator = newMock(ValidatableFieldSupport.class);

        IValidationDelegate delegate = new ValidationDelegate();
        IForm form = newMock(IForm.class);
        IMarkupWriter writer = newBufferWriter();

        Suggest comp = newInstance(Suggest.class,
                                   "response", resp,
                                   "templateTagName", "input",
                                   "name", "suggest",
                                   "clientId", "suggest",
                                   "form", form,
                                   "translatedFieldSupport", translator,
                                   "validatableFieldSupport", validator,
                                   "engineService", engine,
                                   "script", script);

        expect(cycle.isRewinding()).andReturn(false).anyTimes();
        expect(resp.isDynamic()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form).anyTimes();

        expect(form.wasPrerendered(writer, comp)).andReturn(false).anyTimes();
        expect(form.getDelegate()).andReturn(delegate).anyTimes();
        expect(form.getElementId(comp)).andReturn("suggest");
        expect(form.isRewinding()).andReturn(false).anyTimes();

        form.setFormFieldUpdating(true);
        
        expect(translator.format(comp, null)).andReturn("r2d2");
        translator.renderContributions(comp, writer, cycle);
        validator.renderContributions(comp, writer, cycle);

        expect(engine.getLink(eq(false), isA(DirectServiceParameter.class))).andReturn(link);
        expect(link.getURL()).andReturn("http://url");

        Capturer<Map> parm = newCapturer(Map.class);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);
        script.execute(eq(comp), eq(cycle), eq(prs), capture(parm));

        replay();

        comp.renderComponent(writer, cycle);

        verify();

        assert parm.getCaptured() != null;
        Map parms = parm.getCaptured();

        assertEquals(parms.size(), 4);
        assertEquals(parms.get("inputId"), "suggest");
        assertEquals(parms.get("updateId"), "suggestchoices");
        assertEquals(parms.get("options"), "{\"onFailure\":\"tapestry.error\"}");
        assertEquals(parms.get("updateUrl"), "http://url");

        assertBuffer("<input type=\"text\" autocomplete=\"off\" " +
                     "id=\"suggest\" name=\"suggest\" value=\"r2d2\" />" +
                     "<div id=\"suggestchoices\" class=\"\"></div>");
    }

    public void test_Render_TextArea_Component()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        checkOrder(cycle, false);

        ResponseBuilder resp = newMock(ResponseBuilder.class);
        IEngineService engine = newMock(IEngineService.class);
        ILink link = newMock(ILink.class);
        IScript script = newMock(IScript.class);
        PageRenderSupport prs = newMock(PageRenderSupport.class);

        TranslatedFieldSupport translator = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport validator = newMock(ValidatableFieldSupport.class);

        IValidationDelegate delegate = new ValidationDelegate();
        IForm form = newMock(IForm.class);
        IMarkupWriter writer = newBufferWriter();

        Suggest comp = newInstance(Suggest.class,
                                   "response", resp,
                                   "templateTagName", "textarea",
                                   "name", "suggest",
                                   "clientId", "suggest",
                                   "form", form,
                                   "translatedFieldSupport", translator,
                                   "validatableFieldSupport", validator,
                                   "engineService", engine,
                                   "script", script);

        expect(cycle.isRewinding()).andReturn(false).anyTimes();
        expect(resp.isDynamic()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form).anyTimes();

        expect(form.wasPrerendered(writer, comp)).andReturn(false).anyTimes();
        expect(form.getDelegate()).andReturn(delegate).anyTimes();
        expect(form.getElementId(comp)).andReturn("suggest");
        expect(form.isRewinding()).andReturn(false).anyTimes();

        form.setFormFieldUpdating(true);

        expect(translator.format(comp, null)).andReturn("r2d2");
        translator.renderContributions(comp, writer, cycle);
        validator.renderContributions(comp, writer, cycle);

        expect(engine.getLink(eq(false), isA(DirectServiceParameter.class))).andReturn(link);
        expect(link.getURL()).andReturn("http://url");

        Capturer<Map> parm = newCapturer(Map.class);

        trainGetAttribute(cycle, TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE, prs);
        script.execute(eq(comp), eq(cycle), eq(prs), capture(parm));

        replay();

        comp.renderComponent(writer, cycle);

        verify();

        assert parm.getCaptured() != null;
        Map parms = parm.getCaptured();

        assertEquals(parms.size(), 4);
        assertEquals(parms.get("inputId"), "suggest");
        assertEquals(parms.get("updateId"), "suggestchoices");
        assertEquals(parms.get("options"), "{\"onFailure\":\"tapestry.error\"}");
        assertEquals(parms.get("updateUrl"), "http://url");

        assertBuffer("<textarea id=\"suggest\" name=\"suggest\">r2d2</textarea>" +
                     "<div id=\"suggestchoices\" class=\"\"></div>");
    }

    public void test_Render_Invalid_Options_JSON_Syntax()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        checkOrder(cycle, false);
        ResponseBuilder resp = newMock(ResponseBuilder.class);

        TranslatedFieldSupport translator = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport validator = newMock(ValidatableFieldSupport.class);

        IValidationDelegate delegate = new ValidationDelegate();
        IForm form = new MockForm(delegate);
        IMarkupWriter writer = newBufferWriter();

        IBinding binding = newMock(IBinding.class);
        Location l = newLocation();
        ComponentSpecification spec = new ComponentSpecification();
        
        Suggest comp = newInstance(Suggest.class,
                                   "response", resp,
                                   "templateTagName", "input",
                                   "name", "suggest",
                                   "clientId", "suggest",
                                   "form", form,
                                   "translatedFieldSupport", translator,
                                   "validatableFieldSupport", validator,
                                   "options", "{thisisinvalid:true",
                                   "specification", spec);

        expect(cycle.isRewinding()).andReturn(false).anyTimes();
        expect(resp.isDynamic()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form).anyTimes();

        expect(translator.format(comp, null)).andReturn("r2d2");
        translator.renderContributions(comp, writer, cycle);
        validator.renderContributions(comp, writer, cycle);

        expect(binding.getObject()).andReturn("{thisisinvalid:true");
        expect(binding.getLocation()).andReturn(l);

        replay();

        try {

            comp.setBinding("options", binding);

            comp.renderComponent(writer, cycle);

            unreachable();
        } catch (ApplicationRuntimeException ex) {

            assertEquals(ex.getMessage(), "Invalid JSON options string given: {thisisinvalid:true. " +
                                          "The options parameter must be properly formatted according to JSON object syntax rules. " +
                                          "Expected a ',' or '}' at character 19 of {thisisinvalid:true>>missing value<<");
        }

        verify();
    }

    public void test_Render_List()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        checkOrder(cycle, false);
        ResponseBuilder resp = newMock(ResponseBuilder.class);

        IValidationDelegate delegate = new ValidationDelegate();
        IForm form = newMock(IForm.class);
        IMarkupWriter writer = newBufferWriter();

        ValueConverter converter = newMock(ValueConverter.class);

        List<String> source = new ArrayList<String>();
        source.add("Foo");
        source.add("Stimpy");
        source.add("Cat");

        Suggest comp = newInstance(Suggest.class,
                                   "response", resp,
                                   "templateTagName", "input",
                                   "name", "suggest",
                                   "clientId", "suggest",
                                   "form", form,
                                   "searchTriggered", true,
                                   "listSource", source,
                                   "valueConverter", converter,
                                   "listItemRenderer", DefaultListItemRenderer.SHARED_INSTANCE);

        expect(form.isRewinding()).andReturn(false);
        expect(cycle.isRewinding()).andReturn(false).anyTimes();
        expect(resp.isDynamic()).andReturn(true);
        trainGetAttribute(cycle, TapestryUtils.FORM_ATTRIBUTE, form);
        expect(form.wasPrerendered(writer, comp)).andReturn(false);

        expect(form.getElementId(comp)).andReturn("suggest");
        expect(cycle.getParameter("suggest")).andReturn("b");
        expect(converter.coerceValue(source, Iterator.class)).andReturn(source.iterator());

        replay();

        comp.renderComponent(writer, cycle);

        verify();

        assertBuffer("<ul><li>Foo</li><li>Stimpy</li><li>Cat</li></ul>");
    }
}
