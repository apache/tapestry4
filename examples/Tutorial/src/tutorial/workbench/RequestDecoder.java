package tutorial.workbench;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.DecodedRequest;
import net.sf.tapestry.IRequestDecoder;

/**
 *  A useless request decoder (does the same as the default), used to test that
 *  a user-supplied extension is actually used.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class RequestDecoder implements IRequestDecoder
{
    private static final Log LOG = LogFactory.getLog(RequestDecoder.class);

    public RequestDecoder()
    {
        LOG.info("<init>");
    }

    public DecodedRequest decodeRequest(HttpServletRequest request)
    {
        LOG.info("Decoding: " + request);

        DecodedRequest result = new DecodedRequest();

        result.setRequestURI(request.getRequestURI());
        result.setScheme(request.getScheme());
        result.setServerName(request.getServerName());
        result.setServerPort(request.getServerPort());

        return result;

    }

}
