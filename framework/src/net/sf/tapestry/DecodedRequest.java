package net.sf.tapestry;

/**
 *  Contains properties of an {@link javax.servlet.http.HttpServletRequest}
 *  that have been extracted from the request (or otherwise determined).
 * 
 *  <p>An alternative idea would have been to create a new 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  wrapper that overode the various methods.  That struck me as causing
 *  more confusion; instead (in the few places it counts), classes will
 *  get the decoded properties from the {@link net.sf.tapestry.RequestContext}.
 *
 *  @see net.sf.tapestry.IRequestDecoder
 *  @see net.sf.tapestry.RequestContext#getScheme()
 *  @see net.sf.tapestry.RequestContext#getServerName()
 *  @see net.sf.tapestry.RequestContext#getServerPort()
 *  @see net.sf.tapestry.RequestContext#getRequestURI()
 * 
 *  @author Howard Lewis Ship
 *  @version DecodedRequest.java,v 1.1 2002/08/20 21:49:58 hship Exp
 *  @since 2.2
 * 
 **/

public class DecodedRequest
{
    private String _scheme;
    private String _serverName;
    private String _requestURI;
    private int _serverPort;

    public int getServerPort()
    {
        return _serverPort;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public String getServerName()
    {
        return _serverName;
    }

    public String getRequestURI()
    {
        return _requestURI;
    }

    public void setServerPort(int serverPort)
    {
        _serverPort = serverPort;
    }

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

    public void setRequestURI(String URI)
    {
        _requestURI = URI;
    }
}