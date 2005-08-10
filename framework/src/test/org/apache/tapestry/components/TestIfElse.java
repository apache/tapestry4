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

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.io.DataSqueezerUtil;
import org.easymock.MockControl;

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
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(null);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.TRUE);
        
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();

        replayControls();


        IfBean conditional = (IfBean) newInstance(IfBean.class, new Object[]
        { "condition", Boolean.TRUE });
        conditional.addBody(body);

        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verifyControls();
    }

    public void testRenderPlainFalse()
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(null);
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.FALSE);
        
        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        IMarkupWriter writer = newWriter();
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replayControls();


        IfBean conditional = (IfBean) newInstance(IfBean.class, new Object[]
        { "condition", Boolean.FALSE });
        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verifyControls();
    }

    public void testRenderInFormTrue()
    {
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE });

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(form);

        form.getElementId(conditional);
        formc.setReturnValue("If");
        
        form.addHiddenValue("If", "T");
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);
        
        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.TRUE);
        
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();

        replayControls();


        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verifyControls();
    }

    public void testRenderInFormFalse()
    {
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.FALSE });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(form);
        
        form.getElementId(conditional);
        formc.setReturnValue("If");
        
        form.addHiddenValue("If", "F");
        
        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);
        
        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.FALSE);
        
        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        IMarkupWriter writer = newWriter();
        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replayControls();


        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);

        verifyControls();
    }

    public void testIgnoreElementWhenRewindingTrue()
    {
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(true);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(form);
        
        form.getElementId(conditional);
        formc.setReturnValue("If");
        
        cycle.getParameter("If");
        cyclec.setReturnValue("T");

        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);

        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.TRUE);
        
        IMarkupWriter writer = newWriter();

        IRender body = newRender(writer, cycle);
        IRender body2 = newRender();

        replayControls();

        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verifyControls();
    }

    public void testIgnoreElementWhenRewindingFalse()
    {
        IfBean conditional = (IfBean) newInstance(TestIfBean.class, 
        		new Object[] { "condition", Boolean.TRUE, "element", "div" });
        
        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(true);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(form);
        
        form.getElementId(conditional);
        formc.setReturnValue("If");
        
        cycle.getParameter("If");
        cyclec.setReturnValue("F");

        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.FALSE);

        cycle.getAttribute(IfBean.IF_VALUE_ATTRIBUTE);
        cyclec.setReturnValue(Boolean.FALSE);
        
        cycle.isRewinding();
        cyclec.setReturnValue(true);
        
        IMarkupWriter writer = newWriter();

        IRender body = newRender();
        IRender body2 = newRender(writer, cycle);

        replayControls();

        conditional.addBody(body);
        conditional.render(writer, cycle);

        ElseBean reverse = (ElseBean) newInstance(ElseBean.class);
        reverse.addBody(body2);
        reverse.render(writer, cycle);
        
        verifyControls();
    }

    public void testElement()
    {
		IBinding informal = newBinding("informal-value");
        IComponentSpecification spec = newSpec("informal", null);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.isRewinding();
        cyclec.setReturnValue(false);
        
        cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);
        cyclec.setReturnValue(null);
        
        IMarkupWriter writer = newWriter();
        IRender body = newRender(writer, cycle);

        writer.begin("div");
        writer.attribute("informal", "informal-value");

        writer.end("div");

        cycle.setAttribute(IfBean.IF_VALUE_ATTRIBUTE, Boolean.TRUE);

        
        replayControls();

        IfBean conditional = (IfBean) newInstance(TestIfBean.class, new Object[]
        { "condition", Boolean.TRUE, "element", "div", "specification", spec });
        conditional.addBody(body);
        conditional.setBinding("informal", informal);

        conditional.render(writer, cycle);

        verifyControls();
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