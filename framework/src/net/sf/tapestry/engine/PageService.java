package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;

/**
 *  Basic server for creating a link to another page in the application.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class PageService extends AbstractService
{

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 1)
            throw new IllegalArgumentException(Tapestry.getString("service-single-parameter", PAGE_SERVICE));

        return assembleGesture(cycle, PAGE_SERVICE, (String[])parameters, null, true);

    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        RequestContext context = cycle.getRequestContext();
        String[] serviceContext = getServiceContext(context);

        if (serviceContext == null || serviceContext.length != 1)
            throw new ApplicationRuntimeException(
                Tapestry.getString("service-single-parameter", IEngineService.PAGE_SERVICE));

        String pageName = serviceContext[0];

        // At one time, the page service required a session, but that is no longer necessary.
        // Users can now bookmark pages within a Tapestry application.  Pages
        // can implement validate() and throw a PageRedirectException if they don't
        // want to be accessed this way.  For example, most applications have a concept
        // of a "login" and have a few pages that don't require the user to be logged in,
        // and other pages that do.  The protected pages should redirect to a login page.

        IPage page = cycle.getPage(pageName);

        // Allow the page to validate that the user is allowed to visit.  This is simple
        // protection from malicious users who hack the URLs directly, or make inappropriate
        // use of the back button. 

        page.validate(cycle);

        cycle.setPage(page);

        engine.renderResponse(cycle, output);

        return true;
    }

    public String getName()
    {
        return PAGE_SERVICE;
    }

}