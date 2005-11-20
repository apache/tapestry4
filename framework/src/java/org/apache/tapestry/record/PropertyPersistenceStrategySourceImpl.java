// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.engine.ServiceEncoding;

/**
 * Implementation of the <code>tapestry.persist.PropertyPersistenceStrategySource</code> service.
 * Allows access to other services, that implement the
 * {@link org.apache.tapestry.record.PropertyPersistenceStrategy} interface.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PropertyPersistenceStrategySourceImpl implements PropertyPersistenceStrategySource
{
    // Set from tapestry.props.PersistenceStrategy
    private List _contributions;

    private Map _strategies = new HashMap();

    public void initializeService()
    {
        Iterator i = _contributions.iterator();
        while (i.hasNext())
        {
            PropertyPersistenceStrategyContribution c = (PropertyPersistenceStrategyContribution) i
                    .next();

            _strategies.put(c.getName(), c.getStrategy());
        }
    }

    public PropertyPersistenceStrategy getStrategy(String name)
    {
        if (!_strategies.containsKey(name))
            throw new ApplicationRuntimeException(RecordMessages.unknownPersistenceStrategy(name));

        return (PropertyPersistenceStrategy) _strategies.get(name);
    }

    public Collection getAllStoredChanges(String pageName)
    {
        Collection result = new ArrayList();

        Iterator i = _strategies.values().iterator();

        while (i.hasNext())
        {
            PropertyPersistenceStrategy s = (PropertyPersistenceStrategy) i.next();

            result.addAll(s.getStoredChanges(pageName));
        }

        return result;
    }

    public void discardAllStoredChanged(String pageName)
    {
        Iterator i = _strategies.values().iterator();

        while (i.hasNext())
        {
            PropertyPersistenceStrategy s = (PropertyPersistenceStrategy) i.next();

            s.discardStoredChanges(pageName);
        }
    }

    public void addParametersForPersistentProperties(ServiceEncoding encoding, boolean post)
    {
        Iterator i = _strategies.values().iterator();

        while (i.hasNext())
        {
            PropertyPersistenceStrategy s = (PropertyPersistenceStrategy) i.next();

            s.addParametersForPersistentProperties(encoding, post);
        }
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }
}