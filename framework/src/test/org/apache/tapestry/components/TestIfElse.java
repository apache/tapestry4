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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.io.DataSqueezerUtil;

/**
 * Tests for {@link org.apache.tapestry.components.If}&nbsp;
 * and {@link org.apache.tapestry.components.Else}&nbsp;
 * components.
 * 
 * @author Mindbridge
 * @since 4.0
 */
public class TestIfElse extends BaseComponentTestCase
{
    private IRender newRender(IMarkupWriter writer, IRequestCycle cycle)
    {
        IRender render = (IRender) newMock(IRender.class);
        
        render.render(writer, cycle);

        return render;
    }

    public void testRenderPlainTrue()
    {
        IRequestCycle cycle = (IRequestCycle) newMock(IRequestCycle.class);

        IMarkupWriter writer = newWriter();
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        trainResponseBuilder(cycle, writer);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);
        
        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();
        
        replay();
        
        IfBean conditional = (IfBean) newInstance(IfBean.class, new Object[]
        { "condition", Boolean.TRUE });
        conditional.addBody(body);
        
        conditional.render(writer, cycle);
        
        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void testRenderPlainFalse()
    {
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replay();

        IfBean conditional = (IfBean) newInstance(IfBean.class, new Object[]
        { "condition", Boolean.FALSE });
        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void testRenderInFormTrue()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE });

        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();

        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);

        expect(form.getElementId(conditional)).andReturn("If");
        
        form.addHiddenValue("If", "T");
        
        trainResponseBuilder(cycle, writer);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);
        
        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();

        replay();


        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void testRenderInFormFalse()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.FALSE });

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);

        expect(form.getElementId(conditional)).andReturn("If");
        
        form.addHiddenValue("If", "F");
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replay();


        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verify();
    }

    public void testIgnoreElementWhenRewindingTrue()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();

        expect(cycle.isRewinding()).andReturn(true);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.isRewinding()).andReturn(true);
        
        expect(form.getElementId(conditional)).andReturn("If");
        
        expect(cycle.getParameter("If")).andReturn("T");

        trainResponseBuilder(cycle, writer);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);

        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.TRUE);

        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();

        replay();

        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verify();
    }

    public void testIgnoreElementWhenRewindingFalse()
    {
        IMarkupWriter writer = newWriter();
        
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();

        expect(cycle.isRewinding()).andReturn(true);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.isRewinding()).andReturn(true);
        
        expect(form.getElementId(conditional)).andReturn("If");
        
        expect(cycle.getParameter("If")).andReturn("F");
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        expect(cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE)).andReturn(Boolean.FALSE);
        
        expect(cycle.isRewinding()).andReturn(true);
        
        trainResponseBuilder(cycle, writer);
        
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replay();

        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verify();
    }

    public void testElement()
    {
        IMarkupWriter writer = newWriter();
        
		IBinding informal = newBinding("informal-value");
        IComponentSpecification spec = newSpec("informal", null);
        
        IRequestCycle cycle = newCycle();

        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        IRender body = newRender(writer, cycle);

        writer.begin("div");
        writer.attribute("informal", "informal-value");

        writer.end("div");

        trainResponseBuilder(cycle, writer);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);

        
        replay();

        IfBean conditional = (IfBean) newInstance(TestIfBean.class, new Object[]
        { "condition", Boolean.TRUE, "element", "div", "specification", spec });
        conditional.addBody(body);
        conditional.setBinding("informal", informal);

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