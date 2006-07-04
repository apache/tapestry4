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

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.test.Creator;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.record.PageRecorderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPageRecorder extends BaseComponentTestCase
{
    private ErrorLog newErrorLog()
    {
        return newMock(ErrorLog.class);
    }

    public void testGetChanges()
    {
        ErrorLog log = newErrorLog();
        
        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        Collection expected = new ArrayList();

        expect(source.getAllStoredChanges("Foo")).andReturn(expected);

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("Foo", source, log);

        Collection actual = pr.getChanges();

        assertSame(expected, actual);

        verify();
    }

    private IComponentSpecification newSpec(String propertyName, String persistence)
    {
        IComponentSpecification spec = newSpec();
        
        IPropertySpecification ps = newMock(IPropertySpecification.class);
        checkOrder(ps, false);
        
        expect(spec.getPropertySpecification(propertyName)).andReturn(ps);

        expect(ps.getPersistence()).andReturn(persistence);

        return spec;
    }

    public void testObserveChange()
    {
        ErrorLog log = newErrorLog();
        
        IPage page = newPage();

        IComponentSpecification spec = newSpec("foobar", "session");
        
        expect(page.getSpecification()).andReturn(spec);

        expect(page.getIdPath()).andReturn(null);

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        PropertyPersistenceStrategy strategy = newMock(PropertyPersistenceStrategy.class);

        expect(source.getStrategy("session")).andReturn(strategy);

        Object newValue = new Object();

        strategy.store("Foo", null, "foobar", newValue);

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("Foo", source, log);

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", newValue);

        pr.observeChange(event);

        verify();
    }

    public void testUnknownStategy()
    {
        Location l = fabricateLocation(12);
        Throwable inner = new ApplicationRuntimeException("Simulated error.");
        ErrorLog log = newErrorLog();

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        IComponent component = newComponent();
        
        IComponentSpecification spec = newSpec();
        
        IPropertySpecification ps = newMock(IPropertySpecification.class);

        expect(component.getSpecification()).andReturn(spec);

        expect(spec.getPropertySpecification("zip")).andReturn(ps);

        expect(ps.getPersistence()).andReturn("unknown");

        expect(source.getStrategy("unknown")).andThrow(inner);

        expect(ps.getLocation()).andReturn(l);

        log.error("Simulated error.", l, inner);

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("SomePage", source, log);

        assertNull(pr.findStrategy(component, "zip"));

        verify();
    }

    public void testRollbackPageProperty()
    {
        ErrorLog log = newErrorLog();

        Creator creator = new Creator();

        PageFixture page = (PageFixture) creator.newInstance(PageFixture.class);

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        PropertyChange pc = new PropertyChangeImpl(null, "cartoonName", "Dexter's Laboratory");

        expect(source.getAllStoredChanges("MyPage")).andReturn(Collections.singletonList(pc));

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", source, log);

        pr.rollback(page);

        assertEquals("Dexter's Laboratory", page.getCartoonName());

        verify();
    }

    public void testRollbackComponentProperty()
    {
        ErrorLog log = newErrorLog();
        
        IPage page = newPage();

        IComponent component = newMock(IComponent.class);

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        PropertyChange pc = new PropertyChangeImpl("fred.barney", "id", "ziff");

        expect(source.getAllStoredChanges("MyPage")).andReturn(Collections.singletonList(pc));

        expect(page.getNestedComponent("fred.barney")).andReturn(component);

        component.setId("ziff");

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", source, log);

        pr.rollback(page);

        verify();
    }

    public void testChangeWhileLocked()
    {
        ErrorLog log = newErrorLog();
        
        IPage page = newPage();

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        expect(page.getExtendedId()).andReturn("MyPage");

        log
                .error(
                        "Change to persistent property foobar of MyPage has been ignored."
                                + " Persistent properties may only be changed prior to the rendering of the response page.",
                        null,
                        null);

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", source, log);

        pr.commit();

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", new Object());

        pr.observeChange(event);

        verify();
    }

    public void testChangeToNonSpecifiedProperty()
    {
        Resource r = newResource();
        
        ErrorLog log = newErrorLog();
        
        IPage page = newPage();
        
        IComponentSpecification spec = newSpec();

        PropertyPersistenceStrategySource source = newMock(PropertyPersistenceStrategySource.class);

        expect(page.getSpecification()).andReturn(spec);

        expect(spec.getPropertySpecification("foobar")).andReturn(null);

        expect(page.getExtendedId()).andReturn("TestPage");

        expect(page.getSpecification()).andReturn(spec);

        expect(spec.getSpecificationLocation()).andReturn(r);

        log.error(
                "A property change event for property foobar of TestPage was observed, "
                        + "but no such property is identified in the specification (" + r + ").",
                null,
                null);

        replay();

        PageRecorderImpl pr = new PageRecorderImpl("TestPage", source, log);

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", new Object());

        pr.observeChange(event);

        verify();
    }
}