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
package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.Tapestry;

/**
 *  Restarts the Tapestry application.  This is normally reserved for dealing with
 *  catastrophic failures of the application.  Discards the {@link javax.servlet.http.HttpSession}, if any,
 *  and redirects to the Tapestry application servlet URL (invoking the {@link HomeService}).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class RestartService extends AbstractService
{

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 0)
            throw new IllegalArgumentException(
                Tapestry.getString("service-no-parameters", Tapestry.RESTART_SERVICE));

        return constructLink(cycle, Tapestry.RESTART_SERVICE, null, null, true);
    }

    public boolean service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        engine.restart(cycle);

        return true;
    }

    public String getName()
    {
        return Tapestry.RESTART_SERVICE;
    }

}