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

package org.apache.tapestry.vlib;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.vlib.ejb.IBookQueryHome;
import org.apache.tapestry.vlib.ejb.IOperationsHome;

/**
 *  The Global object is shared by all instances of the engine. It is used
 *  to centralize all JNDI lookups.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Global
{
    private static final Log LOG = LogFactory.getLog(Global.class);

    // Home interfaces are static, such that they are only
    // looked up once (JNDI lookup is very expensive).

    private IBookQueryHome _bookQueryHome;
    private IOperationsHome _operationsHome;

    private Context _rootNamingContext;

    public synchronized IBookQueryHome getBookQueryHome()
    {
        if (_bookQueryHome == null)
            _bookQueryHome =
                (IBookQueryHome) findNamedObject("vlib/BookQuery", IBookQueryHome.class);

        return _bookQueryHome;
    }

    public synchronized IOperationsHome getOperationsHome()
    {
        if (_operationsHome == null)
            _operationsHome =
                (IOperationsHome) findNamedObject("vlib/Operations", IOperationsHome.class);

        return _operationsHome;
    }

    public synchronized Object findNamedObject(String name, Class expectedClass)
    {
        Object result = null;

        int i = 0;
        while (true)
        {
            try
            {
                Object raw = getRootNamingContext().lookup(name);

                if (raw == null)
                {
                    String message =
                        "JNDI lookup of "
                            + name
                            + " yielded null; expected instance of "
                            + expectedClass.getName()
                            + ".";

                    LOG.error(message);

                    if (i++ == 0)
                    {
                        clear();
                        continue;
                    }

                    throw new ApplicationRuntimeException(message);
                }

                result = PortableRemoteObject.narrow(raw, expectedClass);

                break;
            }
            catch (ClassCastException ex)
            {
                throw new ApplicationRuntimeException(
                    "Object " + name + " is not type " + expectedClass.getName() + ".",
                    ex);
            }
            catch (NamingException ex)
            {
                String message = "Unable to resolve object " + name + ".";

                if (i++ == 0)
                {
                    LOG.error(message, ex);
                    clear();
                }
                else
                    throw new ApplicationRuntimeException(message, ex);
            }
        }

        return result;
    }

    public synchronized Context getRootNamingContext()
    {
        if (_rootNamingContext == null)
        {
            int i = 0;
            while (true)
            {
                try
                {
                    _rootNamingContext = new InitialContext();

                    break;
                }
                catch (NamingException ex)
                {
                    String message = "Unable to locate root naming context.";

                    if (i++ == 0)
                    {
                        LOG.error(message, ex);
                        clear();
                    }
                    else
                        throw new ApplicationRuntimeException(message, ex);
                }
            }
        }

        return _rootNamingContext;
    }

    /**
     *  Invoked after any kind of naming or remote exception.  Cached
     *  naming contexts and interfaces are discarded.
     *  
     **/

    public synchronized void clear()
    {
        _rootNamingContext = null;
        _bookQueryHome = null;
        _operationsHome = null;
    }
}
