//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
