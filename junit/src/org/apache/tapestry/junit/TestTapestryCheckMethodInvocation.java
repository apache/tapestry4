//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;

/**
 * Tests for the methods 
 * {@link org.apache.tapestry.Tapestry#checkMethodInvocation(Object, String, Object)},
 * {@link org.apache.tapestry.Tapestry#addMethodInvocation(Object)} and
 * {@link org.apache.tapestry.Tapestry#clearMethodInvocations()}.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 **/
public class TestTapestryCheckMethodInvocation extends TapestryTestCase
{

    public void testSuccess()
    {
        Tapestry.clearMethodInvocations();
        Tapestry.addMethodInvocation("alpha");
        Tapestry.addMethodInvocation("beta");

        Tapestry.checkMethodInvocation("alpha", "alpha()", this);
        Tapestry.checkMethodInvocation("beta", "beta()", this);
    }

    public void testFail()
    {
        Tapestry.clearMethodInvocations();

        try
        {
            Tapestry.checkMethodInvocation("gamma", "gamma()", this);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                "Class org.apache.tapestry.junit.TestTapestryCheckMethodInvocation overrides method 'gamma()' but does not invoke the super-class implementation.",
                ex.getMessage());
        }
    }

}
