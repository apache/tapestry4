package org.apache.tapestry.services.impl;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.BaseTagWriter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestBaseTagWriter extends HiveMindTestCase
{
    private IMarkupWriter newWriter(String url)
    {
        IMarkupWriter writer = (IMarkupWriter) newMock(IMarkupWriter.class);

        writer.beginEmpty("base");
        writer.attribute("href", url);
        writer.println();

        return writer;
    }

    private INamespace newNamespace(String id)
    {
        MockControl control = newControl(INamespace.class);
        INamespace ns = (INamespace) control.getMock();

        ns.getId();
        control.setReturnValue(id);

        return ns;
    }

    private IPage newPage(String pageName)
    {
        return newPage(newNamespace(null), pageName);
    }

    private IPage newPage(INamespace ns, String pageName)
    {
        MockControl control = newControl(IPage.class);
        IPage page = (IPage) control.getMock();

        page.getNamespace();
        control.setReturnValue(ns);

        if (pageName != null)
        {
            page.getPageName();
            control.setReturnValue(pageName);
        }

        return page;
    }

    private IRequestCycle newRequestCycle(IPage page, String url)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getPage();
        control.setReturnValue(page);

        cycle.getAbsoluteURL(url);
        control.setReturnValue("http://foo.com/context" + url);

        return cycle;
    }

    private void run(IMarkupWriter writer, IRequestCycle cycle)
    {
        replayControls();

        new BaseTagWriter().render(writer, cycle);

        verifyControls();
    }

    public void testNotApplicationNamespace()
    {
        INamespace ns = newNamespace("library");
        IPage page = newPage(ns, null);
        IRequestCycle cycle = newRequestCycle(page, "/");
        IMarkupWriter writer = newWriter("http://foo.com/context/");

        run(writer, cycle);
    }
    
    public void testInRoot()
    {
        IPage page = newPage("Home");
        IRequestCycle cycle = newRequestCycle(page, "/");
        IMarkupWriter writer = newWriter("http://foo.com/context/");

        run(writer, cycle);
    }
    
    public void testInSubFolder()
    {
        IPage page = newPage("admin/AdminMenu");
        IRequestCycle cycle = newRequestCycle(page, "/admin/");
        IMarkupWriter writer = newWriter("http://foo.com/context/admin/");

        run(writer, cycle);   
    }
}