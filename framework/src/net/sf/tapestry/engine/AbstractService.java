package net.sf.tapestry.engine;

import java.io.IOException;
import java.net.URLDecoder;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.StringSplitter;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 *  Abstract base class for implementing engine services.  Instances of services
 *  are shared by many engines and threads, so they must be threadsafe.
 * 
 *  <p>
 *  Note; too much of the URL encoding/decoding stategy is fixed here.
 *  A future release of Tapestry may extract this strategy, allowing developers
 *  to choose the method via which URLs are encoded.
 * 
 *  @see net.sf.tapestry.engine.AbstractEngine#getService(String)
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractService implements IEngineService
{
    /**
     *  Assembles a URL for the service.
     *
     *  @param the path for the servlet for this Tapestry application
     *  @param serviceName the name of the service
     *  @param serviceContext context related to the service itself which is added to the URL as-is
     *  @param otherContext additional context provided by the component; this is application specific
     *  information, and is encoded with {@link URLEncoder#encode(String)} before being added
     *  to the query.
     *  @param stateful if true, the final URL must be encoded with the HttpSession id
     *
     **/

    protected Gesture assembleGesture(
        IRequestCycle cycle,
        String serviceName,
        String[] serviceContext,
        Object[] parameters,
        boolean stateful)
    {
        DataSqueezer squeezer = cycle.getEngine().getDataSqueezer();
        String[] squeezed = null;

        try
        {
            squeezed = squeezer.squeeze(parameters);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        return new Gesture(cycle, serviceName, serviceContext, squeezed, stateful);
    }

    /**
     *  Returns the service context as an array of Strings.
     *  Returns null if there are no service context strings.
     *
     **/

    protected String[] getServiceContext(RequestContext context)
    {
        String raw = context.getParameter(CONTEXT_QUERY_PARMETER_NAME);
        
        if (raw == null)
            return null;
            
        return new StringSplitter('/').splitToArray(raw);
    }

    /**
     *  Returns the service parameters as an array of Strings.
     *
     **/

    protected Object[] getParameters(IRequestCycle cycle)
    {
        RequestContext context = cycle.getRequestContext();
        
        String[] squeezed = 
            context.getParameters(PARAMETERS_QUERY_PARAMETER_NAME);
            
        if (Tapestry.size(squeezed) == 0)
            return squeezed;
            
        try
        {
            DataSqueezer squeezer =
                cycle.getEngine().getDataSqueezer();
                
            return squeezer.unsqueeze(squeezed);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }
}