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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.services.impl.ExpressionCacheImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestExpressionCache extends HiveMindTestCase
{
    public void testValidExpression()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();

        Object compiled = ec.getCompiledExpression("foo ? bar : baz");

        assertNotNull(compiled);
    }

    public void testCaching()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();

        Object c1 = ec.getCompiledExpression("foo + bar");
        Object c2 = ec.getCompiledExpression("zip.zap.zoom");
        Object c3 = ec.getCompiledExpression("foo + bar");

        assertSame(c1, c3);
        assertNotSame(c1, c2);
    }

    public void testInvalidExpression()
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
    
    public void testClearCache()
    {
        ExpressionCacheImpl ec = new ExpressionCacheImpl();

        Object c1 = ec.getCompiledExpression("foo + bar");
        
        ec.resetEventDidOccur();
        
        Object c2 = ec.getCompiledExpression("foo + bar");

        assertNotSame(c1, c2);
    }

}