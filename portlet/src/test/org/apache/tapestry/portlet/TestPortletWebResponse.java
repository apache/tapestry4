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

package org.apache.tapestry.portlet;

import javax.portlet.PortletResponse;

import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletWebResponse extends BasePortletWebTestCase
{
    private PortletResponse newResponse()
    {
        return (PortletResponse) newMock(PortletResponse.class);
    }

    public void testGetOutputStreamUnsupported() throws Exception
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.getOutputStream();
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testResetUnsupported()
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.reset();
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testSetContentTypeUnsupported()
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.setContentType("foo");
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testEncodeURL()
    {
        MockControl control = newControl(PortletResponse.class);
        PortletResponse response = (PortletResponse) control.getMock();

        response.encodeURL("/foo");
        control.setReturnValue("/foo;encoded");

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        assertEquals("/foo;encoded", pwr.encodeURL("/foo"));

        verifyControls();
    }
}