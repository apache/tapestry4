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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.order.Orderer;
import org.apache.tapestry.engine.IPropertySource;

/**
 * Orders a list of {@link org.apache.tapestry.services.impl.PropertySourceContribution}s
 * and get property values from them (the first non-null result being returned).
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class PropertySourceImpl implements IPropertySource
{
    private List _contributions;
    private List _orderedSources;
    private ErrorHandler _errorHandler;
    private Log _log;

    public void initializeService()
    {
        Orderer orderer = new Orderer(_log, _errorHandler, "property source");

        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            PropertySourceContribution c = (PropertySourceContribution) i.next();

            orderer.add(c, c.getName(), c.getAfter(), c.getBefore());
        }

        _orderedSources = orderer.getOrderedObjects();
    }

    public String getPropertyValue(String propertyName)
    {
        Iterator i = _orderedSources.iterator();

        while (i.hasNext())
        {
            IPropertySource source = (IPropertySource) i.next();

            String result = source.getPropertyValue(propertyName);

            if (result != null)
                return result;
        }

        throw new ApplicationRuntimeException(ImplMessages.noSuchGlobalProperty(propertyName));
    }

    public void setContributions(List list)
    {
        _contributions = list;
    }

    public void setErrorHandler(ErrorHandler handler)
    {
        _errorHandler = handler;
    }

    public void setLog(Log log)
    {
        _log = log;
    }

}
