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

package org.apache.tapestry.multipart;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.multipart.UploadFormParametersWrapper}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestUploadFormParametersWrapper extends HiveMindTestCase
{
    private HttpServletRequest newRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    public void testMapIsNotModifiable()
    {
        HttpServletRequest request = newRequest();

        replayControls();

        Map map = new HashMap();

        map.put("key", new String[]
        { "value" });

        HttpServletRequest r = new UploadFormParametersWrapper(request, map);

        Map pmap = r.getParameterMap();

        assertNotSame(map, pmap);

        assertSame(map.get("key"), pmap.get("key"));

        try
        {
            pmap.put("foo", "bar");
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

    public void testGetSingleParameterWhenNull()
    {
        HttpServletRequest request = newRequest();

        replayControls();

        HttpServletRequest r = new UploadFormParametersWrapper(request, new HashMap());

        assertNull(r.getParameter("unknown-key"));

        verifyControls();
    }

    public void testGetSingleParameterWhenEmptyArray()
    {
        HttpServletRequest request = newRequest();

        replayControls();

        HashMap params = new HashMap();

        params.put("empty-key", new String[0]);

        HttpServletRequest r = new UploadFormParametersWrapper(request, params);

        assertNull(r.getParameter("empty-key"));

        verifyControls();
    }

    public void testGetParameterValues()
    {
        String[] values =
        { "fred", "barney" };

        HttpServletRequest request = newRequest();

        replayControls();

        HashMap params = new HashMap();

        params.put("key", values);

        HttpServletRequest r = new UploadFormParametersWrapper(request, params);

        assertSame(values, r.getParameterValues("key"));

        verifyControls();
    }

    public void testGetParameterNames()
    {
        HttpServletRequest request = newRequest();

        replayControls();

        HashMap params = new HashMap();

        params.put("key", new String[0]);

        HttpServletRequest r = new UploadFormParametersWrapper(request, params);

        Enumeration e = r.getParameterNames();

        assertEquals(true, e.hasMoreElements());
        assertEquals("key", e.nextElement());
        assertEquals(false, e.hasMoreElements());

        verifyControls();
    }
}