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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineFactory;
import org.apache.tapestry.services.LocaleExtractor;
import org.apache.tapestry.services.ObjectPool;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineManagerImpl}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestEngineManager extends HiveMindTestCase
{
    public void testGetFromSession()
    {
        IEngine engine = (IEngine) newMock(IEngine.class);

        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        MockControl sessionControl = newControl(HttpSession.class);
        HttpSession session = (HttpSession) sessionControl.getMock();

        // Training

        request.getSession(false);
        requestControl.setReturnValue(session);

        session.getAttribute(EngineManagerImpl.ENGINE_KEY_PREFIX + "george");
        sessionControl.setReturnValue(engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setServletName("george");
        m.setRequest(request);

        m.initializeService();

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verifyControls();
    }

    public void testGetFromPool()
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        MockControl extractorControl = newControl(LocaleExtractor.class);
        LocaleExtractor extractor = (LocaleExtractor) extractorControl.getMock();

        MockControl poolControl = newControl(ObjectPool.class);
        ObjectPool pool = (ObjectPool) poolControl.getMock();

        // Training

        request.getSession(false);
        requestControl.setReturnValue(null);

        extractor.extractLocaleForCurrentRequest();
        extractorControl.setReturnValue(Locale.CHINESE);

        IEngine engine = (IEngine) newMock(IEngine.class);

        pool.get(Locale.CHINESE);
        poolControl.setReturnValue(engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setServletName("george");
        m.setRequest(request);
        m.setEnginePool(pool);
        m.setLocaleExtractor(extractor);

        m.initializeService();

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verifyControls();
    }

    public void testGetNotInPool()
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        MockControl extractorControl = newControl(LocaleExtractor.class);
        LocaleExtractor extractor = (LocaleExtractor) extractorControl.getMock();

        MockControl poolControl = newControl(ObjectPool.class);
        ObjectPool pool = (ObjectPool) poolControl.getMock();

        // Training

        request.getSession(false);
        requestControl.setReturnValue(null);

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

        m.setServletName("george");
        m.setRequest(request);
        m.setEnginePool(pool);
        m.setLocaleExtractor(extractor);
        m.setEngineFactory(factory);

        m.initializeService();

        IEngine actual = m.getEngineInstance();

        assertSame(engine, actual);

        verifyControls();
    }

    public void testStoreNoSession()
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        MockControl engineControl = newControl(IEngine.class);
        IEngine engine = (IEngine) engineControl.getMock();

        ObjectPool pool = (ObjectPool) newMock(ObjectPool.class);

        // Training

        request.getSession(false);
        requestControl.setReturnValue(null);

        engine.getLocale();
        engineControl.setReturnValue(Locale.KOREAN);

        pool.store(Locale.KOREAN, engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setServletName("george");
        m.setRequest(request);
        m.setEnginePool(pool);

        m.initializeService();

        m.storeEngineInstance(engine);

        verifyControls();
    }

    public void setStoreIntoSession()
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        HttpSession session = (HttpSession) newMock(HttpSession.class);

        MockControl engineControl = newControl(IEngine.class);
        IEngine engine = (IEngine) engineControl.getMock();

        // Training

        request.getSession(false);
        requestControl.setReturnValue(session);

        engine.getLocale();
        engineControl.setReturnValue(Locale.KOREAN);

        session.setAttribute(EngineManagerImpl.ENGINE_KEY_PREFIX + "george", engine);

        replayControls();

        EngineManagerImpl m = new EngineManagerImpl();

        m.setServletName("george");
        m.setRequest(request);

        m.initializeService();

        m.storeEngineInstance(engine);

        verifyControls();
    }
}
