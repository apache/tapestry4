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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.util.ContentType;
import org.testng.annotations.Test;

import javax.portlet.RenderResponse;

/**
 * Tests for {@link org.apache.tapestry.portlet.RenderWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestRenderWebResponse extends BasePortletWebTestCase
{
    private RenderResponse newResponse()
    {
        return newMock(RenderResponse.class);
    }

    public void testReset()
    {
        RenderResponse response = newResponse();

        response.reset();

        replay();

        RenderWebResponse rwr = new RenderWebResponse(response);

        rwr.reset();

        verify();
    }

    public void testGetOutputStream() throws Exception
    {
        RenderResponse response = newMock(RenderResponse.class);
        
        replay();

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

        verify();
    }

    public void testGetNamespace()
    {
        RenderResponse response = newMock(RenderResponse.class);
        
        expect(response.getNamespace()).andReturn("_NAMESPACE_");
        
        replay();

        RenderWebResponse rwr = new RenderWebResponse(response);

        assertEquals("_NAMESPACE_", rwr.getNamespace());

        verify();
    }
}
