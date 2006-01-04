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

import java.util.Locale;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineFactory;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.services.RequestLocaleManager;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineManagerImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestEngineManager extends HiveMindTestCase
{

    public void testGetFromPool()
    {

        MockControl extractorControl = newControl(RequestLocaleManager.class);
        RequestLocaleManager extractor = (RequestLocaleManager) extractorControl.getMock();

        MockControl poolControl = newControl(ObjectPool.class);
        ObjectPool pool = (ObjectPool) poolControl.getMock();

        // Training

        extractor.extractLocaleForCurrentRequest();
        extractorControl.setReturnValue(Locale.CHINESE);

        IEngine engine = (IEngine) newMock(IEngine.class);

        pool.get(Locale.CHINESE);
        poolControl.setReturnValue(engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);
        m.setLocaleManager(extractor);

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verifyControls();
    }

    public void testGetNotInPool()
    {

        MockControl extractorControl = newControl(RequestLocaleManager.class);
        RequestLocaleManager extractor = (RequestLocaleManager) extractorControl.getMock();

        MockControl poolControl = newControl(ObjectPool.class);
        ObjectPool pool = (ObjectPool) poolControl.getMock();

        // Training

        extractor.extractLocaleForCurrentRequest();
        extractorControl.setReturnValue(Locale.CHINESE);

        IEngine engine = (IEngine) newMock(IEngine.class);

        pool.get(Locale.CHINESE);
        poolControl.setReturnValue(null);

        MockControl factoryControl = newControl(EngineFactory.class);
        EngineFactory factory = (EngineFactory) factoryControl.getMock();

        factory.constructNewEngineInstance(Locale.CHINESE);
        factoryControl.setReturnValue(engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);
        m.setLocaleManager(extractor);
        m.setEngineFactory(factory);

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verifyControls();
    }

    public void testStoreNoSession()
    {

        MockControl engineControl = newControl(IEngine.class);
        IEngine engine = (IEngine) engineControl.getMock();

        ObjectPool pool = (ObjectPool) newMock(ObjectPool.class);

        // Training

        engine.getLocale();
        engineControl.setReturnValue(Locale.KOREAN);

        pool.store(Locale.KOREAN, engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setEnginePool(pool);

        m.storeEngineInstance(engine);

        verifyControls();
    }

}