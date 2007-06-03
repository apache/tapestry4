package org.apache.tapestry.services.impl;

import org.apache.tapestry.*;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.services.ResponseBuilder;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests functionality of {@link PrototypeResponseBuilder}. 
 */
@Test
public class PrototypeResponseBuilderTest extends BaseComponentTestCase {

    public void test_Null_Writer_Render()
    {
        IRender render = newMock(IRender.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newBufferWriter();

        ResponseBuilder builder = new PrototypeResponseBuilder(cycle, writer, null);

        render.render(NullWriter.getSharedInstance(), cycle);

        replay();

        builder.render(writer, render, cycle);

        verify();

        assertSame(builder.getWriter(), writer);
        assertBuffer("");
    }

    public void test_Partial_Render()
    {
        IRender render = newMock(IRender.class);
        IComponent comp1 = newMock(IComponent.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);
        IMarkupWriter writer = newMock(IMarkupWriter.class);
        NestedMarkupWriter nested = newMock(NestedMarkupWriter.class);

        List parts = new ArrayList();
        parts.add("id1");

        PrototypeResponseBuilder builder = new PrototypeResponseBuilder(cycle, writer, parts);

        render.render(NullWriter.getSharedInstance(), cycle);

        expect(comp1.getClientId()).andReturn("id1").anyTimes();
        expect(comp1.peekClientId()).andReturn("id1").anyTimes();        

        expect(writer.getNestedWriter()).andReturn(nested);
        
        comp1.render(nested, cycle);

        replay();

        builder.render(null, render, cycle);

        assertTrue(builder.contains(comp1));
        assertTrue(IComponent.class.isInstance(comp1));

        builder.render(null, comp1, cycle);

        verify();

        assertSame(builder.getWriter(), writer);
        assertBuffer("");
    }
}
