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

package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertSame;

import java.util.Locale;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineFactory;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.services.RequestLocaleManager;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineManagerImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestEngineManager extends BaseComponentTestCase
{

    public void testGetFromPool()
    {
        RequestLocaleManager extractor = newMock(RequestLocaleManager.class);
        
        ObjectPool pool = newMock(ObjectPool.class);

        // Training

        expect(extractor.extractLocaleForCurrentRequest()).andReturn(Locale.CHINESE);

        IEngine engine = newMock(IEngine.class);

        expect(pool.get(Locale.CHINESE)).andReturn(engine);

        replay();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);
        m.setLocaleManager(extractor);

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verify();
    }

    public void testGetNotInPool()
    {
        RequestLocaleManager extractor = newMock(RequestLocaleManager.class);
        
        ObjectPool pool = newMock(ObjectPool.class);

        // Training

        expect(extractor.extractLocaleForCurrentRequest()).andReturn(Locale.CHINESE);

        IEngine engine = newMock(IEngine.class);

        expect(pool.get(Locale.CHINESE)).andReturn(null);
        
        EngineFactory factory = newMock(EngineFactory.class);

        expect(factory.constructNewEngineInstance(Locale.CHINESE)).andReturn(engine);

        replay();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);
        m.setLocaleManager(extractor);
        m.setEngineFactory(factory);

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verify();
    }

    public void testStoreNoSession()
    {
        IEngine engine = newMock(IEngine.class);

        ObjectPool pool = newMock(ObjectPool.class);

        // Training

        expect(engine.getLocale()).andReturn(Locale.KOREAN);

        pool.store(Locale.KOREAN, engine);

        replay();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);

        m.storeEngineInstance(engine);

        verify();
    }

}