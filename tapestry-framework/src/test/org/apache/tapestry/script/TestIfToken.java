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

package org.apache.tapestry.script;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests {@link org.apache.tapestry.script.IfToken}, the basis for the &lt;if&gt; and
 * &lt;if-not&gt; elements.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestIfToken extends BaseComponentTestCase
{
    public void testEvaluateTrue()
    {
        ScriptSession s = newMock(ScriptSession.class);

        IScriptToken nested = newMock(IScriptToken.class);

        expect(s.evaluate("EXPRESSION", Boolean.class)).andReturn(Boolean.TRUE);

        StringBuffer buffer = new StringBuffer();

        nested.write(buffer, s);

        replay();

        IfToken t = new IfToken(true, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verify();
    }

    public void testEvaluateFalse()
    {
        ScriptSession s = newMock(ScriptSession.class);

        IScriptToken nested = newMock(IScriptToken.class);

        expect(s.evaluate("EXPRESSION", Boolean.class)).andReturn(Boolean.FALSE);

        StringBuffer buffer = new StringBuffer();

        replay();

        IfToken t = new IfToken(true, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verify();
    }

    public void testEvaluateMatch()
    {
        ScriptSession s = newMock(ScriptSession.class);

        IScriptToken nested = newMock(IScriptToken.class);

        expect(s.evaluate("EXPRESSION", Boolean.class)).andReturn(Boolean.FALSE);

        StringBuffer buffer = new StringBuffer();

        nested.write(buffer, s);

        replay();

        IfToken t = new IfToken(false, "EXPRESSION", null);
        t.addToken(nested);

        t.write(buffer, s);

        verify();
    }

    public void testEvaluateFailure()
    {
        Location l = fabricateLocation(8);
        Throwable inner = new ApplicationRuntimeException("Simulated error.");

        ScriptSession s = newMock(ScriptSession.class);

        expect(s.evaluate("EXPRESSION", Boolean.class)).andThrow(inner);

        replay();

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