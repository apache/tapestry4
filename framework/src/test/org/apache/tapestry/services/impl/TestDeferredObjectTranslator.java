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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.DeferredObjectImpl}&nbsp;and
 * {@link org.apache.tapestry.services.impl.DeferredObjectTranslator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestDeferredObjectTranslator extends HiveMindTestCase
{
    private Module newModule()
    {
        return (Module) newMock(Module.class);
    }

    private Translator newTranslator(Module module, String objectReference, Location location,
            Object result)
    {
        MockControl control = newControl(Translator.class);
        Translator translator = (Translator) control.getMock();

        translator.translate(module, Object.class, objectReference, location);
        control.setReturnValue(result);

        return translator;
    }

    public void testDeferredObject()
    {
        Object object = new Object();
        Module module = newModule();
        Location l = newLocation();
        Translator translator = newTranslator(module, "OBJ-REFERENCE", l, object);

        replayControls();

        DeferredObject deferred = new DeferredObjectImpl(translator, module, "OBJ-REFERENCE", l);

        assertSame(object, deferred.getObject());

        // Check that a second call returns a cached value.

        assertSame(object, deferred.getObject());

        verifyControls();

        assertSame(l, deferred.getLocation());
    }

    public void testDeferredObjectTranslator()
    {
        Object object = new Object();
        Module module = newModule();
        Location l = newLocation();
        Translator objectTranslator = newTranslator(module, "OBJ-REFERENCE", l, object);

        replayControls();

        DeferredObjectTranslator translator = new DeferredObjectTranslator();
        translator.setObjectTranslator(objectTranslator);

        DeferredObject deferred = (DeferredObject) translator.translate(
                module,
                Object.class,
                "OBJ-REFERENCE",
                l);

        assertSame(object, deferred.getObject());

        // Check that a second call returns a cached value.

        assertSame(object, deferred.getObject());

        verifyControls();

        assertSame(l, deferred.getLocation());
    }
}