package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.Tapestry;

/**
 *  Implementation of the direct service, which encodes the page and component id in
 *  the service context, and passes application-defined parameters as well.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class DirectService extends AbstractService
{

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        String[] context;

        // New since 1.0.1, we use the component to determine
        // the page, not the cycle.  Through the use of tricky
        // things such as Block/InsertBlock, it is possible 
        // that a component from a page different than
        // the response page will render.
        // In 1.0.6, we start to record *both* the render page
        // and the component page (if different), as the extended
        // context.

        IPage renderPage = cycle.getPage();
        IPage componentPage = component.getPage();

        if (renderPage == componentPage)
        {
            context = new String[2];
            context[0] = componentPage.getName();
            context[1] = component.getIdPath();
        }
        else
        {
            context = new String[3];
            context[0] = renderPage.getName();
            context[1] = componentPage.getName();
            context[2] = component.getIdPath();
        }

        return assembleGesture(cycle, DIRECT_SERVICE, context, parameters, true);
    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        IDirect direct;
        int count = 0;
        String componentPageName;
        IPage componentPage;
        RequestContext requestContext = cycle.getRequestContext();
        String[] serviceContext = getServiceContext(requestContext);

        if (serviceContext != null)
            count = serviceContext.length;

        if (count != 2 && count != 3)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractEngine.direct-context-parameters"));

        int i = 0;
        String pageName = serviceContext[i++];

        if (count == 2)
            componentPageName = pageName;
        else
            componentPageName = serviceContext[i++];

        String componentPath = serviceContext[i++];

        IPage page = cycle.getPage(pageName);

        // Allow the page to validate that the user is allowed to visit.  This is simple
        // protection from malicious users who hack the URLs directly, or make inappropriate
        // use of the back button. 
        // Using Block/InsertBlock, it is possible that the component is on a different page
        // than the render page.  The render page is validated, not necessaily the component
        // page.

        page.validate(cycle);
        cycle.setPage(page);

        if (count == 2)
            componentPage = page;
        else
            componentPage = cycle.getPage(componentPageName);

        IComponent component = componentPage.getNestedComponent(componentPath);

        try
        {
            direct = (IDirect) component;
        }
        catch (ClassCastException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("AbstractEngine.direct-component-wrong-type", component.getExtendedId()),
                component,
                ex);
        }

        if (direct.isStateful())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException(
                Tapestry.getString("DirectService.stale-session-exception", direct.getExtendedId()),
                direct.getPage());
        }


        Object[] parameters = getParameters(cycle);

        cycle.setServiceParameters(parameters);
        direct.trigger(cycle);

        // Render the response.  This will be the response page (the first element in the context)
        // unless the direct (or its delegate) changes it.

        engine.renderResponse(cycle, output);

        return true;
    }

    public String getName()
    {
        return DIRECT_SERVICE;
    }
}