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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests {@link org.apache.tapestry.script.ForeachToken}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential=true)
public class TestForeachToken extends BaseComponentTestCase
{
    private static class EchoToken extends AbstractToken
    {
        private String _key;

        public EchoToken(String key)
        {
            super(null);

            _key = key;
        }

        public void write(StringBuffer buffer, ScriptSession session)
        {
            Object value = session.getSymbols().get(_key);

            buffer.append(value);
            buffer.append("\n");
        }
    }

    public void testFull()
    {
        Map symbols = new HashMap();
        
        ScriptSession s = newMock(ScriptSession.class);

        StringBuffer buffer = new StringBuffer();

        Iterator i = Arrays.asList(new String[]
        { "buffy", "angel" }).iterator();

        expect(s.evaluate("EXPRESSION", Iterator.class)).andReturn(i);

        expect(s.getSymbols()).andReturn(symbols).times(5);

        replay();

        ForeachToken t = new ForeachToken("value", "index", "EXPRESSION", null);
        t.addToken(new EchoToken("value"));
        t.addToken(new EchoToken("index"));

        t.write(buffer, s);

        verify();

        assertEquals("buffy\n0\nangel\n1\n", buffer.toString());

        assertEquals("angel", symbols.get("value"));
        assertEquals("1", symbols.get("index"));
    }

    public void testNoIndex()
    {
        Map symbols = new HashMap();

        symbols.put("index", "none");

        ScriptSession s = newMock(ScriptSession.class);

        StringBuffer buffer = new StringBuffer();

        Iterator i = Arrays.asList(new String[]
        { "buffy", "angel" }).iterator();

        expect(s.evaluate("EXPRESSION", Iterator.class)).andReturn(i);

        expect(s.getSymbols()).andReturn(symbols).times(5);

        replay();

        ForeachToken t = new ForeachToken("value", null, "EXPRESSION", null);
        t.addToken(new EchoToken("value"));
        t.addToken(new EchoToken("index"));

        t.write(buffer, s);

        verify();

        assertEquals("buffy\nnone\nangel\nnone\n", buffer.toString());

        assertEquals("angel", symbols.get("value"));
        assertEquals("none", symbols.get("index"));
    }

    public void testNullIterator()
    {
        ScriptSession s = newMock(ScriptSession.class);

        expect(s.evaluate("EXPRESSION", Iterator.class)).andReturn(null);

        IScriptToken inner = newMock(IScriptToken.class);

        replay();
        ForeachToken t = new ForeachToken("value", "index", "EXPRESSION", null);
        t.addToken(inner);

        t.write(null, s);

        verify();
    }
}