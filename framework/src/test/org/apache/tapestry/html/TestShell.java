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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.test.Creator;
import org.easymock.MockControl;

/**
 * Tests for the {@link org.apache.tapestry.html.Shell}&nbsp; component.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestShell extends HiveMindTestCase
{
    private Creator _creator = new Creator();

    private IMarkupWriter newMarkupWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    private IRequestCycle newRequestCycle(boolean rewinding)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.isRewinding();
        control.setReturnValue(rewinding);

        return cycle;
    }

    private IRender newRender()
    {
        return (IRender) newMock(IRender.class);
    }

    /**
     * Test that Shell does very little when the entire page is rewinding (which itself is a
     * holdback to the action service).
     */

    public void testRewinding()
    {
        IMarkupWriter writer = newMarkupWriter();
        IRequestCycle cycle = newRequestCycle(true);

        IRender body = newRender();

        body.render(writer, cycle);

        replayControls();

        Shell shell = (Shell) _creator.newInstance(Shell.class);

        shell.addBody(body);

        shell.render(writer, cycle);

        verifyControls();
    }
}