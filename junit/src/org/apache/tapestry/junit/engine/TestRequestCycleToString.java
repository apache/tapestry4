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

package org.apache.tapestry.junit.engine;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.junit.TapestryTestCase;

/**
 * Tests {@link org.apache.tapestry.engine.RequestCycle#toString()}.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class TestRequestCycleToString extends TapestryTestCase
{

    private String toString(Object object)
    {
        String raw = object.toString();

        int bracketx = raw.indexOf("[");

        return raw.substring(bracketx + 1, raw.length() - 1);
    }

    public void testAllNull()
    {
        IRequestCycle cycle = new RequestCycle(null, null, null, null);

        assertEquals(
            "rewinding=false,serviceParameters=<null>,attributes=<null>,targetActionId=0,targetComponent=<null>",
            toString(cycle));

    }

    public void testWithServiceParameters()
    {
        Object[] sp = new Object[] { "alpha", "beta" };

        RequestCycle cycle = new RequestCycle(null, null, null, null);
        cycle.setServiceParameters(sp);

        assertEquals(
            "rewinding=false,serviceParameters={alpha,beta},attributes=<null>,targetActionId=0,targetComponent=<null>",
            toString(cycle));
    }

    public void testWithService()
    {
        IEngineService service = new MockService("test");

        IRequestCycle cycle = new RequestCycle(null, null, service, null);

        assertEquals(
            "rewinding=false,service=test,serviceParameters=<null>,attributes=<null>,targetActionId=0,targetComponent=<null>",
            toString(cycle));
    }

    public void testWithAttributes()
    {
        IRequestCycle cycle = new RequestCycle(null, null, null, null);

        cycle.setAttribute("foo.bar", "baz");

        assertEquals(
            "rewinding=false,serviceParameters=<null>,attributes={foo.bar=baz},targetActionId=0,targetComponent=<null>",
            toString(cycle));
    }

    // TODO: An amazing amount of work to be able to test loaded pages ... may need
    // to do so inside a mock unit test suite since so much machinery is involved.

}
