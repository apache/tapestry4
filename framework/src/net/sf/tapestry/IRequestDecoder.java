package net.sf.tapestry;

import javax.servlet.http.HttpServletRequest;

/**
 *  Given a {@link javax.servlet.http.HttpServletRequest}, identifies
 *  the correct request properties (server, scheme, URI and port).
 * 
 *  <p>An implementation of this class may be necessary when using
 *  Tapestry with specific firewalls which may obscure
 *  the scheme, server, etc. visible to the client web browser
 *  (the request appears to arrive from the firewall server, not the
 *  client web browser).
 *
 *  @author Howard Lewis Ship
 *  @version IRequestDecoder.java,v 1.1 2002/08/20 21:49:58 hship Exp
 *  @since 2.2
 * 
 **/

public interface IRequestDecoder
{

    /**
     *  Invoked to identify the actual properties from the request.
     * 
     **/

    public DecodedRequest decodeRequest(HttpServletRequest request);
}
