package tutorial.workbench;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.engine.HomeService;

/**
 *  Special version of the home service used to reset the visit tab when re-entering
 *  the Tapestry application from a static HTML page.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @see Redirect
 * 
 **/

public class WorkbenchHomeService extends HomeService
{

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        Visit visit = (Visit) engine.getVisit();

        if (visit != null)
            visit.setActiveTabName("Home");

        return super.service(engine, cycle, output);
    }

}