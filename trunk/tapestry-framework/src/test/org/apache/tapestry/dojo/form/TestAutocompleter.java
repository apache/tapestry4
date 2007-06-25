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

package org.apache.tapestry.dojo.form;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.BaseFormComponentTestCase;
import org.apache.tapestry.form.MockDelegate;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential=true)
public class TestAutocompleter extends BaseFormComponentTestCase
{
    private IAutocompleteModel createModel()
    {
        List values = new ArrayList();
        
        SimpleBean s1 = new SimpleBean(new Integer(1), "Simple 1", 100);
        SimpleBean s2 = new SimpleBean(new Integer(2), "Simple 2", 200);
        SimpleBean s3 = new SimpleBean(new Integer(3), "Simple 3", 300);
        
        values.add(s1);
        values.add(s2);
        values.add(s3);
        
        return new DefaultAutocompleteModel(values, "id", "label");
    }
    
    public void test_Rewind()
    {
        IAutocompleteModel model = createModel();
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        DataSqueezer ds = newMock(DataSqueezer.class);
        
        Autocompleter component = newInstance(Autocompleter.class, 
                new Object[] { "model", model, "validatableFieldSupport", vfs,
            "dataSqueezer", ds});
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);
        
        trainGetParameter(cycle, "barney", "1");
        
        expect(ds.unsqueeze("1")).andReturn(new Integer(1));
        
        SimpleBean match = new SimpleBean(new Integer(1), null, -1);
        
        try
        {
            vfs.validate(component, writer, cycle, match);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
        
        assertEquals(match, component.getValue());
    }
    
    public void test_Rewind_NotForm()
    {
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
    }
    
    @DataProvider(name="renderings")
    public Object[][] createRenderings() {
        return new Object[][] {
            {"<span class=\"prefix\"><select name=\"fred\" autocomplete=\"off\" id=\"fred\" class=\"validation-delegate\"> </select></span>",
                     Boolean.FALSE},
            {"<span class=\"prefix\"><select name=\"fred\" autocomplete=\"off\" id=\"fred\" class=\"validation-delegate\"> <option " +
                     "value=\"1p\">Simple 1</option><option value=\"2p\" selected=\"selected\">Simple 2</option><option value=\"3p\">Simple 3</option></select></span>",
                     Boolean.TRUE}
        };
    }    
    
    @Test(dataProvider = "renderings")
    public void test_Render(String outcome, Boolean local)
    {           
        IAutocompleteModel model = createModel();
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        DataSqueezer ds = newMock(DataSqueezer.class);
        ResponseBuilder resp = newMock(ResponseBuilder.class);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        checkOrder(form, false);
        
        MockDelegate delegate = new MockDelegate();
        
        IEngineService engine = newEngineService();
        ILink link = newLink();
        
        IScript script = newMock(IScript.class);
        
        SimpleBean match = new SimpleBean(new Integer(2), "Simple 2", 200);
        
        Autocompleter component = newInstance(Autocompleter.class, 
                new Object[] { 
            "name", "fred", "model", model, 
            "directService", engine,
            "script", script,
            "validatableFieldSupport", vfs, 
            "value", match,
            "dataSqueezer", ds, "local", local
        });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        expect(form.getName()).andReturn("testform").anyTimes();
        
        form.setFormFieldUpdating(true);
        
        IMarkupWriter writer = newBufferWriter();
        
        DirectServiceParameter dsp = 
            new DirectServiceParameter(component);
        
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
        if (!local.booleanValue()) 
        {
            trainGetLinkCheckIgnoreParameter(engine, cycle, true, dsp, link);        
            trainGetURL(link, "urlstring");
        }
        else 
        {
            expect(ds.squeeze(1)).andReturn("1p");
            expect(ds.squeeze(2)).andReturn("2p");
            expect(ds.squeeze(3)).andReturn("3p");
        }
        // for the default value
        expect(ds.squeeze(2)).andReturn("2p"); 
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        script.execute(eq(component), eq(cycle), eq(prs), isA(Map.class));
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
        
        assertBuffer(outcome);
    }
    
    public void test_Render_JSON()
    {
        IAutocompleteModel model = createModel();
        IRequestCycle cycle = newMock(IRequestCycle.class);
        DataSqueezer ds = newMock(DataSqueezer.class);
        checkOrder(ds, false);
        
        IJSONWriter writer = newBufferJSONWriter();
        
        Autocompleter component = newInstance(Autocompleter.class, new Object[]
        { "name", "fred", "model", model,
            "filter", "l", "dataSqueezer", ds });
        
        expect(ds.squeeze(1)).andReturn("1");
        expect(ds.squeeze(2)).andReturn("2");
        expect(ds.squeeze(3)).andReturn("3");
        
        replay();
        
        component.renderComponent(writer, cycle);
        
        verify();
        
        JSONObject json = writer.object();
        
        assertEquals(json.length(), 3);
        assertEquals(json.get("1"), "Simple 1");
        assertEquals(json.get("2"), "Simple 2");
        assertEquals(json.get("3"), "Simple 3");
    }
    
    public void test_Is_Required()
    {
        ValidatableFieldSupport support = newMock(ValidatableFieldSupport.class);
        
        Autocompleter field = newInstance(Autocompleter.class, 
                new Object[] { "validatableFieldSupport", support, });
        
        expect(support.isRequired(field)).andReturn(true);
        
        replay();
        
        assert field.isRequired();

        verify();
    }
}
