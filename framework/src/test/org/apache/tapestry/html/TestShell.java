package org.apache.tapestry.html;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * Tests for the {@link org.apache.tapestry.html.Shell}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestShell extends HiveMindTestCase
{
    private Creator _creator = new Creator();

    private IMarkupWriter newMarkupWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    private IRequestCycle newRequestCycle(boolean rewinding)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(rewinding);

        return cycle;
    }

    private IRender newRender()
    {
        return (IRender) newMock(IRender.class);
    }

    private IApplicationSpecification newSpec()
    {
        ApplicationSpecification spec = new ApplicationSpecification();

        spec.setName("TestShell");

        return spec;
    }

    private IPage newPage(String name)
    {
        MockControl control = newControl(IPage.class);
        IPage page = (IPage) control.getMock();

        page.getPageName();
        control.setReturnValue(name);

        return page;
    }

    /**
     * Test that Shell does very little when the entire page is rewinding (which itself is a
     * holdback to the action service).
     */

    public void testRewinding()
    {
        IMarkupWriter writer = newMarkupWriter();
        IRequestCycle cycle = newRequestCycle(true);

        IRender body = newRender();

        body.render(writer, cycle);

        replayControls();

        Shell shell = (Shell) _creator.newInstance(Shell.class);

        shell.addBody(body);

        shell.render(writer, cycle);

        verifyControls();
    }
}