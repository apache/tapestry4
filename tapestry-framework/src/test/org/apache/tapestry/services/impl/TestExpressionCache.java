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
import ognl.Node;
import ognl.OgnlContext;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.services.impl.ExpressionCacheImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(enabled = false)
public class TestExpressionCache extends BaseComponentTestCase
{
    public void test_Valid_Expression()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();

        Object compiled = ec.getCompiledExpression("foo ? bar : baz");

        assertNotNull(compiled);
    }

    public void test_Caching()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();
        
        Object c1 = ec.getCompiledExpression("foo + bar");
        Object c2 = ec.getCompiledExpression("zip.zap.zoom");
        Object c3 = ec.getCompiledExpression("foo + bar");

        assertSame(c1, c3);
        assertNotSame(c1, c2);
    }

    public void test_Compiled_Caching()
    {
        ExpressionEvaluator evaluator = newMock(ExpressionEvaluator.class);
        ExpressionCacheImpl ec = new ExpressionCacheImpl();
        ec.setEvaluator(evaluator);
        
        BasicObject target = new BasicObject();
        OgnlContext context = new OgnlContext();
        
        expect(evaluator.createContext(target)).andReturn(context);
        
        replay();
        
        Node e1 = (Node)ec.getCompiledExpression(target, "value");
        
        assertNotNull(e1.getAccessor());
        assertEquals(e1.getAccessor().get(context, target), "foo");
        
        Node e2 = (Node)ec.getCompiledExpression(target, "value");
        
        assertSame(e1, e2);
        
        verify();
    }
    
    public void test_Invalid_Expression()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();

        try
        {
            ec.getCompiledExpression("foo and bar and");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(ex, "Unable to parse OGNL expression 'foo and bar and'");
        }
    }
    
    // fails only when running from command line, must be threading issue
    @Test(enabled = false)
    public void test_Clear_Cache()
    {
        ExpressionEvaluator evaluator = newMock(ExpressionEvaluator.class);
        ExpressionCacheImpl ec = new ExpressionCacheImpl();
        ec.setEvaluator(evaluator);
        
        BasicObject target = new BasicObject();
        OgnlContext context = new OgnlContext();
        
        expect(evaluator.createContext(target)).andReturn(context).anyTimes();
        
        replay();
        
        Node e1 = (Node)ec.getCompiledExpression(target, "value");
        
        ec.resetEventDidOccur();
        
        Node e2 = (Node)ec.getCompiledExpression(target, "value");
        
        assertNotSame(e1, e2);
        
        verify();
    }

}
