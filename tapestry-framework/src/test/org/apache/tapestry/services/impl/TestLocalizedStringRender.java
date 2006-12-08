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
package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.parse.LocalizationToken;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link LocalizedStringRender} .
 *
 * @author jkuhnert
 */
@Test
public class TestLocalizedStringRender extends BaseComponentTestCase
{   
    protected LocalizationToken newToken(String tag, String key, boolean raw, Map attributes)
    {
        Location l = newLocation();
        return new LocalizationToken(tag, key, raw, attributes, l);
    }
    
    public void test_No_Attributes()
    {
        IRequestCycle cycle = newCycle(false);
        IMarkupWriter writer = newWriter();
        IComponent c = newComponent();
        LocalizationToken tok = newToken(null, "hello", false, null);
        Messages m = newMock(Messages.class);
        
        LocalizedStringRender render = new LocalizedStringRender(c, tok);
        
        expect(c.getMessages()).andReturn(m);
        expect(m.getMessage("hello")).andReturn("World");
        
        writer.print("World", false);
        
        replay();
        
        render.render(writer, cycle);
        
        verify();
    }
    
    public void test_Attributes()
    {
        IRequestCycle cycle = newCycle(false);
        IMarkupWriter writer = newWriter();
        IComponent c = newComponent();
        
        Map attr = new HashMap();
        attr.put("class", "feely mcfeels");
        attr.put("joo", "talkin to me?");
        
        LocalizationToken tok = newToken(null, "hello", false, attr);
        Messages m = newMock(Messages.class);
        
        LocalizedStringRender render = new LocalizedStringRender(c, tok);
        
        expect(c.getMessages()).andReturn(m);
        expect(m.getMessage("hello")).andReturn("World");
        
        writer.begin("span");
        writer.attribute("joo", (String)attr.get("joo"));
        writer.attribute("class", (String)attr.get("class"));
        writer.print("World", false);
        writer.end();
        
        replay();
        
        render.render(writer, cycle);
        
        verify();
    }
    
    public void test_Attributes_Tag()
    {
        IRequestCycle cycle = newCycle(false);
        IMarkupWriter writer = newWriter();
        IComponent c = newComponent();
        
        Map attr = new HashMap();
        attr.put("class", "feely mcfeels");
        
        LocalizationToken tok = newToken("div", "hello", false, attr);
        Messages m = newMock(Messages.class);
        
        LocalizedStringRender render = new LocalizedStringRender(c, tok);
        
        expect(c.getMessages()).andReturn(m);
        expect(m.getMessage("hello")).andReturn("World");
        
        writer.begin("div");
        writer.attribute("class", (String)attr.get("class"));
        writer.print("World", false);
        writer.end();
        
        replay();
        
        render.render(writer, cycle);
        
        verify();
    }
}
