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

package org.apache.tapestry.services.impl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.order.Orderer;
import org.apache.tapestry.services.ApplicationInitializer;

/**
 * Uses an orderable list of {@link org.apache.tapestry.services.ApplicationInitializer}s
 * to initialize the application. 
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class MasterInitializer implements ApplicationInitializer
{
    private Log _log;
    private ErrorHandler _errorHandler;
    private List _initializers;

    public void initializeService()
    {
        Orderer o = new Orderer(_log, _errorHandler, ImplMessages.initializerContribution());

        Iterator i = _initializers.iterator();
        while (i.hasNext())
        {
            InitializerContribution c = (InitializerContribution) i.next();

            o.add(c, c.getName(), c.getAfter(), c.getBefore());
        }

        _initializers = o.getOrderedObjects();
    }

    public void initialize(HttpServlet servlet)
    {
        Iterator i = _initializers.iterator();
        while (i.hasNext())
        {
            InitializerContribution c = (InitializerContribution) i.next();

            c.initialize(servlet);
        }
    }

    public void setErrorHandler(ErrorHandler handler)
    {
        _errorHandler = handler;
    }

    public void setInitializers(List list)
    {
        _initializers = list;
    }

    public void setLog(Log log)
    {
        _log = log;
    }

}
