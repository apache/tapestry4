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

package org.apache.tapestry.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;

/**
 *  Renders a link using an absolute URL, not simply a URI
 *  (as with {@link org.apache.tapestry.link.DefaultLinkRenderer}.  In addition,
 *  the scheme, server and port may be changed (this may be appropriate when
 *  switching between secure and insecure portions of an application).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class AbsoluteLinkRenderer extends DefaultLinkRenderer
{
    private String _scheme;
    private String _serverName;
    private int _port;

    public int getPort()
    {
        return _port;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public String getServerName()
    {
        return _serverName;
    }

    /**
     *  Used to override the port in the final URL, if specified.  If not specified,
     *  the port provided by the {@link javax.servlet.ServletRequest#getServerPort() request}
     *  is used (typically, the value 80).
     *
     **/

    public void setPort(int port)
    {
        _port = port;
    }
    
    /**
     *  Used to override the scheme in the final URL, if specified.  If not specified,
     *  the scheme provided by the {@link javax.servlet.ServletRequest#getScheme() request}
     *  is used (typically, <code>http</code>).
     *
     **/

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    /**
     *  Used to override the server name in the final URL, if specified.  If not specified,
     *  the port provided by the {@link javax.servlet.ServletRequest#getServerName()) request}
     *  is used.
     *
     **/

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

    protected String constructURL(ILink link, String anchor, IRequestCycle cycle)
    {
        return link.getAbsoluteURL(_scheme, _serverName, _port, anchor, true);
    }

}
