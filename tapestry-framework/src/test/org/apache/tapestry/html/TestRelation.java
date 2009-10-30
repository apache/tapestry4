// Copyright 2005-2009 The Apache Software Foundation
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

import java.io.PrintWriter;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IRender;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.markup.MarkupWriterImpl;
import org.apache.tapestry.markup.AsciiMarkupFilter;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;
import org.easymock.IAnswer;

/**
 * Tests for the {@link org.apache.tapestry.html.Relation} component.
 * 
 * @author Andreas Andreou
 * @since 4.1.1
 */
@Test(sequential=true)
public class TestRelation extends BaseComponentTestCase
{
    private static final String SYSTEM_NEWLINE = (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
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

    public void testIeStyle()
    {
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle(false);
        Location componentLocation = newMock(Location.class);
        StringBuffer shellOutput = new StringBuffer();

        MarkupWriterSource source = newMock(MarkupWriterSource.class);
        expect(source.newMarkupWriter(isA(PrintWriter.class), isA(ContentType.class))).andAnswer(
                new IAnswer<IMarkupWriter>() {
                    public IMarkupWriter answer() throws Throwable {
                        return new MarkupWriterImpl("text/html",
                                (PrintWriter) getCurrentArguments()[0],
                                new AsciiMarkupFilter());
                    }
                }
        );

        Relation relation = newInstance(Relation.class,
                "location", componentLocation,
                "markupWriterSource", source,
                "useBody", true,
                "ieCondition", "IE"
        );

        relation.addBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                writer.print("Some css rules");
            }
        });

        trainResponseBuilder(cycle, writer);

        Shell shell = newInstance(Shell.class, "contentBuffer", shellOutput);

        expect(cycle.renderStackPush(relation)).andReturn(relation);

        trainGetShellFromCycle(cycle, shell);

        expect(cycle.renderStackPop()).andReturn(relation);

        replay();

        relation.render(writer, cycle);

        // nothing should be output - just pushed to Shell
        assertBuffer("");

        // now check what Shell has gathered
        assertEquals(shellOutput.toString(), "<!--[if IE]>" + 
                "<style type=\"text/css\">Some css rules</style>" + 
                "<![endif]-->" + SYSTEM_NEWLINE);
        verify();
    }
    
    protected void trainGetShellFromCycle(IRequestCycle cycle, Shell shell)
    {
        expect(cycle.getAttribute(Shell.SHELL_ATTRIBUTE)).andReturn(shell);
    }    
}
