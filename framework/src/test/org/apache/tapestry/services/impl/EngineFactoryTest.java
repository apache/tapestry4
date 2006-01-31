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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.engine.AbstractEngine;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineFactoryImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class EngineFactoryTest extends HiveMindTestCase
{
    public void testUseDefault()
    {
        IApplicationSpecification spec = newSpec();

        // Training

        trainGetEngineClassName(spec, null);

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());
        f.setDefaultEngineClassName(BaseEngine.class.getName());

        replayControls();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CANADA_FRENCH);

        assertTrue(result instanceof BaseEngine);
        assertEquals(Locale.CANADA_FRENCH, result.getLocale());

        verifyControls();
    }

    private void trainGetEngineClassName(IApplicationSpecification spec, String engineClassName)
    {
        spec.getEngineClassName();
        setReturnValue(spec, engineClassName);
    }

    private IApplicationSpecification newSpec()
    {
        return (IApplicationSpecification) newMock(IApplicationSpecification.class);
    }

    public void testDefinedInSpec()
    {
        IApplicationSpecification spec = newSpec();

        trainGetEngineClassName(spec, EngineFixture.class.getName());

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());

        replayControls();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CHINESE);

        assertTrue(result instanceof EngineFixture);
        assertEquals(Locale.CHINESE, result.getLocale());

        verifyControls();
    }

    public void testUnableToInstantiate()
    {
        IApplicationSpecification spec = newSpec();

        // Training

        trainGetEngineClassName(spec, AbstractEngine.class.getName());

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());

        replayControls();

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

        verifyControls();
    }

    public void testInvalidClass()
    {
        IApplicationSpecification spec = newSpec();

        trainGetEngineClassName(spec, "foo.XyzzYx");

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        log.error("Engine class 'foo.XyzzYx' not found.", null, null);

        EngineFactoryImpl f = new EngineFactoryImpl();

        f.setApplicationSpecification(spec);
        f.setClassResolver(new DefaultClassResolver());
        f.setErrorLog(log);
        f.setDefaultEngineClassName(BaseEngine.class.getName());

        replayControls();

        f.initializeService();

        IEngine result = f.constructNewEngineInstance(Locale.CANADA_FRENCH);

        assertTrue(result instanceof BaseEngine);

        verifyControls();
    }
}
