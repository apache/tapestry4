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
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.record.PageRecorderImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPageRecorder extends HiveMindTestCase
{
    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private ErrorLog newLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    public void testGetChanges()
    {
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        MockControl sourcec = newControl(PropertyPersistenceStrategySource.class);
        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) sourcec
                .getMock();

        Collection expected = new ArrayList();

        source.getAllStoredChanges("Foo", cycle);
        sourcec.setReturnValue(expected);

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("Foo", cycle, source, log);

        Collection actual = pr.getChanges();

        assertSame(expected, actual);

        verifyControls();
    }

    private IComponentSpecification newSpec(String propertyName, String persistence)
    {
        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl psc = newControl(IPropertySpecification.class);
        IPropertySpecification ps = (IPropertySpecification) psc.getMock();

        spec.getPropertySpecification(propertyName);
        specc.setReturnValue(ps);

        ps.getPersistence();
        psc.setReturnValue(persistence);

        return spec;
    }

    public void testObserveChange()
    {
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        IComponentSpecification spec = newSpec("foobar", "session");

        page.getSpecification();
        pagec.setReturnValue(spec);

        page.getIdPath();
        pagec.setReturnValue(null);

        MockControl sourcec = newControl(PropertyPersistenceStrategySource.class);
        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) sourcec
                .getMock();

        PropertyPersistenceStrategy strategy = (PropertyPersistenceStrategy) newMock(PropertyPersistenceStrategy.class);

        source.getStrategy("session");
        sourcec.setReturnValue(strategy);

        Object newValue = new Object();

        strategy.store("Foo", null, "foobar", newValue);

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("Foo", cycle, source, log);

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", newValue);

        pr.observeChange(event);

        verifyControls();
    }

    public void testUnknownStategy()
    {
        Location l = fabricateLocation(12);
        Throwable inner = new ApplicationRuntimeException("Simulated error.");
        ErrorLog log = newLog();

        IRequestCycle cycle = newCycle();

        MockControl sourcec = newControl(PropertyPersistenceStrategySource.class);
        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) sourcec
                .getMock();

        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        MockControl psc = newControl(IPropertySpecification.class);
        IPropertySpecification ps = (IPropertySpecification) psc.getMock();

        component.getSpecification();
        componentc.setReturnValue(spec);

        spec.getPropertySpecification("zip");
        specc.setReturnValue(ps);

        ps.getPersistence();
        psc.setReturnValue("unknown");

        source.getStrategy("unknown");
        sourcec.setThrowable(inner);

        ps.getLocation();
        psc.setReturnValue(l);

        log.error("Simulated error.", l, inner);

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("SomePage", cycle, source, log);

        assertNull(pr.findStrategy(component, "zip"));

        verifyControls();
    }

    public void testRollbackPageProperty()
    {
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        Creator creator = new Creator();

        PageFixture page = (PageFixture) creator.newInstance(PageFixture.class);

        MockControl sourcec = newControl(PropertyPersistenceStrategySource.class);
        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) sourcec
                .getMock();

        PropertyChange pc = new PropertyChangeImpl(null, "cartoonName", "Dexter's Laboratory");

        source.getAllStoredChanges("MyPage", cycle);
        sourcec.setReturnValue(Collections.singletonList(pc));

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", cycle, source, log);

        pr.rollback(page);

        assertEquals("Dexter's Laboratory", page.getCartoonName());

        verifyControls();
    }

    public void testRollbackComponentProperty()
    {
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        IComponent component = (IComponent) newMock(IComponent.class);

        MockControl sourcec = newControl(PropertyPersistenceStrategySource.class);
        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) sourcec
                .getMock();

        PropertyChange pc = new PropertyChangeImpl("fred.barney", "id", "ziff");

        source.getAllStoredChanges("MyPage", cycle);
        sourcec.setReturnValue(Collections.singletonList(pc));

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(component);

        component.setId("ziff");

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", cycle, source, log);

        pr.rollback(page);

        verifyControls();
    }

    public void testChangeWhileLocked()
    {
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) newMock(PropertyPersistenceStrategySource.class);

        page.getExtendedId();
        pagec.setReturnValue("MyPage");

        log
                .error(
                        "Change to persistent property foobar of MyPage has been ignored."
                                + " Persistent properties may only be changed prior to the rendering of the response page.",
                        null,
                        null);

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("MyPage", cycle, source, log);

        pr.commit();

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", new Object());

        pr.observeChange(event);

        verifyControls();
    }

    public void testChangeToNonSpecifiedProperty()
    {
        Resource r = fabricateLocation(99).getResource();
        IRequestCycle cycle = newCycle();
        ErrorLog log = newLog();

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        PropertyPersistenceStrategySource source = (PropertyPersistenceStrategySource) newMock(PropertyPersistenceStrategySource.class);

        page.getSpecification();
        pagec.setReturnValue(spec);

        spec.getPropertySpecification("foobar");
        specc.setReturnValue(null);

        page.getExtendedId();
        pagec.setReturnValue("TestPage");

        page.getSpecification();
        pagec.setReturnValue(spec);

        spec.getSpecificationLocation();
        specc.setReturnValue(r);

        log.error(
                "A property change event for property foobar of TestPage was observed, "
                        + "but no such property is identified in the specification (" + r + ").",
                null,
                null);

        replayControls();

        PageRecorderImpl pr = new PageRecorderImpl("TestPage", cycle, source, log);

        ObservedChangeEvent event = new ObservedChangeEvent(page, "foobar", new Object());

        pr.observeChange(event);

        verifyControls();
    }
}