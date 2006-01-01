// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.util.ContentType;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
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
            pwr.getOutputStream(new ContentType("foo/bar"));
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testGetNamespace() throws Exception
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        assertEquals("", pwr.getNamespace());

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

    public void testSetDateHeaderUnsupported()
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.setDateHeader(null, 0);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testSetHeaderUnsupported()
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.setHeader(null, null);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testSetIntHeaderUnsupported()
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.setIntHeader(null, 0);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testSendErrorUnsupported() throws Exception
    {
        PortletResponse response = newResponse();

        replayControls();

        PortletWebResponse pwr = new PortletWebResponse(response);

        try
        {
            pwr.sendError(99, "foo!");
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