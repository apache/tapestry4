/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.link;

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