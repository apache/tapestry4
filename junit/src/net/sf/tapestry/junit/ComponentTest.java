package net.sf.tapestry.junit;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.engine.NullWriter;

/**
 *  Test a few random things in {@link net.sf.tapestry.AbstractComponent}
 *  and {@link  net.sf.tapestry.BaseComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ComponentTest extends TapestryTestCase
{
    private static class TestRender implements IRender
    {
        private boolean rendered = false;

        public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
        {
            rendered = true;
        }

    }

    private static class TestComponent extends BaseComponent
    {
        void addOuterTest(IRender render)
        {
            addOuter(render);
        }
        
        void testRenderComponent(IMarkupWriter write, IRequestCycle cycle)
        throws RequestCycleException
        {
            renderComponent(write, cycle);
        }
    }

    public ComponentTest(String name)
    {
        super(name);
    }

    /** 
     *  Test the ability of {@link net.sf.tapestry.BaseComponent#addOuter(IRender)}
     *  to add a large number of objects.
     * 
     **/

    public void testOuter() throws Exception
    {
        TestComponent c = new TestComponent();

        TestRender[] list = new TestRender[50];

        for (int i = 0; i < list.length; i++)
        {
            list[i] = new TestRender();
            c.addOuterTest(list[i]);
        }

        IMarkupWriter writer = new NullWriter();

        c.testRenderComponent(writer, null);

        for (int i = 0; i < list.length; i++)
            assertTrue("Outer object #" + i + " did render.", list[i].rendered);
    }
}
