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

package org.apache.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  A service, provided by the {@link IEngine}, for its pages and/or components.  
 *  Services are
 *  responsible for constructing {@link EngineServiceLink}s (an encoding of URLs)
 *  to represent dynamic application behavior, and for
 *  parsing those URLs when a subsequent request involves them.
 *
 *  @see IEngine#getService(String)
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IEngineService
{
    /**
     *  Builds a URL for a service.  This is performed during the
     *  rendering phase of one request cycle and bulds URLs that will
     *  invoke activity in a subsequent request cycle.
     *
     *  @param cycle Defines the request cycle being processed.
     *  @param component The component requesting the URL.  Generally, the
     *  service context is established from the component.
     *  @param parameters Additional parameters specific to the
     *  component requesting the EngineServiceLink.
     *  @return The URL for the service.  The URL will have to be encoded
     *  via {@link HttpServletResponse#encodeURL(java.lang.String)}.
     *
     **/

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters);

    /**
     *  Perform the service, interpreting the URL (from the
     *  {@link javax.servlet.http.HttpServletRequest}) 
     *  responding appropriately, and
     *  rendering a result page.
     *
     *  <p>The return value indicates whether processing of the request could, in any way,
     *  change the state of the {@link IEngine engine}.  Generally, this is true.
     *
     *  @see IEngine#service(RequestContext)
     *  @param engine a view of the {@link IEngine} with additional methods needed by services
     *  @param cycle the incoming request
     *  @param output stream to which output should ultimately be directed
     * 
     **/

    public boolean service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException;

    /**
     *  Returns the name of the service.
     *
     *  @since 1.0.1
     **/

    public String getName();
}