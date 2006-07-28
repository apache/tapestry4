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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IScript;
import org.apache.tapestry.engine.IScriptSource;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.enhance.DeferredScriptImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestDeferredScript extends BaseComponentTestCase
{
    public void testSuccess()
    {
        IScriptSource source = newMock(IScriptSource.class);

        Resource r = newMock(Resource.class);
        IScript script = newMock(IScript.class);

        expect(source.getScript(r)).andReturn(script);

        replay();

        DeferredScript ds = new DeferredScriptImpl(r, source, null);

        assertSame(script, ds.getScript());

        // Cheating a little for code coverage. The script's resource
        // is part of ds's toString()

        assertTrue(ds.toString().indexOf(r.toString()) > 0);

        verify();
    }

    public void testFailure()
    {
        IScriptSource source = newMock(IScriptSource.class);

        Resource newResource = newMock(Resource.class);
        Resource r = newResource;

        Location l = newLocation();
        Throwable t = new RuntimeException("Woops!");

        expect(source.getScript(r)).andThrow(t);

        replay();

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

        verify();

    }
}
