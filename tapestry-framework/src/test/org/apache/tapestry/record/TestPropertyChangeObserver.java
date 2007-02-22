// Copyright 2004, 2005 The Apache Software Foundation
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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.dojo.form.DefaultAutocompleteModel;
import org.apache.tapestry.dojo.form.IAutocompleteModel;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.testng.annotations.Test;


/**
 * Tests watching persistent object properties via proxy objects.
 */
@Test
public class TestPropertyChangeObserver extends BaseComponentTestCase
{
    static final long NANOSECONDS_IN_MILLIS = 1000000;
    
    class CglibChangeObserver implements ChangeObserver {

        int _changes = 0;
        
        public int getChanges()
        {
            return _changes;
        }
        
        public boolean isLocked()
        {
            return false;
        }
        
        /**
         * {@inheritDoc}
         */
        public void observeChange(ObservedChangeEvent event)
        {
            _changes++;
        }
    }
    
    public void test_Null_Property_Proxy() 
    throws Exception
    {
        PropertyChangeObserver propObserver = new CglibProxiedPropertyChangeObserverImpl();
        IPage page = newMock(IPage.class);
        
        replay();
        
        assert propObserver.observePropertyChanges(page, null, "state") == null;
        
        verify();
    }
    
    void callState(SimpleState o1, SimpleState o2)
    {
        o1.getDate();
        o2.getDate();
    }
    
    public void test_Proxy_Access_Changes() 
    throws Exception
    {
        SimpleState rawState = new SimpleState();
        CglibChangeObserver pageObserver = new CglibChangeObserver();
        PropertyChangeObserver propObserver = new CglibProxiedPropertyChangeObserverImpl();
        
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        expect(page.getPage()).andReturn(page).anyTimes();
        expect(page.getIdPath()).andReturn(null).anyTimes();
        expect(page.getChangeObserver()).andReturn(pageObserver).anyTimes();
        
        replay();
        
        SimpleState state = (SimpleState) propObserver.observePropertyChanges(page, rawState, "state");
        
        // test that same instance is returned unmolested
        assert state == (SimpleState) propObserver.observePropertyChanges(page, state, "state");
        
        assert state != null;
        assert state.getKeys().size() == 0;
        assert SimpleState.class.isAssignableFrom(state.getClass());
        
        assertEquals(pageObserver.getChanges(), 0);
        
        for (int i=0; i < 40; i++) {
            callState(rawState, state);
        }
        
        assertEquals(pageObserver.getChanges(), 0);
        
        long otime = System.nanoTime();
        rawState.getDate();
        otime = System.nanoTime() - otime;
        
        long etime = System.nanoTime();
        state.getDate();
        etime = System.nanoTime() - etime;
        
        long otimems = otime / NANOSECONDS_IN_MILLIS;
        long etimems = etime / NANOSECONDS_IN_MILLIS;
        
        assertEquals(otimems, etimems, "Enhanced property access times not equal to normal access times: \n"
                + "original time: " + otime + "ns : " + otimems + " ms , "
                + "enhanced time: " + etime + "ns : " + etimems + " ms");
        
        verify();
    }
    
    void setState(SimpleState raw, SimpleState enhanced, Date date) 
    {
        raw.setDate(date);
        enhanced.setDate(date);
    }
    
    public void test_Proxy_Set_Changes() 
    throws Exception
    {
        SimpleState rawState = new SimpleState();
        CglibChangeObserver pageObserver = new CglibChangeObserver();
        PropertyChangeObserver propObserver = new CglibProxiedPropertyChangeObserverImpl();
        
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        expect(page.getPage()).andReturn(page).anyTimes();
        expect(page.getIdPath()).andReturn(null).anyTimes();
        expect(page.getChangeObserver()).andReturn(pageObserver).anyTimes();
        
        replay();
        
        SimpleState state = (SimpleState) propObserver.observePropertyChanges(page, rawState, "state");
        
        assertEquals(pageObserver.getChanges(), 0);
        
        Date testDate = new Date();
        for (int i=0; i < 2000; i++) {
            setState(rawState, state, testDate);
        }
        
        assertEquals(pageObserver.getChanges(), 2000);
        
        long otime = System.nanoTime();
        rawState.setDate(new Date());
        otime = System.nanoTime() - otime;
        
        long etime = System.nanoTime();
        state.setDate(new Date());
        etime = System.nanoTime() - etime;
        
        long otimems = otime / NANOSECONDS_IN_MILLIS;
        long etimems = etime / NANOSECONDS_IN_MILLIS;
        
        assertEquals(otimems, etimems, "Enhanced property set times not equal to normal set times: \n"
                + "original setDate() time: " + otime + "ns : " + otimems + " ms , "
                + "enhanced setDate() time: " + etime + "ns : " + etimems + " ms");
        
        verify();
    }
    
