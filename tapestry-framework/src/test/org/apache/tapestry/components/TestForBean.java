// Copyright 2004, 2005 The Apache Software Foundation
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

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link ForBean} .
 *
 * @author jkuhnert
 */
@Test
public class TestForBean extends BaseComponentTestCase
{   
    public void test_Render()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newBufferWriter();
        ValueConverter conv = newMock(ValueConverter.class);
        ResponseBuilder resp = newMock(ResponseBuilder.class);
        IComponentSpecification spec = newSpec();
        IParameterSpecification pspec = newMock(IParameterSpecification.class);
        
        List src = new ArrayList();
        src.add("Test1");
        src.add("Test2");
        
        IBinding source = newBinding(src);
        
        ForBean bean = newInstance(ForBean.class, new Object[] {
            "valueConverter", conv,
            "responseBuilder", resp,
            "templateTagName", "div",
            "renderTag", true,
            "specification", spec
            });
        
        expect(cycle.renderStackPush(bean)).andReturn(bean);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(null);
        
        expect(spec.getParameter("source")).andReturn(pspec).anyTimes();
        
        expect(conv.coerceValue(src, Iterator.class)).andReturn(src.iterator());
        
        expect(resp.isDynamic()).andReturn(false).anyTimes();
        
        expect(cycle.getResponseBuilder()).andReturn(resp).anyTimes();
        
        IRender body = newMock(IRender.class);
        
        resp.render(writer, body, cycle);
        expectLastCall().anyTimes();
        
        expect(cycle.renderStackPop()).andReturn(bean);
        
        replay();
        
        bean.addBody(body);
        bean.setBinding("source", source);
        
        bean.render(writer, cycle);
        
        verify();
        
        assertBuffer("<div></div><div></div>");
    }
    
    public void test_Rewind_Missing_Converter()
    {
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newWriter();
        ValueConverter conv = newMock(ValueConverter.class);
        ResponseBuilder resp = newMock(ResponseBuilder.class);
        IForm form = newMock(IForm.class);
        IComponentSpecification spec = newSpec();
       // IParameterSpecification pspec = newMock(IParameterSpecification.class);
        
        List src = new ArrayList();
        src.add("Test1");
        src.add("Test2");
        
        IBinding source = newBinding(src);
        
        ForBean bean = newInstance(ForBean.class, new Object[] {
            "valueConverter", conv,
            "responseBuilder", resp,
            "templateTagName", "div",
            "renderTag", true,
            "specification", spec
            });
        
        expect(cycle.renderStackPush(bean)).andReturn(bean);
        
        expect(cycle.isRewinding()).andReturn(true).anyTimes();
        
        expect(cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE)).andReturn(form);
        
        expect(form.isRewinding()).andReturn(true);
        
        expect(form.getElementId(bean)).andReturn("foo");
        
        String[] parms = (String[])src.toArray((new String[src.size()]));
        
        expect(cycle.getParameters("foo")).andReturn(parms);
        
        expect(conv.coerceValue(src, Iterator.class)).andReturn(src.iterator());
        
        expect(resp.isDynamic()).andReturn(false).anyTimes();
        
        expect(cycle.renderStackPop()).andReturn(bean);
        
        replay();
        
        // bean.addBody(body);
        bean.setBinding("source", source);
        
        bean.render(writer, cycle);
        
        verify();
    }
}
