/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.request;

/**
 *  Contains properties of an {@link javax.servlet.http.HttpServletRequest}
 *  that have been extracted from the request (or otherwise determined).
 * 
 *  <p>An alternative idea would have been to create a new 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  wrapper that overode the various methods.  That struck me as causing
 *  more confusion; instead (in the few places it counts), classes will
 *  get the decoded properties from the {@link RequestContext}.
 *
 *  @see IRequestDecoder
 *  @see RequestContext#getScheme()
 *  @see RequestContext#getServerName()
 *  @see RequestContext#getServerPort()
 *  @see RequestContext#getRequestURI()
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