    public void test_Proxy_Class_Cached()
    {
        SimpleState rawState = new SimpleState();
        CglibChangeObserver pageObserver = new CglibChangeObserver();
        PropertyChangeObserver propObserver = new CglibProxiedPropertyChangeObserverImpl();
        
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        expect(page.getPage()).andReturn(page).anyTimes();
        
        expect(page.getIdPath()).andReturn(null).anyTimes();
        
        expect(page.getChangeObserver()).andReturn(pageObserver).anyTimes();
        
        replay();
        
        SimpleState state = (SimpleState) propObserver.observePropertyChanges(page, rawState, "fooBar");
        
        SimpleState state2 = (SimpleState) propObserver.observePropertyChanges(page, state, "fooBar");
        
        assert state == state2;
        
        SimpleState newState = new SimpleState();
        
        SimpleState enewState = (SimpleState) propObserver.observePropertyChanges(page, newState, "fooBar");
        
        assert enewState != state;
        
        assert enewState != newState;
        
        SimpleState newState2 = (SimpleState) propObserver.observePropertyChanges(page, newState, "fooBar");
        
        assert Enhancer.isEnhanced(state.getClass());
        assert Enhancer.isEnhanced(state.getClass());
        assert Enhancer.isEnhanced(state2.getClass());
        assert !Enhancer.isEnhanced(newState.getClass());
        assert Enhancer.isEnhanced(enewState.getClass());
        assert Enhancer.isEnhanced(newState2.getClass());
        
        assert newState2 != enewState;
        assert newState2.getClass() == enewState.getClass();
        assert state.getClass() == state2.getClass();
        assert state.getClass() != newState.getClass();
        assert enewState.getClass() == state2.getClass();
        
        assert ObservedProperty.class.isAssignableFrom(state.getClass());
        
        SimpleState preEnhanced = (SimpleState)((ObservedProperty)state).getCGProperty();
        
        assert preEnhanced != null;
        assert !ObservedProperty.class.isAssignableFrom(preEnhanced.getClass());
        
        verify();
    }
    
    public void test_Proxy_Values_Equal()
    {
        CglibChangeObserver pageObserver = new CglibChangeObserver();
        PropertyChangeObserver propObserver = new CglibProxiedPropertyChangeObserverImpl();
        
        IPage page = newMock(IPage.class);
        checkOrder(page, false);
        
        expect(page.getPage()).andReturn(page).anyTimes();
        expect(page.getIdPath()).andReturn(null).anyTimes();
        expect(page.getChangeObserver()).andReturn(pageObserver).anyTimes();
        
        replay();
        
        List list = new ArrayList();
        list.add(new SimpleState(1, "First"));
        list.add(new SimpleState(2, "Second"));
        list.add(new SimpleState(3, "Third"));
        
        IAutocompleteModel model = new DefaultAutocompleteModel( list , "id", "name");
        
        SimpleState selected = (SimpleState)list.get(1);
        
        assertEquals(model.getPrimaryKey(selected), Long.valueOf(2));
        assertEquals(model.getLabelFor(selected), "Second");
        
        assertEquals(selected.getId(), 2);
        
        SimpleState sel = (SimpleState)propObserver.observePropertyChanges(page, selected, "foo");
        
        assertEquals(sel.getId(), selected.getId());
        
        assertEquals(model.getPrimaryKey(sel), Long.valueOf(2));
        assertEquals(model.getLabelFor(sel), "Second");
        
        verify();
    }
}
