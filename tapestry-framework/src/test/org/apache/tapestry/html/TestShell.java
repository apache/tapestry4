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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.test.Creator;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.html.Shell}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestShell extends BaseComponentTestCase
{
    private Creator _creator = new Creator();

    /**
     * Test that Shell does very little when the entire page is rewinding (which itself is a
     * holdback to the action service).
     */

    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();

        IRequestCycle cycle = newCycle(true, writer);
        IRender body = newRender();

        Shell shell = (Shell) _creator.newInstance(Shell.class);
        shell.addBody(body);

        trainStoreShellInCycle(cycle, shell);
        trainGetNestedWriter(writer, nested);
        body.render(nested, cycle);
        nested.close();
        trainRemoveShellFromCycle(cycle);

        replay();

        shell.render(writer, cycle);

        verify();
    }

    protected void trainStoreShellInCycle(IRequestCycle cycle, Shell shell)
    {
        cycle.getAttribute(Shell.SHELL_ATTRIBUTE);
        setReturnValue(null);
        cycle.setAttribute(Shell.SHELL_ATTRIBUTE, shell);
    }

    protected void trainRemoveShellFromCycle(IRequestCycle cycle)
    {
        cycle.removeAttribute(Shell.SHELL_ATTRIBUTE);
    }
}