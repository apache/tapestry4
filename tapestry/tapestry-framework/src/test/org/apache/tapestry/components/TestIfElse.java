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

package org.apache.tapestry.components;

import org.apache.tapestry.*;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.io.DataSqueezerUtil;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.components.IfBean}&nbsp;
 * and {@link ElseBean}&nbsp;
 * components.
 * 
 * @author Mindbridge
 * @since 4.0
 */
@Test
public class TestIfElse extends BaseComponentTestCase
{
    private IRender newRender(IMarkupWriter writer, IRequestCycle cycle)
    {
        IRender render = newMock(IRender.class);
        
        render.render(writer, cycle);
        
        return render;
    }

    public void test_Render_Plain_True()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);

        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(IfBean.class, 
                new Object[] { "condition", Boolean.TRUE });
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender(writer, cycle);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);
        expect(cycle.renderStackPop()).andReturn(reverse);
        
        IRender body2 = newRender();
        
        replay();
        
        conditional.addBody(body);
        
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void test_Render_Plain_False()
    {
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(IfBean.class, 
                new Object[] { "condition", Boolean.FALSE });
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        expect(cycle.isRewinding()).andReturn(false);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(reverse);
        
        replay();
        
        conditional.addBody(body);
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void test_Render_In_Form_True()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE });
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.getElementId(conditional)).andReturn("If");
        
        form.addHiddenValue("If", "T");
        
        form.setFormFieldUpdating(true);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender(writer, cycle);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);
        
        IRender body2 = newRender();
        
        expect(cycle.renderStackPop()).andReturn(reverse);
        
        replay();
        
        conditional.addBody(body);
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void test_Render_In_Form_False()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.FALSE });

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);

        expect(form.getElementId(conditional)).andReturn("If");
        
        form.addHiddenValue("If", "F");
        
        form.setFormFieldUpdating(true);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        expect(cycle.renderStackPop()).andReturn(reverse);
        
        replay();

        conditional.addBody(body);
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void test_Ignore_Element_When_Rewinding_True()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(true);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.isRewinding()).andReturn(true);
        
        expect(form.getElementId(conditional)).andReturn("If");
        
        expect(cycle.getParameter("If")).andReturn("T");
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender(writer, cycle);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);
        
        IRender body2 = newRender();

        expect(cycle.renderStackPop()).andReturn(reverse);
        
        replay();

        conditional.addBody(body);
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verify();
    }

    public void test_Ignore_Element_When_Rewinding_False()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        
        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(true);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.isRewinding()).andReturn(true);
        
        expect(form.getElementId(conditional)).andReturn("If");
        
        expect(cycle.getParameter("If")).andReturn("F");
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        expect(cycle.renderStackPush(reverse)).andReturn(reverse);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        
        expect(cycle.isRewinding()).andReturn(true);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(reverse);
        
        replay();

        conditional.addBody(body);
        conditional.render(writer, cycle);
        
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verify();
    }

    public void test_Render_Tag()
    {
        IMarkupWriter writer = newWriter();
        
		IBinding informal = newBinding("informal-value");
        IComponentSpecification spec = newSpec("informal", null);
        
        IRequestCycle cycle = newCycle();

        IfBean conditional = newInstance(TestIfBean.class,
                                         "condition", Boolean.TRUE,
                                         "element", "div",
                                         "specification", spec,
                                         "renderTag", Boolean.TRUE,
                                         "clientId", "testIf");
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        IRender body = newRender(writer, cycle);

        writer.begin("div");
        writer.attribute("id", "testIf");
        writer.attribute("informal", "informal-value");

        writer.end("div");
        
        trainResponseBuilder(cycle, writer);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        replay();
        
        conditional.addBody(body);
        conditional.setBinding("informal", informal);
        
        conditional.render(writer, cycle);
        
        verify();
    }
    
    public void test_Render_Tag_False()
    {
        IMarkupWriter writer = newWriter();
        IComponentSpecification spec = newSpec();
        IRequestCycle cycle = newCycle();

        IfBean conditional = newInstance(TestIfBean.class, 
                new Object[] { 
            "condition", Boolean.TRUE,
            "specification", spec,
            "renderTag", Boolean.FALSE,
            "templateTagName", "fieldset"
        });
        
        expect(cycle.renderStackPush(conditional)).andReturn(conditional);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender(writer, cycle);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.renderStackPop()).andReturn(conditional);
        
        replay();
        
        conditional.addBody(body);
        
        conditional.render(writer, cycle);
        
        verify();
    }
    
    public static abstract class TestIfBean extends IfBean
    {
    	public TestIfBean() {
    	}
    	
    	public DataSqueezer getDataSqueezer() {
    		return DataSqueezerUtil.createUnitTestSqueezer();
    	}
    }
}
