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

import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.StaticLink;

/**
 *  Tests for {@link org.apache.tapestry.link.StaticLink}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class TestStaticLink extends TapestryTestCase
{
    private static final String URL = "http://host/path";

    ILink l = new StaticLink(URL);

    public void testURL()
    {
        assertEquals(URL, l.getURL());
    }

    public void testAbsoluteURL()
    {
        assertEquals(URL, l.getAbsoluteURL());
    }

    public void testURLWithAnchor()
    {
        assertEquals(URL, l.getURL(null, false));
        assertEquals(URL + "#anchor", l.getURL("anchor", false));
        assertEquals(URL + "#feeble", l.getURL("feeble", true));
    }

    public void testAbsoluteURLWithParameters()
    {
        assertEquals(URL + "#anchor", l.getAbsoluteURL("scheme", "server", 8080, "anchor", false));
    }

    public void testGetParameterNames()
    {
        assertEquals(null, l.getParameterNames());
    }

    public void testGetParameterValues()
    {
        try
        {
            l.getParameterValues("any");

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
        }
    }
}
