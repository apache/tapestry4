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

import javax.portlet.RenderResponse;

import org.apache.tapestry.util.ContentType;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.RenderWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestRenderWebResponse extends BasePortletWebTestCase
{
    private RenderResponse newResponse()
    {
        return (RenderResponse) newMock(RenderResponse.class);
    }

    public void testReset()
    {
        RenderResponse response = newResponse();

        response.reset();

        replayControls();

        RenderWebResponse rwr = new RenderWebResponse(response);

        rwr.reset();

        verifyControls();
    }

    public void testGetOutputStream() throws Exception
    {
        MockControl control = newControl(RenderResponse.class);
        RenderResponse response = (RenderResponse) control.getMock();
        replayControls();

        RenderWebResponse rwr = new RenderWebResponse(response);

        try
        {
            rwr.getOutputStream(new ContentType("foo/bar"));
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testGetNamespace()
    {
        MockControl control = newControl(RenderResponse.class);
        RenderResponse response = (RenderResponse) control.getMock();

        response.getNamespace();
        control.setReturnValue("_NAMESPACE_");

        replayControls();

        RenderWebResponse rwr = new RenderWebResponse(response);

        assertEquals("_NAMESPACE_", rwr.getNamespace());

        verifyControls();
    }
}