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

package org.apache.tapestry.script;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests {@link org.apache.tapestry.script.IfToken}, the basis for the &lt;if&gt; and
 * &lt;if-not&gt; elements.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestIfToken extends HiveMindTestCase
{
    public void testEvaluateTrue()
    {
        MockControl sc = newControl(ScriptSession.class);
        ScriptSession s = (ScriptSession) sc.getMock();

        IScriptToken nested = (IScriptToken) newMock(IScriptToken.class);

        s.evaluate("EXPRESSION", Boolean.class);
        sc.setReturnValue(Boolean.TRUE);

        StringBuffer buffer = new StringBuffer();

        nested.write(buffer, s);

        replayControls();

        IfToken t = new IfToken(true, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verifyControls();
    }

    public void testEvaluateFalse()
    {
        MockControl sc = newControl(ScriptSession.class);
        ScriptSession s = (ScriptSession) sc.getMock();

        IScriptToken nested = (IScriptToken) newMock(IScriptToken.class);

        s.evaluate("EXPRESSION", Boolean.class);
        sc.setReturnValue(Boolean.FALSE);

        StringBuffer buffer = new StringBuffer();

        replayControls();

        IfToken t = new IfToken(true, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verifyControls();
    }

    public void testEvaluateMatch()
    {
        MockControl sc = newControl(ScriptSession.class);
        ScriptSession s = (ScriptSession) sc.getMock();

        IScriptToken nested = (IScriptToken) newMock(IScriptToken.class);

        s.evaluate("EXPRESSION", Boolean.class);
        sc.setReturnValue(Boolean.FALSE);

        StringBuffer buffer = new StringBuffer();

        nested.write(buffer, s);

        replayControls();

        IfToken t = new IfToken(false, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verifyControls();
    }

    public void testEvaluateFailure()
    {
        Location l = fabricateLocation(8);
        Throwable inner = new ApplicationRuntimeException("Simulated error.");

        MockControl sc = newControl(ScriptSession.class);
        ScriptSession s = (ScriptSession) sc.getMock();

        s.evaluate("EXPRESSION", Boolean.class);
        sc.setThrowable(inner);

        replayControls();

        IfToken t = new IfToken(false, "EXPRESSION", l);

        try
        {
            t.write(null, s);
            
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(inner.getMessage(), ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(inner, ex.getRootCause());
        }
    }
}