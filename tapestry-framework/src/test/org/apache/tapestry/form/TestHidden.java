// Copyright Oct 21, 2006 The Apache Software Foundation
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
package org.apache.tapestry.form;

import static org.easymock.EasyMock.*;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.DataSqueezer;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link Hidden}.
 */
@Test
public class TestHidden extends BaseFormComponentTestCase
{

    public void test_Render()
    {   
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        DataSqueezer squeezer = newMock(DataSqueezer.class);
        
        IMarkupWriter writer = newMarkupWriter();

        MockDelegate delegate = new MockDelegate();
        
        Hidden component = newInstance(Hidden.class, new Object[]
        { "name", "fred", "value", new Integer(10), 
            "encode", Boolean.TRUE, "dataSqueezer", squeezer });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);
        
        form.setFormFieldUpdating(true);
        
        expect(squeezer.squeeze(10)).andReturn("i10");
        
        delegate.setFormComponent(component);
        
        form.addHiddenValue("fred", "fred", "i10");
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }
}
