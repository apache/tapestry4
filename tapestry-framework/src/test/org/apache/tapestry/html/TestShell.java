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

package org.apache.tapestry.html;

import static org.easymock.EasyMock.expect;

import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.html.Shell}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestShell extends BaseComponentTestCase
{

    /**
     * Test that Shell does very little when the entire page is rewinding (which itself is a
     * holdback to the action service).
     */

    public void test_Rewinding()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        
        IRequestCycle cycle = newCycle(true, writer);
        IRender body = newRender();
        
        Shell shell = newInstance(Shell.class, null);
        
        expect(cycle.renderStackPush(shell)).andReturn(shell);
        
        shell.addBody(body);

        trainStoreShellInCycle(cycle, shell);
        trainGetNestedWriter(writer, nested);
        
        body.render(nested, cycle);
        
        nested.close();
        
        trainRemoveShellFromCycle(cycle);
        
        expect(cycle.renderStackPop()).andReturn(shell);
        
        replay();

        shell.render(writer, cycle);

        verify();
    }
    
    public void test_Add_Relation()
    {        
        Shell shell = newInstance(Shell.class, null);
        RelationBean css1 = new RelationBean();
        css1.setHref("temp");
        RelationBean css2 = new RelationBean();
        css2.setHref("temp");
        shell.addRelation(css1);
        shell.addRelation(css2);
        
        List all = shell.getRelations();
        assertEquals(all.size(), 1);   
    }
    
    public void test_Include_Additional_Content_Null()
    {
        StringBuffer sb = new StringBuffer();
        Shell shell = (Shell) newInstance(Shell.class, "contentBuffer", sb);
        shell.includeAdditionalContent(null);
        assertEquals(sb.length(), 0);
    }
    
    public void test_Include_Additional_Content()
    {
        StringBuffer sb = new StringBuffer();
        Shell shell = (Shell) newInstance(Shell.class, "contentBuffer", sb);
        shell.includeAdditionalContent("data");
        assertEquals(sb.toString(), "data");
    }    

    protected void trainStoreShellInCycle(IRequestCycle cycle, Shell shell)
    {
        expect(cycle.getAttribute(Shell.SHELL_ATTRIBUTE)).andReturn(null);
        cycle.setAttribute(Shell.SHELL_ATTRIBUTE, shell);
    }

    protected void trainRemoveShellFromCycle(IRequestCycle cycle)
    {
        cycle.removeAttribute(Shell.SHELL_ATTRIBUTE);
    }
}