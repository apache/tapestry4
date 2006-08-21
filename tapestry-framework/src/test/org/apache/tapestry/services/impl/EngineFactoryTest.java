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

import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.engine.AbstractEngine;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineFactoryImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class EngineFactoryTest extends BaseComponentTestCase
{
    public void testUseDefault()
    {
        IApplicationSpecification spec = newAppSpec();

        // Training

        trainGetEngineClassName(spec, null);

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());
        f.setDefaultEngineClassName(BaseEngine.class.getName());

        replay();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CANADA_FRENCH);

        assertTrue(result instanceof BaseEngine);
        assertEquals(Locale.CANADA_FRENCH, result.getLocale());

        verify();
    }

    private void trainGetEngineClassName(IApplicationSpecification spec, String engineClassName)
    {
        expect(spec.getEngineClassName()).andReturn(engineClassName);
    }

    private IApplicationSpecification newAppSpec()
    {
        return newMock(IApplicationSpecification.class);
    }

    public void testDefinedInSpec()
    {
        IApplicationSpecification spec = newAppSpec();

        trainGetEngineClassName(spec, EngineFixture.class.getName());

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());

        replay();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CHINESE);

        assertTrue(result instanceof EngineFixture);
        assertEquals(Locale.CHINESE, result.getLocale());

        verify();
    }

    public void testUnableToInstantiate()
    {
        IApplicationSpecification spec = newAppSpec();

        // Training

        trainGetEngineClassName(spec, AbstractEngine.class.getName());

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());

        replay();

        f.initializeService();

        try
        {
            f.constructNewEngineInstance(Locale.CHINESE);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(
                    ex,
                    "Unable to instantiate engine as instance of class org.apache.tapestry.engine.AbstractEngine");
        }

        verify();
    }

    public void testInvalidClass()
    {
        IApplicationSpecification spec = newAppSpec();

        trainGetEngineClassName(spec, "foo.XyzzYx");

        ErrorLog log = newMock(ErrorLog.class);

        log.error("Engine class 'foo.XyzzYx' not found.", null, null);

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());
        f.setErrorLog(log);
        f.setDefaultEngineClassName(BaseEngine.class.getName());

        replay();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CANADA_FRENCH);

        assertTrue(result instanceof BaseEngine);

        verify();
    }
}