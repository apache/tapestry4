/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  Additional methods implemented by the engine that are 
 *  exposed to {@link IEngineService engine services}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 */

public interface IEngineServiceView extends IEngine
{
    /**
     *  Invoked by a service to force the page selected by the {@link IRequestCycle}
     *  to be renderred.  This takes care of a number of bookkeeping issues, such
     *  as committing changes in page recorders.
     * 
     **/

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
        throws ServletException, IOException;

    /**
     *  Invoked to restart the application from start; this most frequently follows
     *  some kind of catastrophic failure.  This will invalidate any {@link javax.servlet.http.HttpSession}
     *  and force a redirect to the application servlet (i.e., invoking the home service
     *  in a subsequent request cycle).
     * 
     **/

    public void restart(IRequestCycle cycle) throws IOException;

    /**
     *  Invoked (typically by the reset service) to clear all cached data known
     *  to the engine.  This includes 
     *  pages, templates, helper beans, specifications,
     *  localized strings, etc., and
     *  is used during debugging.
     * 
     **/

    public void clearCachedData();

    /**
     *  Writes a detailed report of the exception to <code>System.err</code>.
     *  This is invoked by services that can't write an HTML description
     *  of the error because they don't provide text/html content (such as
     *  an asset that creates an image).
     *
     *  @since 1.0.10
     */

    public void reportException(String reportTitle, Throwable ex);
}
