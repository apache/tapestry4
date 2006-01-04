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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.ServiceEncoding;

/**
 * Tests for {@link org.apache.tapestry.record.PropertyPersistenceStrategySourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PropertyPersistenceStrategySourceTest extends HiveMindTestCase
{
    private PropertyPersistenceStrategy newStrategy()
    {
        return (PropertyPersistenceStrategy) newMock(PropertyPersistenceStrategy.class);
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

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("known", strategy));
        source.initializeService();

        assertSame(strategy, source.getStrategy("known"));

        verifyControls();
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

        strategy.getStoredChanges(pageName);
        setReturnValue(strategy, changes);
    }

    public void testGetAllStoredChanges()
    {
        PropertyPersistenceStrategy strategy = newStrategy();

        PropertyChange change = newChange();

        trainGetStoredChanges(strategy, "MyPage", Collections.singleton(change));

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        Collection result = source.getAllStoredChanges("MyPage");

        assertEquals(1, result.size());
        assertSame(change, result.iterator().next());

        verifyControls();
    }

    private PropertyChange newChange()
    {
        return (PropertyChange) newMock(PropertyChange.class);
    }

    public void testAddParameters()
    {
        PropertyPersistenceStrategy strategy = newStrategy();
        ServiceEncoding encoding = newEncoding();

        strategy.addParametersForPersistentProperties(encoding, false);

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        source.addParametersForPersistentProperties(encoding, false);

        verifyControls();

        strategy.addParametersForPersistentProperties(encoding, true);

        replayControls();

        source.addParametersForPersistentProperties(encoding, true);
    }

    private ServiceEncoding newEncoding()
    {
        return (ServiceEncoding) newMock(ServiceEncoding.class);
    }

    public void testDiscardStoredChanges()
    {
        PropertyPersistenceStrategy strategy = newStrategy();

        strategy.discardStoredChanges("Home");

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("known", strategy));
        source.initializeService();

        source.discardAllStoredChanged("Home");

        verifyControls();
    }
}