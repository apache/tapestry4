// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.order.Orderer;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.util.DelegatingPropertySource;

/**
 * Orders a list of {@link org.apache.tapestry.services.impl.PropertySourceContribution}s and get
 * property values from them (the first non-null result being returned).
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class PropertySourceImpl implements IPropertySource
{
    private List _contributions;

    private IPropertySource _delegate;

    private ErrorLog _errorLog;

    public void initializeService()
    {
        Orderer orderer = new Orderer(_errorLog, "property source");

        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            PropertySourceContribution c = (PropertySourceContribution) i.next();

            orderer.add(c, c.getName(), c.getAfter(), c.getBefore());
        }

        List ordered = orderer.getOrderedObjects();

        DelegatingPropertySource delegate = new DelegatingPropertySource();

        i = ordered.iterator();
        while (i.hasNext())
        {
            PropertySourceContribution c = (PropertySourceContribution) i.next();

            delegate.addSource(c.getSource());
        }

        _delegate = delegate;
    }

    public String getPropertyValue(String propertyName)
    {
        return _delegate.getPropertyValue(propertyName);
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setContributions(List list)
    {
        _contributions = list;
    }

}