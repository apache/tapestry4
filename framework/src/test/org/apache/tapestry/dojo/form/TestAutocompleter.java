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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.testng.AssertJUnit.assertEquals;

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
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAutocompleter extends BaseFormComponentTestCase
{
    public void testRewind()
    {
        String[] values = { "red", "green", "blue" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Autocompleter component = newInstance(Autocompleter.class, new Object[]
        { "model", model, "validatableFieldSupport", vfs });
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);
        
        String key = "0";
        String value = values[0];
        
        trainGetParameter(cycle, "barney", key);
        
        try
        {
            vfs.validate(component, writer, cycle, value);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
        
        assertEquals(values[0], component.getValue());
    }
    
    public void testRewindNotForm()
    {
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
    }
    
    public void testRender()
    {
        String[] values = { "red", "green", "blue" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newBufferWriter();
        
        MockDelegate delegate = new MockDelegate();
        
        IEngineService engine = newEngineService();
        ILink link = newLink();
        
        IScript script = newMock(IScript.class);
        
        Autocompleter component = newInstance(Autocompleter.class, new Object[]
        { "name", "fred", "model", model, 
            "directService", engine,
            "script", script,
            "validatableFieldSupport", vfs, 
            "value", values[1] });
        
        DirectServiceParameter dsp = 
            new DirectServiceParameter(component, new Object[]{}, 
                    new String[]{"fred"}, true);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);
        
        delegate.setFormComponent(component);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, true, dsp, link);
        
        trainGetURL(link, "urlstring");
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        script.execute(eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        component.render(writer, cycle);
        
        verify();
        
        assertBuffer("<span class=\"prefix\"><select name=\"fred\" class=\"validation-delegate\"></select></span>");
    }
    
    public void testRenderJSON()
    {
        String[] values = { "red", "green", "blue", "yellow" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        
        IRequestCycle cycle = newMock(IRequestCycle.class);
        
        IJSONWriter json = newBufferJSONWriter();
        
        Autocompleter component = newInstance(Autocompleter.class, new Object[]
        { "name", "fred", "model", model,
            "filter", "l" });
        
        replay();
        
        component.renderComponent(json, cycle);
        
        verify();
        
        assertEquals(json.length(), 2);
        assertEquals(json.get("3"), "yellow");
        assertEquals(json.get("2"), "blue");
    }
    
    public void testIsRequired()
    {
        ValidatableFieldSupport support = newMock(ValidatableFieldSupport.class);
        
        Autocompleter field = newInstance(Autocompleter.class, new Object[]
        { "validatableFieldSupport", support, });
        
        expect(support.isRequired(field)).andReturn(true);
        
        replay();
        
        assertEquals(true, field.isRequired());

        verify();
    }
}
