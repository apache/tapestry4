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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.html.Relation}&nbsp; component.
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
@Test
public class TestRelation extends BaseComponentTestCase
{
    
    /**
     * Test that Relation does nothing when the entire page is rewinding
     */

    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(true);
        
        Relation relation = newInstance(Relation.class, null);
        
        expect(cycle.renderStackPush(relation)).andReturn(relation);
        
        expect(cycle.renderStackPop()).andReturn(relation);
        
        replay();

        relation.render(writer, cycle);

        verify();
    }
    
    /**
     * Test that exception is thrown when Shell is missing
     */    
    public void testShellMissing()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);
        Location componentLocation = newMock(Location.class);
        
        Relation relation = newInstance(Relation.class, 
                new Object[] {"location", componentLocation});
        
        expect(cycle.renderStackPush(relation)).andReturn(relation);
        
        trainGetShellFromCycle(cycle, null);

        expect(cycle.renderStackPop()).andReturn(relation);
        
        replay();
        
        try {
            
            relation.render(writer, cycle);
            unreachable();
            
        } catch (ApplicationRuntimeException ex) {
            
            assertEquals(ex.getLocation(), componentLocation);
        }

        verify();        
    }
    
    /**
     * Test that exception is thrown for invalid href parameter
     */      
    public void testInvalidHrefParameter()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);
        Location componentLocation = newMock(Location.class);
        
        Relation relation = newInstance(Relation.class, 
                new Object[] {"location", componentLocation, "href", null});
        
        Shell shell = newInstance(Shell.class, null);
        
        expect(cycle.renderStackPush(relation)).andReturn(relation);
        
        trainGetShellFromCycle(cycle, shell);

        expect(cycle.renderStackPop()).andReturn(relation);
        
        replay();
        
        try {
            
            relation.render(writer, cycle);
            unreachable();
            
        } catch (ApplicationRuntimeException ex) {
            
            assertEquals(ex.getLocation(), componentLocation);
        }

        verify();         
    }    
    
    protected void trainGetShellFromCycle(IRequestCycle cycle, Shell shell)
    {
        expect(cycle.getAttribute(Shell.SHELL_ATTRIBUTE)).andReturn(shell);
    }    
}
