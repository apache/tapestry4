package net.sf.tapestry.junit.mock.app;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.junit.mock.lib.Dumper;

/**
 *  Part of the Mock application test suite.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Home extends BasePage
{
    public void linkClicked(IRequestCycle cycle)
    {
        Dumper dumper = (Dumper)cycle.getPage("lib:Dumper");
        
        dumper.setObjects(cycle.getServiceParameters());
        
        cycle.setPage(dumper);
    }
}
