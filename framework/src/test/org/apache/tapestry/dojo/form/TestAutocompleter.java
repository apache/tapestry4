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

import java.util.HashMap;

import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.IgnoreMatcher;
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
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAutocompleter extends BaseFormComponentTestCase
{
    public void testRewind()
    {
        String[] values = { "red", "green", "blue" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        
        MockControl vfsc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) vfsc.getMock();
        
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class, new Object[]
        { "model", model, "validatableFieldSupport", vfs });
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
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
        
        trainGetParameter(cycle, "barney_selected", key);
        
        try
        {
            vfs.validate(component, writer, cycle, value);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        replayControls();
        
        component.render(writer, cycle);
        
        verifyControls();
        
        assertEquals(values[0], component.getValue());
    }
    
    public void testRewindNotForm()
    {
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class);
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);
        
        replayControls();
        
        component.render(writer, cycle);
        
        verifyControls();
    }
    
    public void testRender()
    {
        String[] values = { "red", "green", "blue" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();
        
        MockDelegate delegate = new MockDelegate();
        
        IEngineService engine = newEngineService();
        ILink link = newLink();
        
        IScript script = (IScript)newMock(IScript.class);
        
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class, new Object[]
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
        trainGetDelegate(form, delegate);
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, true, dsp, link);
        
        trainGetURL(link, "urlstring");
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        script.execute(cycle, prs, new HashMap());
        
        ArgumentMatcher ignore = new IgnoreMatcher();
        getControl(script).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, null, ignore }));
        
        replayControls();
        
        component.render(writer, cycle);
        
        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><select name=\"fred\" class=\"validation-delegate\"></select></span>");
    }
    
    public void testRenderJSON()
    {
        String[] values = { "red", "green", "blue", "yellow" };
        StringPropertySelectionModel model = new StringPropertySelectionModel(values);
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        
        IJSONWriter json = newBufferJSONWriter();
        
        Autocompleter component = (Autocompleter) newInstance(Autocompleter.class, new Object[]
        { "name", "fred", "model", model,
            "filter", "l" });
        
        replayControls();
        
        component.renderComponent(json, cycle);
        
        verifyControls();
        
        assertEquals(json.length(), 2);
        assertEquals(json.get("3"), "yellow");
        assertEquals(json.get("2"), "blue");
    }
    
    public void testIsRequired()
    {
        MockControl supportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport support = (ValidatableFieldSupport) supportc.getMock();
        
        Autocompleter field = (Autocompleter) newInstance(Autocompleter.class, new Object[]
        { "validatableFieldSupport", support, });
        
        support.isRequired(field);
        supportc.setReturnValue(true);
        
        replayControls();
        
        assertEquals(true, field.isRequired());

        verifyControls();
    }
}
