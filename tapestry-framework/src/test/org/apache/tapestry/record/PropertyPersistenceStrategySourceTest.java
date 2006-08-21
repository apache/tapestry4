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

import static org.easymock.EasyMock.expect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.record.PropertyPersistenceStrategySourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class PropertyPersistenceStrategySourceTest extends BaseComponentTestCase
{
    private PropertyPersistenceStrategy newStrategy()
    {
        return newMock(PropertyPersistenceStrategy.class);
    }

    private List newContributions(String name, PropertyPersistenceStrategy strategy)
    {
        PropertyPersistenceStrategyContribution c = new PropertyPersistenceStrategyContribution();
        c.setName(name);
        c.setStrategy(strategy);

        return Collections.singletonList(c);
    }

    public void testGetKnownStrategy()
    {
        PropertyPersistenceStrategy strategy = newStrategy();

        replay();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("known", strategy));
        source.initializeService();

        assertSame(strategy, source.getStrategy("known"));

        verify();
    }

    public void testGetUnknownStrategy()
    {
        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(Collections.EMPTY_LIST);

        try
        {
            source.getStrategy("unknown");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(RecordMessages.unknownPersistenceStrategy("unknown"), ex.getMessage());
        }
    }

    protected void trainGetStoredChanges(PropertyPersistenceStrategy strategy, String pageName,
            Collection changes)
    {
        expect(strategy.getStoredChanges(pageName)).andReturn(changes);
    }

    public void testGetAllStoredChanges()
    {
        PropertyPersistenceStrategy strategy = newStrategy();

        PropertyChange change = newChange();

        trainGetStoredChanges(strategy, "MyPage", Collections.singleton(change));

        replay();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        Collection result = source.getAllStoredChanges("MyPage");

        assertEquals(1, result.size());
        assertSame(change, result.iterator().next());

        verify();
    }

    private PropertyChange newChange()
    {
        return newMock(PropertyChange.class);
    }

    public void testAddParameters()
    {
        PropertyPersistenceStrategy strategy = newStrategy();
        ServiceEncoding encoding = newEncoding();

        strategy.addParametersForPersistentProperties(encoding, false);

        replay();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        source.addParametersForPersistentProperties(encoding, false);

        verify();

        strategy.addParametersForPersistentProperties(encoding, true);

        replay();

        source.addParametersForPersistentProperties(encoding, true);
    }

    private ServiceEncoding newEncoding()
    {
        return newMock(ServiceEncoding.class);
    }

    public void testDiscardStoredChanges()
    {
        PropertyPersistenceStrategy strategy = newStrategy();

        strategy.discardStoredChanges("Home");

        replay();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("known", strategy));
        source.initializeService();

        source.discardAllStoredChanged("Home");

        verify();
    }
}