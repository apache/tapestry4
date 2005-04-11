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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.record.PropertyPersistenceStrategySourceImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPropertyPersistenceStrategySource extends HiveMindTestCase
{
    private PropertyPersistenceStrategy newStrategy()
    {
        return (PropertyPersistenceStrategy) newMock(PropertyPersistenceStrategy.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
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

    public void testGetAllStoredChanges()
    {
        IRequestCycle cycle = newCycle();

        MockControl control = newControl(PropertyPersistenceStrategy.class);
        PropertyPersistenceStrategy strategy = (PropertyPersistenceStrategy) control.getMock();

        PropertyChange change = (PropertyChange) newMock(PropertyChange.class);

        strategy.getStoredChanges("MyPage", cycle);
        control.setReturnValue(Collections.singleton(change));

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        Collection result = source.getAllStoredChanges("MyPage", cycle);

        assertEquals(1, result.size());
        assertSame(change, result.iterator().next());

        verifyControls();
    }

    public void testAddParameters()
    {
        IRequestCycle cycle = newCycle();
        PropertyPersistenceStrategy strategy = newStrategy();
        ServiceEncoding encoding = (ServiceEncoding) newMock(ServiceEncoding.class);

        strategy.addParametersForPersistentProperties(encoding, cycle);

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("whatever", strategy));
        source.initializeService();

        source.addParametersForPersistentProperties(encoding, cycle);

        verifyControls();
    }

    public void testDiscardStoredChanges()
    {
        IRequestCycle cycle = newCycle();
        PropertyPersistenceStrategy strategy = newStrategy();

        strategy.discardStoredChanges("Home", cycle);

        replayControls();

        PropertyPersistenceStrategySourceImpl source = new PropertyPersistenceStrategySourceImpl();
        source.setContributions(newContributions("known", strategy));
        source.initializeService();

        source.discardAllStoredChanged("Home", cycle);

        verifyControls();
    }
}