package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;

/**
 *  Restarts the Tapestry application.  This is normally reserved for dealing with
 *  catastrophic failures of the application.  Discards the {@link javax.servlet.http.HttpSession}, if any,
 *  and redirects to the Tapestry application servlet URL (invoking the {@link HomeService}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class RestartService extends AbstractService
{

    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 0)
            throw new IllegalArgumentException(Tapestry.getString("service-no-parameters", RESTART_SERVICE));

        return assembleGesture(cycle, RESTART_SERVICE, null, null, true);
    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        engine.restart(cycle);

        return true;
    }

    public String getName()
    {
        return RESTART_SERVICE;
    }

}