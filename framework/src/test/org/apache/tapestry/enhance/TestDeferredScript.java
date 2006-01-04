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

package org.apache.tapestry.enhance;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.DeferredScriptImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDeferredScript extends HiveMindTestCase
{
    public void testSuccess()
    {
        MockControl control = newControl(IScriptSource.class);
        IScriptSource source = (IScriptSource) control.getMock();

        Resource r = (Resource) newMock(Resource.class);
        IScript script = (IScript) newMock(IScript.class);

        source.getScript(r);
        control.setReturnValue(script);

        replayControls();

        DeferredScript ds = new DeferredScriptImpl(r, source, null);

        assertSame(script, ds.getScript());

        // Cheating a little for code coverage. The script's resource
        // is part of ds's toString()

        assertTrue(ds.toString().indexOf(r.toString()) > 0);

        verifyControls();
    }

    public void testFailure()
    {
        MockControl control = newControl(IScriptSource.class);
        IScriptSource source = (IScriptSource) control.getMock();

        Resource newResource = (Resource) newMock(Resource.class);
        Resource r = newResource;

        Location l = newLocation();
        Throwable t = new RuntimeException("Woops!");

        source.getScript(r);
        control.setThrowable(t);

        replayControls();

        DeferredScript ds = new DeferredScriptImpl(r, source, l);

        try
        {
            ds.getScript();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Woops!", ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(t, ex.getRootCause());
        }

        verifyControls();

    }
}
