package net.sf.tapestry.link;

import java.net.URL;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Abstract super-class for components that generate some form of
 *  &lt;a&gt; hyperlink using an {@link IEngineService}.
 *  Supplies support for the following parameters:
 *
 *  <ul>
 *  <li>scheme</li>
 *  <li>port</li>
 *  <li>anchor</li>
 * </ul>
 *
 * <p>Subclasses usually need only implement {@link #getServiceName()}
 * and {@link #getServiceParameters(IRequestCycle)}.
 * 
 *                       
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class GestureLink extends AbstractServiceLink
{
    private String _anchor;
    private String _scheme;
    private int _port;

    /**
     *  Constructs a URL based on the service, context plus scheme, port and anchor.
     * 
     **/

    protected String getURL(IRequestCycle cycle) throws RequestCycleException
    {
        return buildURL(cycle, getServiceParameters(cycle));
    }

    /**
     *  Invoked by {@link #getURL(IRequestCycle)}.
     *  The default implementation 
     *  invokes  returns null.
     *  Implementations can provide appropriate parameters as needed.
     *  
     *  @since 2.2
     * 
     **/
        
    protected Object[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        return null;
    }

    private String buildURL(IRequestCycle cycle, Object[] serviceParameters)
        throws RequestCycleException
    {
        String URL = null;

        String serviceName = getServiceName();
        IEngineService service = cycle.getEngine().getService(serviceName);

        if (service == null)
            throw new RequestCycleException(
                Tapestry.getString("GestureLink.missing-service", serviceName),
                this);

        Gesture g = service.buildGesture(cycle, this, serviceParameters);

        // Now, dress up the URL with scheme, server port and anchor,
        // as necessary.

        if (_scheme == null && _port == 0)
            URL = g.getURL();
        else
            URL = g.getAbsoluteURL(_scheme, null, _port);

        if (_anchor == null)
            return URL;

        return URL + "#" + _anchor;
    }

    /**
     *  Returns the service used to build URLs.  This method is implemented
     *  by subclasses.
     *
     **/

    protected abstract String getServiceName();

    public String getAnchor()
    {
        return _anchor;
    }

    public void setAnchor(String anchor)
    {
        this._anchor = anchor;
    }

    public int getPort()
    {
        return _port;
    }

    public void setPort(int port)
    {
        this._port = port;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public void setScheme(String scheme)
    {
        this._scheme = scheme;
    }

}